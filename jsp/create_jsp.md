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

## JSP tags
Five types:
| Tag     | Name          | Purpose |
-----------|:--------------|:-----------:|
|<%@ %>   | JSP directive | to set conditions that apply to the entire JSP |
|<% %>    | JSP Scriptlet | to insert a block of Java statements |
| <%= %>  | JSP expression | to display the string value of expression |
| <%-- --%>| JSP comment   | to tell the JSP engine to ignore code. |
| <%! %>  | JSP declaration | to declare instance variables and methods for a JSP |
