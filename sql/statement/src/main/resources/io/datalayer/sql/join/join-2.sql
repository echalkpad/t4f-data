Join (SQL)
From Wikipedia, the free encyclopedia

A SQL join clause combines records from two or more tables in a database. It creates a set that can be saved as a table or used as it is. A JOIN is a means for combining fields from two tables by using values common to each. ANSI-standard SQL specifies five types of JOIN: INNER, LEFT OUTER, RIGHT OUTER, FULL OUTER and CROSS. As a special case, a table (base table, view, or joined table) can JOIN to itself in a self-join.

A programmer writes a JOIN statement to identify the records for joining. If the evaluated predicate is true, the combined record is then produced in the expected format, a record set or a temporary table.

Contents

    1 Sample tables
    2 Cross join
    3 Inner join
        3.1 Equi-join
            3.1.1 Natural join
    4 Outer join
        4.1 Left outer join
            4.1.1 Alternative syntaxes
        4.2 Right outer join
        4.3 Full outer join
    5 Self-join
        5.1 Example
    6 Alternatives
    7 Implementation
        7.1 Join algorithms
        7.2 Join Indexes
    8 See also
    9 Notes
    10 References
    11 External links

Sample tables

Relational databases are usually normalized to eliminate duplication of information such as when objects have one-to-many relationships. For example, a Department may be associated with a number of Employees. Joining separate tables for Department and Employee effectively creates another table which combines the information from both tables. This is at some expense in terms of the time it takes to compute the join. While it is also possible to simply maintain a denormalized table if speed is important, duplicate information may take extra space, and add the expense and complexity of maintaining data integrity if data which is duplicated later changes.

All subsequent explanations on join types in this article make use of the following two tables. The rows in these tables serve to illustrate the effect of different types of joins and join-predicates. In the following tables the DepartmentID column of the Department table (which can be designated as Department.DepartmentID) is the primary key, while Employee.DepartmentID is a foreign key.
Employee table LastName     DepartmentID
Rafferty    31
Jones   33
Heisenberg  33
Robinson    34
Smith   34
John    NULL
Department table DepartmentID   DepartmentName
31  Sales
33  Engineering
34  Clerical
35  Marketing


Note: In the Employee table above, the employee "John" has not been assigned to any department yet. Also, note that no employees are assigned to the "Marketing" department.

This is the SQL to create the aforementioned tables.

CREATE TABLE department
(
 DepartmentID INT,
 DepartmentName VARCHAR(20)
);
 
CREATE TABLE employee
(
 LastName VARCHAR(20),
 DepartmentID INT
);
 
INSERT INTO department VALUES(31, 'Sales');
INSERT INTO department VALUES(33, 'Engineering');
INSERT INTO department VALUES(34, 'Clerical');
INSERT INTO department VALUES(35, 'Marketing');
 
INSERT INTO employee VALUES('Rafferty', 31);
INSERT INTO employee VALUES('Jones', 33);
INSERT INTO employee VALUES('Heisenberg', 33);
INSERT INTO employee VALUES('Robinson', 34);
INSERT INTO employee VALUES('Smith', 34);
INSERT INTO employee VALUES('John', NULL);

Cross join

CROSS JOIN returns the Cartesian product of rows from tables in the join. In other words, it will produce rows which combine each row from the first table with each row from the second table.[1]

Example of an explicit cross join:

SELECT *
FROM employee CROSS JOIN department;

Example of an implicit cross join:

SELECT *
FROM employee, department;

Employee.LastName   Employee.DepartmentID   Department.DepartmentName   Department.DepartmentID
Rafferty    31  Sales   31
Jones   33  Sales   31
Heisenberg  33  Sales   31
Smith   34  Sales   31
Robinson    34  Sales   31
John    NULL    Sales   31
Rafferty    31  Engineering     33
Jones   33  Engineering     33
Heisenberg  33  Engineering     33
Smith   34  Engineering     33
Robinson    34  Engineering     33
John    NULL    Engineering     33
Rafferty    31  Clerical    34
Jones   33  Clerical    34
Heisenberg  33  Clerical    34
Smith   34  Clerical    34
Robinson    34  Clerical    34
John    NULL    Clerical    34
Rafferty    31  Marketing   35
Jones   33  Marketing   35
Heisenberg  33  Marketing   35
Smith   34  Marketing   35
Robinson    34  Marketing   35
John    NULL    Marketing   35

