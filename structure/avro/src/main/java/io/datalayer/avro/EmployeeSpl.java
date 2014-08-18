package io.datalayer.avro;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.avro.Schema;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.io.Encoder;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.util.Utf8;

/**
 * A simple entity used to test Avro schema evolution properties.
 * 
 */
class EmployeeSpl {
    public static Schema SCHEMA; // writer's schema
    public static Schema SCHEMA2; // reader's schema

    static {
        try {
            SCHEMA = Schema.parse(EmployeeSpl.class.getResourceAsStream("Employee.avsc"));
            SCHEMA2 = Schema.parse(EmployeeSpl.class.getResourceAsStream("Employee2.avsc"));
        }
        catch (IOException e) {
            System.out.println("Couldn't load a schema: " + e.getMessage());
        }
    }

    private String name;
    private int age;
    private String[] mails;
    private EmployeeSpl boss;

    public EmployeeSpl(String name, int age, String[] emails, EmployeeSpl b) {
        this.name = name;
        this.age = age;
        this.mails = emails;
        this.boss = b;
    }

    /**
     * This method serializes the java object into Avro record.
     * 
     * @return Avro generic record
     */
    public GenericData.Record serialize() {
        GenericData.Record record = new GenericData.Record(SCHEMA);

        record.put("name", this.name);
        record.put("age", this.age);

        int nemails = (mails != null) ? this.mails.length : 0;
        GenericData.Array emails = new GenericData.Array(nemails, SCHEMA.getField("emails").schema());
        for (int i = 0; i < nemails; ++i)
            emails.add(new Utf8(this.mails[i]));
        record.put("emails", emails);

        if (this.boss != null)
            record.put("boss", this.boss.serialize());

        return record;
    }

    /**
     * Writes out Java objects into a binary Avro-encoded file
     * 
     * @param file
     *            where to store serialized Avro records
     * @param people
     *            is an array of objects to be serialized
     * @throws IOException
     */
    public static void testWrite(File file, EmployeeSpl[] people) throws IOException {
        GenericDatumWriter datum = new GenericDatumWriter(EmployeeSpl.SCHEMA);
        DataFileWriter writer = new DataFileWriter(datum);

        writer.setMeta("Meta-Key0", "Meta-Value0");
        writer.setMeta("Meta-Key1", "Meta-Value1");

        writer.create(EmployeeSpl.SCHEMA, file);
        for (EmployeeSpl p : people)
            writer.append(p.serialize());

        writer.close();
    }

    /**
     * Writes out Java objects into a JSON-encoded file
     * 
     * @param file
     *            where to store serialized Avro records
     * @param people
     *            people is an array of objects to be serialized
     * @throws IOException
     */
    public static void testJsonWrite(File file, EmployeeSpl[] people) throws IOException {
        GenericDatumWriter writer = new GenericDatumWriter(EmployeeSpl.SCHEMA);
        Encoder e = EncoderFactory.get().jsonEncoder(EmployeeSpl.SCHEMA, new FileOutputStream(file));

        for (EmployeeSpl p : people)
            writer.write(p.serialize(), e);

        e.flush();
    }

    /**
     * Reads in binary Avro-encoded entities using the schema stored in the file
     * and prints them out.
     * 
     * @param file
     * @throws IOException
     */
    public static void testRead(File file) throws IOException {
        GenericDatumReader datum = new GenericDatumReader();
        DataFileReader reader = new DataFileReader(file, datum);

        GenericData.Record record = new GenericData.Record(reader.getSchema());
        while (reader.hasNext()) {
            reader.next(record);
            System.out.println("Name " + record.get("name") + " Age " + record.get("age") + " @ "
                    + record.get("emails"));
        }

        reader.close();
    }

    /**
     * Reads in binary Avro-encoded entities using a schema that is different
     * from the writer's schema.
     * 
     * @param file
     * @throws IOException
     */
    public static void testRead2(File file) throws IOException {
        GenericDatumReader datum = new GenericDatumReader(EmployeeSpl.SCHEMA2);
        DataFileReader reader = new DataFileReader(file, datum);

        GenericData.Record record = new GenericData.Record(EmployeeSpl.SCHEMA2);
        while (reader.hasNext()) {
            reader.next(record);
            System.out.println("Name " + record.get("name") + " " + record.get("yrs") + " yrs old " + " Gender "
                    + record.get("gender") + " @ " + record.get("emails"));
        }

        reader.close();
    }

    public static void main(String[] args) {
        EmployeeSpl e1 = new EmployeeSpl("Joe", 31, new String[] { "joe@abc.com", "joe@gmail.com" }, null);
        EmployeeSpl e2 = new EmployeeSpl("Jane", 30, null, e1);
        EmployeeSpl e3 = new EmployeeSpl("Zoe", 21, null, e2);
        EmployeeSpl[] all = new EmployeeSpl[] { e1, e2, e3 };

        File bf = new File("test.avro");
        File jf = new File("test.json");

        try {
            testWrite(bf, all);
            testRead(bf);
            testRead2(bf);

            testJsonWrite(jf, all);
        }
        catch (IOException e) {
            System.out.println("Main: " + e.getMessage());
        }
    }

}
