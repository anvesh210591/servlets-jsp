# How to use JDBC to work with Databases

JDBC API requires to write a significant amount of low-level code. Many legacy applications use JDBC. The modern trend is to use JPA.

You must import JDBC driver to the application to connect to a database. Type1 and Type2 drivers require installation on the client side, so they are not suitable. So, you'll typically use type-3 or type-4.

Type-4 named Connector/J driver available free from MySQL website. Also included in Tomcat.

Database URL syntax
`jdbc:subprotocol:databasURL`.

`jdbc:mysql://localhost:3306/dbname`

How to connect to MySQL using automatic driver loading (JDBC 4.0)

```Java
try {
  String url = "jdbc:mysql://localhost:3306/my_db";
  String username = "root";
  String password = "rootPassword";
  Connection con = DriverManager.getConnection(url, username, password);
}
catch(SQLException e) {
  for(Throwable t: e)
    t.printStackTrace();
}
```

- How to connect to Oracle DB

```Java
Connection con = DriverManager.getConnection("jdbc:oracle:thin@localhost/dbname", username, password);
```

- How to load a MysQL driver prior to JDBC 4.0
This code goes before we call getConnection method.

```Java
try {
  Class.forName("com.mysql.jdbc.Driver");
} catch(ClassNotFoundException e) {
  e.printStackTrace();
}
```

- How to get results from database.

```Java
Statement statement = connection.createStatement();
ResultSet products = statement.executeQuery("SELECT * FROM Products");
```

- To loop through result set

```Java
while(products.next()) {

}
```

### Methods of ResultSet

- next() : moves the cursor to the next row in result set.
- last(): moves the cursor to the last row in the result set.
- close(): release the result set's resources.
- getRow(): returns an int value that identifies the current row of the result set.

- By default, createStatement method creates a forward-only, read-only result set. That means we can only move the cursor in forward direction and we can't update it. We can provide additional arguments to create other types of result sets.

- Once the cursor is positioned on the row, we can use following methods to retrieve each column values.

```Java
String code = products.getString(1);
String description = products.getString(2);
double price = products.getDouble(3);
```

```Java
String code = products.getString("ProductCode");    // column name ProductCode in database.
String description = products.getString("ProductDescription");
double price = products.getDouble("ProductPrice");
```

- Code that creates a Product object from result set.

```Java
Product product = new Product(products.getString(1),
                              products.getString(2),
                              products.getDouble(3));
```

- To insert, modify or delete a data, we use executeUpdate() method on statement. When modifying data, it is always best to use PreparedStatement to avoid SQL injection.

```Java
String query = "INSERT INTO Product(ProductCode, ProductDescription, ProductPrice) " +
      "VALUES ('" + product.getCode() + "', " +
      "'" + product.getDescirption()" + ", " +
      "'" + product.getPrice() + "')";
Statement statement = connection.createStatement();
int rowCount = statement.executeUpdate(query);
```

```Java
String query = "UPDATE Product SET " +
          "ProductCode='" + product.getCode() + "', " +
          "ProductDescription='" + product.getDescription() + "' " +
          "WHERE ProductCode='" + product.getCode() + "'";
Statement statement = connection.createStatement();
int rowCount = statement.executeUpdate(query);
```

```Java
String query = "DELETE FROM Product " +
            "WHERE ProductCode='" + productCode + "'";
Statement statement = connection.createStatement();
int rowCount = statement.executeUpdate(query);
```

 **PreparedStatement** improves performance and avoid SQL injection when we execute query on user input.

 - get a result SET

```Java
String prepareSQL = "SELECT ProductCode, ProductDescription, ProductPrice " +
      "FROM Product WHERE ProductCode= ?";
PreparedStatement ps = connection.prepareStatement(preparedSQL);
ps.setString(1, productCode);
ResultSet product = ps.executeQuery();
```

- modify a row

```Java
String preparedSQL = "UPDATE Product SET " +
                    "ProductCode=?, " +
                    "ProductDescription=?, " +
                    "ProductPrice=? " +
                    "WHERE ProductCode=?";
PreparedStatement ps = connection.prepareStatement(preparedSQL);
ps.setString(1, product.getCode());
ps.setString(2, product.getDescription());
ps.setString(3, produdct.getPrice());
ps.setString(4, product.getCode());
ps.executeUpdate();
```