The cross join does not itself apply any predicate to filter records from the joined table. The results of a cross join can be filtered by using a WHERE clause which may then produce the equivalent of an inner join.

In the SQL:2011 standard, cross joins are part of the optional F401, "Extended joined table", package.
Inner join

An 'inner join' is a commonly used join operation used in applications. Inner join creates a new result table by combining column values of two tables (A and B) based upon the join-predicate. The query compares each row of A with each row of B to find all pairs of rows which satisfy the join-predicate. When the join-predicate is satisfied, column values for each matched pair of rows of A and B are combined into a result row.

The result of the join can be defined as the outcome of first taking the Cartesian product (or Cross join) of all records in the tables (combining every record in table A with every record in table B) and then returning all records which satisfy the join predicate. Actual SQL implementations normally use other approaches, such as hash joins or sort-merge joins, since computing the Cartesian product is very inefficient.

SQL specifies two different syntactical ways to express joins: "explicit join notation" and "implicit join notation".

The "explicit join notation" uses the JOIN keyword, optionally preceded by the INNER keyword, to specify the table to join, and the ON keyword to specify the predicates for the join, as in the following example:

SELECT *
FROM employee INNER JOIN department
  ON employee.DepartmentID = department.DepartmentID;

The "implicit join notation" simply lists the tables for joining, in the FROM clause of the SELECT statement, using commas to separate them. Thus it specifies a cross join, and the WHERE clause may apply additional filter-predicates (which function comparably to the join-predicates in the explicit notation).

The following example is equivalent to the previous one, but this time using implicit join notation:

SELECT *
FROM employee, department
WHERE employee.DepartmentID = department.DepartmentID;

The queries given in the examples above will join the Employee and Department tables using the DepartmentID column of both tables. Where the DepartmentID of these tables match (i.e. the join-predicate is satisfied), the query will combine the LastName, DepartmentID and DepartmentName columns from the two tables into a result row. Where the DepartmentID does not match, no result row is generated.

Thus the result of the execution of either of the two queries above will be:
Employee.LastName   Employee.DepartmentID   Department.DepartmentName   Department.DepartmentID
Robinson    34  Clerical    34
Jones   33  Engineering     33
Smith   34  Clerical    34
Heisenberg  33  Engineering     33
Rafferty    31  Sales   31

Notice that the employee "John" and the department "Marketing" do not appear in the query execution results. Neither of these has any matching records in the other respective table: "John" has no associated department, and no employee has the department ID 35 ("Marketing"). Depending on the desired results, this behavior may be a subtle bug, which can be avoided with an outer join.

Note: Programmers should take special care when joining tables on columns that can contain NULL values, since NULL will never match any other value (not even NULL itself), unless the join condition explicitly uses the IS NULL or IS NOT NULL predicates. The Inner join can only be safely used in a database that enforces referential integrity or where the join fields are guaranteed not to be NULL. Many transaction processing relational databases rely on Atomicity, Consistency, Isolation, Durability (ACID) data update standards to ensure data integrity, making inner joins an appropriate choice. Many reporting relational database and data warehouses use high volume Extract, Transform, Load (ETL) batch updates which make referential integrity difficult or impossible to enforce, resulting in potentially NULL join fields that a SQL query author cannot modify and which cause inner joins to omit data with no indication of an error. The choice to use an inner join depends on the database design and data characteristics. A left outer join can usually be substituted for an inner join when the join field in one table may contain NULL values. A commitment to an inner join assumes NULL join fields will not be introduced by future changes, including vendor updates, design changes and bulk processing outside of the application's data validation rules such as data conversions.

One can further classify inner joins as equi-joins, as natural joins, or as cross-joins.
Equi-join

An equi-join is a specific type of comparator-based join, that uses only equality comparisons in the join-predicate. Using other comparison operators (such as <) disqualifies a join as an equi-join. The query shown above has already provided an example of an equi-join:

SELECT *
FROM employee JOIN department
  ON employee.DepartmentID = department.DepartmentID;

We can write equi-join as below,

SELECT *
FROM employee, department
WHERE employee.DepartmentID = department.DepartmentID;

If columns in an equi-join have the same name, SQL-92 provides an optional shorthand notation for expressing equi-joins, by way of the USING construct:[2]

SELECT *
FROM employee INNER JOIN department USING (DepartmentID);

