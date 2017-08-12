# Restricting access to web resources by authentication

Tomcat provide a built-in way to restrict access to certain parts of a web application. This is known as container-managed security or container managed authentication.

To restrict access to a resource, a security constraint must be coded in the web.xml file.

```xml
<security-constraint>
  <web-resource-collection>
    <web-resource-name>Protected area</web-resource-name>
    <url-pattern>/download/*</url-pattern>
  </web-resource-collection>
  <auth-constraint>
    <role-name>programmer</role-name>
    <role-name>developer</role-name>
  </auth-constraint>
</security-contraint>

<login-config>
  <auth-method>BASIC</auth-method>
  <realm-name>Admin Login</realm-name>
</login-config>
```

This restricts access to all files within download foler. Only programmer and developer can access the contents in this folder. They will ask for username and password. If it matches, then they are granted access. Users with programmer and developer roles can access the resource. This is known as authorization.

There are three forms of authentication.

1. Basic authentication - causes browser to display a dialog box. It doesn't encrypt the username and password before sending them to the server.
2. Digest authentication - causes browser to display a diglog box and encrypts the username and password before sending them to the server.
3. Form-based authentication - allows the developer to code a login form that gets the username and password. It doesn't encrypt the username and password before sending them to the server.

Before you restrict access to web resource, you want to add one or more security-role elements. These are roles that are allowed to access restricted web resources.

You can also restrict access to one of the methods using http-method element. It's not advisable.
To make the restricted data transported between client and the server over a secure connection, we can use transport-guarantee element with CONFIDENTIAL value.

web.xml

```xml
<security-role>
  <description>Customer service department</description>
  <role-name>service</role-name>
</security-role>
<security-role>
  <description>programmers</description>
  <role-name>programmer</role-name>
</security-role>

<security-constraint>
  <web-resource-collection>
    <web-resource-name>Protected access</web-resource-name>
    <url-pattern>/download/*</url-pattern>
    <!-- Not secure - don't include http-method elements
    <http-method>GET</http-method>-->
  </web-resource-collection>

  <auth-constraint>
    <role-name>programmer</role-name>
    <role-name>service</role-name>
  </auth-constraint>

  <user-data-constraint>
    <transport-guarantee>CONFIDENTIAL</transport-guarantee>
  </user-data-constraint>
</security-constraint>
```

|Element | Description |
---------|:-----------:|
|<security-role> | Creates a security role for one or more web resources |
|role-name> | specifies name for security role |
|<security-constraint> | creates security constraint for web resources |
|<web-resource-collection> | specifies collection of restricted web resources. |
|<url-pattern> | specifies URL patetrn that you wish to restrict |
|<http-method> | HTTP methods that require authentication. |
|<auth-constraint> | security roles that are permitted t oaccess restricted content. |
|<user-data-constraint> | species constraints that apply to the data that's stored within restricted resources. |
|<transport-guarantee> | guarantees a secure connection when set to a value of CONFIDENTIAL |

When Tomcat manages security, you need to determine what type of security realm you want to implement.

## UserDatabaseRealm

Tomcat stores usernames, passwords and roles in tomcat-users.xml file in conf directory.

server.xml

```xml
<Realm className="org.apache.catalina.realm.UserDatabaseRealm" resourceName="UserDatabase" />
... ... ...
... ... ...
<Resource name="UserDatabase" auth="Constraint"
          type="org.apache.catalina.UserDatabase"
          description="User database that can be updated and saved"
          factory="org.apache.catalina.users.MemoryUserDatabaseFactory"
          pathname="conf/tomcat-users.xml" />
```

tomcat-users.xml

```xml
<?xml version='1.0' encoding="utf-8" ?>
<tomcat-users>
  <role rolename="manager" />
  <role rolename="programmer" />
  <role rolename="service" />
  <user username="admin" password="admin" roles="manager" />
  <user username="joe" password="doe" roles="programmer, service" />
</tomcat-users>
```

Here only admin and joe can access the resources.

## JDBCRealm

Serious applications use JDBC Realm. You need to create two tables for this though. All the configuration is saved in context.xml for this application only so that all the applications on server does not implement this realm but only this application uses this JDBC realm.

context.xml