- delete a row

```Java
String sql = "DELETE FROM Product WHERE ProductCode=?";
PreparedStatement ps = connection.prepareStatement(sql);
ps.setString(1, productCode);
ps.executeUpdate();
```

## Working with connection pool

Opening a connection to a database is a time-consuming process and can degrade application performance. So, usually a collection of Connection objects are created and stored in another object known as database connection pool. Typically, you create a single connection pool for a web app. Then, when user accesses a servlet, the servlet creates a thread. This thread gets a connection object from connection pool, uses it to access database, returns it to the connection pool.

Usually, these code is available from third party. For example, Tomcat includes connection pool in JAR file named tomcat-jdcp.jar in lib directory. Add the JAR file to application.

To customize connection pool, we can use context.xml file.

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<Context path="/projectpath">
  <Resource name="Jdbc/dbname" auth="Container"
            driverClassName="com.mysql.jdbc.Driver"
            url="jdbc:mysql://localhost:3306/dbname?autoReconnect=true"
            username="username" password="password"
            maxActive="100" maxIdle="30" maxWait="10000"
            logAbandoned="true" removeAbandoned="true"
            removeAbandonedTimeout="60" type="javax.sql.DataSource" />
</Context>
```

We can modify every attribute to suit our application. If web application doesn't close its ResultSet, Statement, Connection objects, connection might be abandoned. However, to return any abandoned connections to the pool, Resource element can be configured.

removeAbandoned to true,
removeAbandonedTimeout to 60 seconds. That way abandoned connections are returned to the pool after they have been idle for 60 seconds.
logAbandoned: allows to write code that caused the connection to be abandoned to log file.

Then we can create ConnectionPool class to easily work with Connection Pool

```Java
package com.piyushpatel2005;

import java.sql.*;
import javax.sql.DataSource;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class ConnectionPool {
  private static Connection pool = null;
  private static DataSource dataSource = null;

  private ConnectionPool() {
    try {
      InitialContext ic = new InitialContext();
      dataSource = (DataSource) ic.lookup("java:/comp/env/jdbc/dbname");
    } catch(NamingException e) {
      System.out.println(e);
    }
  }

  public static synchronized ConnectionPool getInstance() {
    if(pool == null) {
      pool = new ConnectionPool();
    }
    return pool;
  }

  public Connection getConnection() {
    try {
      return dataSource.getConnection();
    } catch(SQLEXception e) {
      System.out.println(e);
      return null;
    }
  }

  public void freeConnection(Connection c) {
    try {
      c.close();
    } catch (SQLException e) {
      System.out.println(e);
    }
  }
}
```

Now you can create connection easily.

```java
ConnectionPool pool = ConnectionPool.getInstance();
Connection connection = pool.getConnection();

... ... ...
pool.freeConnection(connection);
```

We can create UserDB class to insert, update, delete a User with these methods.

`public static int insert(User user) {}`

- We can also check if the email already exists in our data.

```java
public static boolean emailExists(String email) {
  ConnectionPool pool = ConnectionPool.getInstance();
  Connection connection = pool.getConnection();
  PreparedStateent ps = null;
  ResultSet rs = null;

  String query = "SELECT Email FROM User WHERE Email = ?";

  try {
    ps = connection.prepareStatement(query);
    ps.setString(1, email);
    rs = ps.executeQuery();
    return rs.next();
  } catch(SQLException e) {
    System.out.println(e);
    return false;
  }
  finally {
    DBUtil.closeResultSet(rs);
    DBUtil.closePreparedStatement(ps);
    pool.freeConnection(connection);
  }
}
```

We can also create DBUtil class to close connections as they require repeatitive statements.

```Java
public class DBUtil {
  public static void closeSatement(Statement s) {
    try {
      if(s != null) {
        s.close();
      }
    } catch(SQLException e) {
      System.out.println(e);
    }
  }

  // similar methods for closing prepared statement and result set.
}
```