The USING construct is more than mere syntactic sugar, however, since the result set differs from the result set of the version with the explicit predicate. Specifically, any columns mentioned in the USING list will appear only once, with an unqualified name, rather than once for each table in the join. In the case above, there will be a single DepartmentID column and no employee.DepartmentID or department.DepartmentID.

The USING clause is not supported by MS SQL Server and Sybase.
Natural join

A natural join is a type of equi-join where the join predicate arises implicitly by comparing all columns in both tables that have the same column-names in the joined tables. The resulting joined table contains only one column for each pair of equally named columns. In the case that no columns with the same names are found, a cross join is performed.

Most experts agree that NATURAL JOINs are dangerous and therefore strongly discourage their use.[3] The danger comes from inadvertently adding a new column, named the same as another column in the other table. An existing natural join might then "naturally" use the new column for comparisons, making comparisons/matches using different criteria (from different columns) than before. Thus an existing query could produce different results, even though the data in the tables have not been changed, but only augmented. The use of column names to automatically determine table links is not an option in large databases with hundreds or thousands of tables where it would place an unrealistic constraint on naming conventions. Real world databases are commonly designed with foreign key data that is not consistently populated (NULL values are allowed), due to business rules and context. It is common practice to modify column names of similar data in different tables and this lack of rigid consistency relegates natural joins to a theoretical concept for discussion.

The above sample query for inner joins can be expressed as a natural join in the following way:

SELECT *
FROM employee NATURAL JOIN department;

As with the explicit USING clause, only one DepartmentID column occurs in the joined table, with no qualifier:
DepartmentID    Employee.LastName   Department.DepartmentName
34  Smith   Clerical
33  Jones   Engineering
34  Robinson    Clerical
33  Heisenberg  Engineering
31  Rafferty    Sales

PostgreSQL, MySQL and Oracle support natural joins; Microsoft T-SQL and IBM DB2 do not. The columns used in the join are implicit so the join code does not show which columns are expected, and a change in column names may change the results. In the SQL:2011 standard, natural joins are part of the optional F401, "Extended joined table", package.

In many database environments the column names are controlled by an outside vendor, not the query developer. A natural join assumes stability and consistency in column names which can change during vendor mandated version upgrades.
Outer join

An outer join does not require each record in the two joined tables to have a matching record. The joined table retains each record—even if no other matching record exists. Outer joins subdivide further into left outer joins, right outer joins, and full outer joins, depending on which table's rows are retained (left, right, or both).

(In this case left and right refer to the two sides of the JOIN keyword.)

No implicit join-notation for outer joins exists in standard SQL.
Left outer join

The result of a left outer join (or simply left join) for tables A and B always contains all records of the "left" table (A), even if the join-condition does not find any matching record in the "right" table (B). This means that if the ON clause matches 0 (zero) records in B (for a given record in A), the join will still return a row in the result (for that record)—but with NULL in each column from B. A left outer join returns all the values from an inner join plus all values in the left table that do not match to the right table.

For example, this allows us to find an employee's department, but still shows the employee(s) even when they have not been assigned to a department (contrary to the inner-join example above, where unassigned employees were excluded from the result).

Example of a left outer join (the OUTER keyword is optional), with the additional result row (compared with the inner join) italicized:

SELECT *
FROM employee LEFT OUTER JOIN department
  ON employee.DepartmentID = department.DepartmentID;

Employee.LastName   Employee.DepartmentID   Department.DepartmentName   Department.DepartmentID
Jones   33  Engineering     33
Rafferty    31  Sales   31
Robinson    34  Clerical    34
Smith   34  Clerical    34
John    NULL    NULL    NULL
Heisenberg  33  Engineering     33
Alternative syntaxes

Oracle supports the deprecated[4] syntax:

SELECT *
FROM employee, department
WHERE employee.DepartmentID = department.DepartmentID(+)

Sybase supports the syntax:

SELECT *
FROM employee, department
WHERE employee.DepartmentID *= department.DepartmentID

IBM Informix supports the syntax:

SELECT *
FROM employee, OUTER department
WHERE employee.DepartmentID = department.DepartmentID

Right outer join

A right outer join (or right join) closely resembles a left outer join, except with the treatment of the tables reversed. Every row from the "right" table (B) will appear in the joined table at least once. If no matching row from the "left" table (A) exists, NULL will appear in columns from A for those records that have no match in B.

A right outer join returns all the values from the right table and matched values from the left table (NULL in the case of no matching join predicate). For example, this allows us to find each employee and his or her department, but still show departments that have no employees.