```xml
<?xml version="1.0" encoding="utf-8" ?>
<Context path="/appname">
  <Realm className="org.apache.catalina.realm.JDBCRealm" debug="99"
      driverName="com.mysql.jdbc.Driver"
      connectionURL="jdbc:mysql://localhost:3306/dbname"
      connectionName="root" connectionPassword="password"
      userTable="UserPass" userNameCol="Username" userCredCol="Password"
      userRoleTable="UserRole" roleNameCol="RoleName" />
</Context>
```

|Attribute | Description |
-------------|:---------:|
|className | full classname for JDBCRealm class |
|driverName | full JDBC driver |
|connectionURL | database URL |
|connectionName | username for connection |
|connectionPassword | password for the connection |
|userTable | name of the table that contains usernames and passwords using userNameCol and userCredCol attributes. |
|userNameCol | specifies column name that contains usernames |
|userCredCol | contains column that contains passwords |
|  userRoleTable | name of table that contains user roles. |
|roleNameCol | name of the column that contains user roles. |

```sql
CREATE TABLE UserPass (
  Username VARCHAR(15) NOT NULL PRIMARY KEY,
  Password VARCHAR(15) NOT NULL
);

CREATE TABLE UseRRole (
  Username VARCHAR(15) NOT NULL,
  Rolename VARCHAR(15) NOT NULL,
  PRIMARY KEY (Username, Rolename)
);
```

## DataSourceRealm

For connection pooling data source realm is more feasible. IT allows to use connection pooling.

```xml
<!-- context.xml -->
<?xml version="1.0" encoding="utf-8" ?>
<Context path="/appname">
  <Resource name="jdbc/dbname" auth="Container"
            maxActive="100" maxIdle="30" maxWait="10000"
            driverClassName="com.mysql.jdbc.Driver"
            url="jdbc:mysql://localhost:3306/dbname?autoReconnect=true"
            logAbandoned="true" removeAbandoned="true"
            removeAbandonedTimeout="60" type="javax.sql.DataSource" />
  <Realm className="org.apache.catalina.realm.DataSourceRealm" debug="99"
          dataSourceName="jdbc/dbname" localDataSource="true"
          userTable="UserPass" userNameCol="Username" userCredCol="Password"
          userRoleTable="UserRole" roleNameCol="Rolename" />
</Context>
```

|Attribute | Description |
---------|:-------------:|
|className | full name of the DataSourceRealm class |
|dataSourceName | name that specifies the data source. If Realm is in the same file as Resource element, it can be specified the same name as the Resource element name. |
|localDataSource | By default, set to false. If Realm element is coded in the same context.xml as Resource element, you can set this to true to specify a local data source. |

# Authorization : How to allow access to authorized users.

## Basic authentication

When user tries to access restricted resource, they are asked for username and password. When user enters those credentials, security realm checks whether the usernam and password are valid, and also whether the user is associated with a role that is authorized to access the resource. If so, user is allowed to access.

For this, you need login-config element in web.xml. When you add XML tags for login-config in web.xml, they must immediately follow the security-constraint element that they relate to.

```xml
<login-config>
  <auth-method>BASIC</auth-method>
  <realm-name>Admin</realm-name>
</login-config>
```

## Digest authentication

To switch to digest authentication, change BASIC to DIGEST in auth-method element. Then, username and password are encrypted.

## Form based authentication

It works similar to basic authentication, but lets you use HTML, CSS to style your forms.

web.xml

```xml
<login-config>
  <auth-method>FORM</auth-method>
  <form-login-config>
    <form-login-page>/admin/login.html</form-login-page>
    <form-error-page>/admin/login_error.html</form-error-page>
  </form-login-config>
</login-config>
```

form-login-config: specifies the login and error pages that should be used for form-based authentication.

form-login-page: specifies the location of the login page. This page can be HTML, JSP or servlet.

form-error-page: the location of the page that should be displayed when invalid usernamd and/or password is entered.

Sample login page:

form action must be set to `j_security_check` and username must be `j_username` and password name must be `j_password`.

```html
<html>
  <head>
    <title>Login Page</title>
    <link href="styles/main.css" rel="stylesheet" type="text/css">
  </head>
  <body>
    <h1> Login </h1>
    <p>Please enter your username and password to continue.</p>
    <form action="j_security_check" method="get">
      <label>Username: </label>
      <input type="text" name="j_username">
      <label>Password: </label>
      <input type="password" name="j_password">
      <input type="submit" value="Login">
    </form>
  </body>
</html>
```
