package io.datalayer.sql.dao.dao2.ibatis;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Scanner;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapClientBuilder;

public class ibatisTest {

    public static void main(String[] args) throws SQLException, IOException {
        InputStream is = ibatisTest.class.getClassLoader().getResourceAsStream("dbSource.xml");
        System.out.println(is.available());

        SqlMapClient client = SqlMapClientBuilder.buildSqlMapClient(is);

        try {
            client.startTransaction();

            System.out.println(client.queryForObject("lcokNote", 1));
            System.out.println(client.update("incrementNote", 1));

            new Scanner(System.in).next();

            client.commitTransaction();
        }
        catch (Exception e) {
            client.endTransaction();
        }

    }
}