Below is an example of a right outer join (the OUTER keyword is optional), with the additional result row italicized:

SELECT *
FROM employee RIGHT OUTER JOIN department
  ON employee.DepartmentID = department.DepartmentID;

Employee.LastName   Employee.DepartmentID   Department.DepartmentName   Department.DepartmentID
Smith   34  Clerical    34
Jones   33  Engineering     33
Robinson    34  Clerical    34
Heisenberg  33  Engineering     33
Rafferty    31  Sales   31
NULL    NULL    Marketing   35

Right and left outer joins are functionally equivalent. Neither provides any functionality that the other does not, so right and left outer joins may replace each other as long as the table order is switched.
Full outer join

Conceptually, a full outer join combines the effect of applying both left and right outer joins. Where records in the FULL OUTER JOINed tables do not match, the result set will have NULL values for every column of the table that lacks a matching row. For those records that do match, a single row will be produced in the result set (containing fields populated from both tables).

For example, this allows us to see each employee who is in a department and each department that has an employee, but also see each employee who is not part of a department and each department which doesn't have an employee.

Example of a full outer join (the OUTER keyword is optional):

SELECT *
FROM employee FULL OUTER JOIN department
  ON employee.DepartmentID = department.DepartmentID;

Employee.LastName   Employee.DepartmentID   Department.DepartmentName   Department.DepartmentID
Smith   34  Clerical    34
Jones   33  Engineering     33
Robinson    34  Clerical    34
John    NULL    NULL    NULL
Heisenberg  33  Engineering     33
Rafferty    31  Sales   31
NULL    NULL    Marketing   35

Some database systems do not support the full outer join functionality directly, but they can emulate it through the use of an inner join and UNION ALL selects of the "single table rows" from left and right tables respectively. The same example can appear as follows:

SELECT employee.LastName, employee.DepartmentID,
       department.DepartmentName, department.DepartmentID
FROM employee
INNER JOIN department ON employee.DepartmentID = department.DepartmentID
 
UNION ALL
 
SELECT employee.LastName, employee.DepartmentID,
       CAST(NULL AS VARCHAR(20)), CAST(NULL AS INTEGER)
FROM employee
WHERE NOT EXISTS (
    SELECT * FROM department
             WHERE employee.DepartmentID = department.DepartmentID)
 
UNION ALL
 
SELECT CAST(NULL AS VARCHAR(20)), CAST(NULL AS INTEGER),
       department.DepartmentName, department.DepartmentID
FROM department
WHERE NOT EXISTS (
    SELECT * FROM employee
             WHERE employee.DepartmentID = department.DepartmentID)

Self-join

A self-join is joining a table to itself.[5]
Example

A query to find all pairings of two employees in the same country is desired. If there were two separate tables for employees and a query which requested employees in the first table having the same country as employees in the second table, a normal join operation could be used to find the answer table. However, all the employee information is contained within a single large table.[6]

Consider a modified Employee table such as the following:
Employee Table EmployeeID   LastName    Country     DepartmentID
123     Rafferty    Australia   31
124     Jones   Australia   33
145     Heisenberg  Australia   33
201     Robinson    United States   34
305     Smith   Germany     34
306     John    Germany     NULL


An example solution query could be as follows:

SELECT F.EmployeeID, F.LastName, S.EmployeeID, S.LastName, F.Country
FROM Employee F INNER JOIN Employee S ON F.Country = S.Country
WHERE F.EmployeeID < S.EmployeeID
ORDER BY F.EmployeeID, S.EmployeeID;

Which results in the following table being generated.
Employee Table after Self-join by Country EmployeeID    LastName    EmployeeID  LastName    Country
123     Rafferty    124     Jones   Australia
123     Rafferty    145     Heisenberg  Australia
124     Jones   145     Heisenberg  Australia
305     Smith   306     John    Germany


For this example:

    F and S are aliases for the first and second copies of the employee table.
    The condition F.Country = S.Country excludes pairings between employees in different countries. The example question only wanted pairs of employees in the same country.
    The condition F.EmployeeID < S.EmployeeID excludes pairings where the EmployeeID of the first employee is greater than or equal to the EmployeeID of the second employee. In other words, the effect of this condition is to exclude duplicate pairings and self-pairings. Without it, the following less useful table would be generated (the table below displays only the "Germany" portion of the result):

