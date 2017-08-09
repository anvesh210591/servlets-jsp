# MySQL

- It is inexpensive, free for most uses.
- Mysql is one of the fastest RDBMS.
- It is easy to use.
- portable, runs on all systems.
- supports SQL
- supports multiple clients from variety of programming languages.
- MySQL can provide security.
- It now provides referential integrity and transaction processing.

- Create database

```sql
CREATE DATABASE database_name
```

- Select that database

```sql
USE database_name
```

- Drop a database

```sql
DROP DATABASE database_name
```

- Create table

```mysql
CREATE TABLE User (
  UserId INT NOT NULL AUTO_INCREMENT,
  Email VARCHAR(50),
  FirstName VARCHAR(50),
  LastName VARCHAR(50),
  PRIMARY KEY(UserId)
);
```

- drop TABLE

```sql
DROP TABLE User
```

```sql
DROP TABLE IF EXISTS User
```

- insert data into table

```sql
INSERT INTO
  User(FirstName, LastName, Email)
VALUES
  ('Piyush', 'Patel', 'ppatel@gmail.com'),
  ('Andre', 'Paul', 'apaul@gmail.com');
```

- select data from a table

```sql
SELECT * FROM table_name
  [WHERE condition]
  [ORDER BY column_name [ASC|DESC]]
```

```sql
SELECT * FROM User
```

```sql
SELECT FirstName, LastName
FROM User
WHERE UserId < 2
ORDER BY LastName ASC;
```

- inner join: data from the rows in the two tables are included in the result set only if their related columns match.
- outer join: data from one of the tables are included even if it doesn't match with data in other table. LEFT OUTER JOIN returns all the data from LEFT table and RIGHT OUTER JOIN returns all the data from the RIGHT table.

```sql
SELECT column_names
FROM table1
INNER JOIN table2
ON table1.column1=table2.column3;
```

```sql
SELECT Order.OrderId, Customers.CustomerName
FROM Orders
INNER JOIN Customers
ON Orders.CustomerID=Customers.CustomerID;
```

```sql
SELECT column_names
FROM table1
LEFT OUTER JOIN table2
ON table1.column1 = table2.column2;
```

- update a record.

```sql
UPDATE table_name
SET column1=value1, column2=value2, ...
WHERE condition;
```

```sql
UPDATE User
SET FirstName='Pratik'
WHERE email='ppatel@gmail.com';
```

- delete a record

```sql
DELETE FROM table_name
WHERE condition;
```

You can check sqlsite application, that is developed with MySQL. It's like an interactive SQL interface.
