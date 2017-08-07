# JSP Expression Language (EL)

- compact and element
- makes code easier to read and write
- makes it easy to access nested properties.

```jsp
${item.product.code}
```

- EL returns empty string instead of null for String.
- lets you work with HTTP headers, cookies, context.
- EL doesn't create a JavaBean if it doesn't exist already.
- EL doesn't provide a way to set properties.

Standard JSP:

```jsp
<jsp:useBEan id="user" scope="session" class="com.piyushpatel.User" />
<label>Email:</label>
<span><jsp:getProperty name="user" property="email" /></span>
```

EL:

```jsp
<label>Email:</label>
<span>${user.email}</span>
```

Usually, EL looks up the attribute starting with the smallest scope (page) to the largest scope (application scope).

However, you can be specific about the scope as follows:

Servlet Code:

```Java
Date currentDate = new Date();
request.setAttribute("currentDate", currentDate);
```

JSP:

```jsp
<p>The current date is ${requestScope.currentDate}</p>
```

```jsp
<p>Hello, ${sessionScope.user.firstName}</p>
```


```Java
User user = new User("John", "Smith", "jsmith@gmail.com");
session.setAttribute("user", user);
```

```jsp
<p>Hello ${user["firstName"]}</p>
```

- EL allows to work with arrays

```Java
String[] colors = {"Red", "Green", "Blue"};
ServletContext application = this.getServletContext();
application.setAttribute("colors", colors);
```

```jsp
<p>The first color is ${colors[0]}</p>
<p>The second color is ${colors[1]}</p>
```

- EL allows to work with arraylist.

```Java
ArrayList<User> users = getUsers(path);
session.setAttribute("users", users);
```

```jsp
<p>The first address on our list is ${users[0].email}</p>
```

- With EL we can access nested attributes using dot syntax

```Java
Product p = new Product();
p.setCode("pf01");
LineItem lineItem = new LineItem(p, 10);
session.setAttribute("item", lineItem);
```

```jsp
<p>Product Code: ${item.product.code}</p>
```

OR

```jsp
<p>Product Code: ${item["product"].code}</p>
```

- EL works with Maps

For example, servlet code:

```Java
// Maps email and user. User contains firstName and lastName.
HashMap<String, User> usersMap = getUsersMap(path);
session.setAttribute("usersMap", usersMap);

String email = request.getParameter("email"); // get supplied email
session.setAttribute("email", email);
```

```JSP
<p>First name: ${usersMap[email].firstName}</p>
```

In above code, email is replaced by the supplied email and then `usersMap[email]` returns that user and then we get the firstName.

If you add double quotes around email, it will not work `${usersMap["email"].firstName}`.

```Java
HashMap<String, User> usersMap = getUsersMap(path);
session.setAttribute("usersMap", usersMap);

String[] emails = {"jsmith@gmail.com", "joie@yahoo.com"};
session.setAttribute("emails", emails);
```

```jsp
<p>First Name: ${usersMap[emails[0]].firstName}</p>
```

Here are some of the implicit objects that you can access in JSP.
param, paramValues, header, headerValues, cookie, initParam, pageContext.

```html
<form action="emailList" method="post">
  <p>First Name: <input type="text" name="firstName"></p>
  <p>Email 1: <input type="email" name="email"></p>
  <p>Email 2: <input type="email" name="email"></p>
</form>
```

```JSP
<p>First Name: ${param.firstName}</p>
<p>Email 1: ${paramValues.email[0]}</p>
<p>Email 2: ${paramValues.email[1]}</p>
```

```jsp
<p>MIME types: ${header.accept}<br>
  Compression types: ${header["accept-encoding"]}
</p>
```

Working with cookies in JSP using EL

```Java
Cookie c = new Cookie("emailCookie", email);
c.setMaxAge(60 * 60);
c.setPath("/");
response.addCookie(c);
```

```jsp
<p>The email cookie: ${cookie.emailCookie.value}</p>
```

How to get context init parameters

```jsp
<p>The context init Param: ${initParam.custEmail}</p>
```

pageContext object:

```jsp
<p>HTTP request method: ${pageContext.request.method}<br>
  HTTP response type: ${pageContext.response.contentType}<br>
  HTTP session ID: ${pageContext.session.id}<br>
  HTTP contextPath: ${pageContext.servletContext.contextPath}<br>
</p>
```

## Additonal Operators in EL

Operator | Alternative | Description |
--------|:-------------|:------------:|
|+      |               |Addition     |
|-      |               |Subtraction  |
|*      |               |Multiplication |
|/      |div            |Division     |
|%      |med            |Modulus      |
|==     |eq             |Equal to     |
|!=     |ne             |Not equal to |
|<      |lt             |less than    |
|>      |gt             |greater than |
|<=     |le             |less than or equal to |
|>=     |ge             |greater than or equal to |
|&&     |and            |logical and  |
|||     |or             |logical or   |
|!      |not            |negation     |
|empty  |               |is empty? returns true if the value is null or equal to empty string     |

```jsp
${"s1" == "s1"}
${1 != 2}
${3 lt 4}
${1 div 5}
```

```jsp
${empty firstName}
${true ? "s1" : "s2"}
```

To disable EL, you can use page directive to JSP page and set isELIgnored attribute to true. To disable for multiple pages, we can add jsp-config tag in web.xml

```jsp
<%@ page isELIgnored="true" %>
```

```xml
<jsp-config>
  <jsp-property-group>
    <url-pattern>*.jsp</url-pattern>
    <el-ignored>true</el-ignored>
  </jsp-property-group>
</jsp-config>
```

To disable scripting, edit web.xml

```xml
<jsp-config>
  <jsp-property-group>
    <url-pattern>*.jsp</url-pattern>
    <scripting-invalid>true</scripting-invalid>
  </jsp-property-group>
</jsp-config>
```