EmployeeID  LastName    EmployeeID  LastName    Country
305     Smith   305     Smith   Germany
305     Smith   306     John    Germany
306     John    305     Smith   Germany
306     John    306     John    Germany


Only one of the two middle pairings is needed to satisfy the original question, and the topmost and bottommost are of no interest at all in this example.
Alternatives

The effect of an outer join can also be obtained using a UNION ALL between an INNER JOIN and a SELECT of the rows in the "main" table that do not fulfill the join condition. For example

SELECT employee.LastName, employee.DepartmentID, department.DepartmentName
FROM employee
LEFT OUTER JOIN department ON employee.DepartmentID = department.DepartmentID;

can also be written as

SELECT employee.LastName, employee.DepartmentID, department.DepartmentName
FROM employee
INNER JOIN department ON employee.DepartmentID = department.DepartmentID
 
UNION ALL
 
SELECT employee.LastName, employee.DepartmentID, CAST(NULL AS VARCHAR(20))
FROM employee
WHERE NOT EXISTS (
    SELECT * FROM department
             WHERE employee.DepartmentID = department.DepartmentID)

Implementation

Much work in database-systems has aimed at efficient implementation of joins, because relational systems commonly call for joins, yet face difficulties in optimising their efficient execution. The problem arises because inner joins operate both commutatively and associatively. In practice, this means that the user merely supplies the list of tables for joining and the join conditions to use, and the database system has the task of determining the most efficient way to perform the operation. A query optimizer determines how to execute a query containing joins. A query optimizer has two basic freedoms:

    Join order: Because it joins functions commutatively and associatively, the order in which the system joins tables does not change the final result set of the query. However, join-order could have an enormous impact on the cost of the join operation, so choosing the best join order becomes very important.
    Join method: Given two tables and a join condition, multiple algorithms can produce the result set of the join. Which algorithm runs most efficiently depends on the sizes of the input tables, the number of rows from each table that match the join condition, and the operations required by the rest of the query.

Many join-algorithms treat their inputs differently. One can refer to the inputs to a join as the "outer" and "inner" join operands, or "left" and "right", respectively. In the case of nested loops, for example, the database system will scan the entire inner relation for each row of the outer relation.

One can classify query-plans involving joins as follows:[7]

left-deep 
    using a base table (rather than another join) as the inner operand of each join in the plan
right-deep 
    using a base table as the outer operand of each join in the plan
bushy 
    neither left-deep nor right-deep; both inputs to a join may themselves result from joins

These names derive from the appearance of the query plan if drawn as a tree, with the outer join relation on the left and the inner relation on the right (as convention dictates).
Join algorithms

Three fundamental algorithms for performing a join operation exist: nested loop join, sort-merge join and hash join.
Join Indexes

Join indexes are database indexes that facilitate the processing of join queries in data warehouses: they are currently (2012) available in implementations by Oracle[8] and Teradata.[9]

In the Teradata implementation, specified columns, aggregate functions on columns, or components of date columns from one or more tables are specified using a syntax similar to the definition of a database view: up to 64 columns/column expressions can be specified in a single join index. Optionally, a column that defines the primary key of the composite data may also be specified: on parallel hardware, the column values are used to partition the index's contents across multiple disks. When the source tables are updated interactively by users, the contents of the join index are automatically updated. Any query whose WHERE clause specifies any combination of columns or column expressions that are an exact subset of those defined in a join index (a so-called "covering query") will cause the join index, rather than the original tables and their indexes, to be consulted during query execution.

The Oracle implementation limits itself to using bitmap indexes. A bitmap join index is used for low-cardinality columns (i.e., columns containing fewer than 300 distinct values, according to the Oracle documentation): it combines low-cardinality columns from multiple related tables. The example Oracle uses is that of an inventory system, where different suppliers provide different parts. The schema has three linked tables: two "master tables", Part and Supplier, and a "detail table", Inventory. The last is a many-to-many table linking Supplier to Part, and contains the most rows. Every part has a Part Type, and every supplier is based in the USA, and has a State column. There are not more than 60 states+territories in the USA, and not more than 300 Part Types. The bitmap join index is defined using a standard three-table join on the three tables above, and specifying the Part_Type and Supplier_State columns for the index. However, it is defined on the Inventory table, even though the columns Part_Type and Supplier_State are "borrowed" from Supplier and Part respectively.

As for Teradata, an Oracle bitmap join index is only utilized to answer a query when the query's WHERE clause specifies columns limited to those that are included in the join index.