# How to create JSP Page

To display an attribute, you can use

`${ attribute }` in JSP file.

For example,
**Servlet code**

```Java
GregorianCalendar currentDate = new GregorianCalendar();
int currentYear = currentDate.get(Calendar.YEAR);
request.setAttribute("currentYear", currentYear);
```

**JSP code**

`<p>The current year is ${currentYear}</p>`

To display property of an attribute

`${atribute.property}`

Java searches following scopes in sequence.
page: the bean stored in implicit pageContext object.
request: the bean stored in HttpServletRequest object.
session: the bean stored in HttpSession object.
application: the bean stored in ServletContext object.

- Application scope attributes are not thread-safe.

## JSP Standard Tag Library

When coding a JSP, we may need to perform conditional processing to change the appearance of the page depending on the values of the attributes. We can use if tag.

Before we can use JSTL tags within an application, we must import jstl-impl.jar and jstl-api.jar in the application.

Before we use JSTL tags, we must code a taglib directive to specify the URI for JSTL core library.

**taglib directive**

```xml
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
```

**JSTL if tag use**

```jsp
<c:if test="${ message != null }">
  <p><i>${message}</i></p>
</c:if>
```

```jsp
<c:if test="${user.wantsUpdates == 'Yes'}">
  <p>This user wants updates</p>
</c:if>
```

## JSP tags
Five types:

| Tag      | Name          | Purpose |
-----------|:--------------|:-----------:|
| <%@ %>   | JSP directive | to set conditions that apply to the entire JSP |
| <% %>    | JSP Scriptlet | to insert a block of Java statements |
| <%= %>  | JSP expression | to display the string value of expression |

| <%-- --%> | JSP comment   | to tell the JSP engine to ignore code. |
| <%! %>  | JSP declaration | to declare instance variables and methods for a JSP |

You can use a directive to import classes in a JSP.

`<%@ page import="java.util.*, java.io.Exception" %>`

JSP tags that display or hide a message

```jsp
<%
  String message = (String) request.getAttribute("message");
  if(message != null) {
%>
  <p><i><%= message %></i></p>
<%
  }
%>
```

When you use HTML comment the Java code in the comment is compiled and sent to browser as a comment. However, when you use JSP comment, the code is not compiled.

```html
<!--
<p>This email address was added to our list on <%= new Date() %></p>
-->
```

```jsp
<%--
<p>This email address was added <%= new Date() %></p>
--%>
```

```jsp
  // get parametesr from the request
  String firstName = request.getParameter("firstName");
  String lastName = request.getParameter("lastName");

  /*
  User user = new User(firstName, lastName);
  UserDB.insert(user);
  */
%>
```

Three ways to access User bean

```jsp
<%@ page import="com.piyushpatel.User" %>
<%
  User user = (User) request.getAttribute("user");
  if(user == null) {
    user = new User();
  }
%>
<label>Email:</label>
<span><%= user.getEmail() %></span><br>
<label>First Name:</label>
<span><%= user.getFirstName() %></span><br>
```

The same code using standard JSP tags

```JSP
<jsp:useBean id="user" scope="request" class="com.piyushpatel.User" />
<label>Eamil:</label>
<span><jsp:getProperty name="user" property="email" /></span><br>
<label>First Name:</label>
<span><jsp:getProperty name="user" property="firstName" /></span><br>
```

The same code using EL

```jsp
<label>Email:</label>
<span>${user.email}</span><br>
<label>First Name:</label>
<span>${user.firstName}</span><br>
```

- **useBean** tag: It uses id attribute to specify the name that's used to access the bean. class attribute to identify the class and scope to specify the scope of the bean. Scope can be page, request, session and application. Scope is page by default.

useBean tag creates object using no-arg constructor.

Syntax:

```jsp
<jsp:useBean id="beanName" class="package.Class" scope="scope" />
```

- **getProperty** and **setPrperty** tags: These can be used to get and set values stored in the bean. Here, name attribute specifies the name of the bean. It should match the id attribute of useBean. property attribute specifies the name of the property that you want to access.

In setProperty tag, you also use value property to set the value. You cannot get property whose value is null or empty string and similarly cannot set property to null or empty string. However, you can use constructor to set these values.


Syntax:

```jsp
<jsp:getProperty name="beanName" property="propertyName" />
```

```jsp
<jsp:setProperty name="beanName" property="propertyName" value="value" />
```

You can include html or jsp code partials into another JSP file using **import**.

```jsp
<c:import url="/includes/header.html" />
```

## Three methods to include files in JSP

1. To include a file at compile-time, use include directive. When you include a file at compile-time, it becomes a part of generated servlet. So, response will be quicker.

```JSP
<%@ include file="/includes/header.html" %>
```

2. To include a file at runtime, code jsp:include tag. Use page attribute to set the relative path of the file to be included.

```jsp
<jsp:include page="/includes/footer.jsp" />
```

3. Alternative, file can be included at runtime using JSTL import. JSTL import allows you to include files from other applications.

<c:import url="/includes/header.html" />
<c:import url="www.google.com/inclues/footer.jsp" />
