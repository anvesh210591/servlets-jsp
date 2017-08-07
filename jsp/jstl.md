# JSP Standard Tag Library

There are five tag libraries in JSTL 1.2.

|Name |Prefix |URI              |Description |
------|:-------|:---------------|:-----------:|
|Core |c        |http://java.sun.com/jsp/jstl/core  |contains core tags for common tasks such as looping and if/else. |
|Formatting |fmt  |http://java.sun.com/jsp/jstl/fmt |Provides ags for formatting numbers, times and dates to work correctly with internationalization |
|SQL  |sql  |http://java.sun.com/jsp/jstl/sql |Provides tags for working with SQL queries and data srouces. |
|XML  |x    |http://java.sun.com/jsp/jstl/xml |tags for manipulating XML documents  |
|Functions  |fn   |http://java.sun.com/jsp/jstl/functions |functions that can be used to manipulate strings |


taglib directive to specify the JSTL core library

```jsp
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
```
- out

EL is vulnerable to XSS (Cross site scripting) attack. To prevent that, you can use out tag to escape the output for your application. It replaces (<) with corresponding HTML entities.

```jsp
<label>Email:</label>
<span><c:out value="${user.email}" /></span>
```

```jsp
<c:forEach var="cook" items="${cookie}">
  <tr>
    <td><c:out value="${cook.value.name}" /></td>
    <td><c:out value="${cook.value.value}" /></td>
  </tr>
</c:forEach>
```

How to display default value:

```jsp
<p><c:out value="${message}" default="No message" /></p>
```


```jsp
<p><c:out value="${message}">No message</c:out></p>
```

You can use single quotes or double quotes for attributes.

- forEach: to loop through any collection.

```jsp
<c:forEach var="item" items="${cart.items}">
  <tr>
    <td>${item.quantity}</td>
    <td>${item.product.description}</td>
    <td>${item.product.priceCurrencyFormat}</td>
    <td>${item.totalCurrencyFormat}</td>
  </tr>
</c:forEach>

-forTokens: to loop through items stored in string before being separated by one or more delimiters. You can nest forTokens in another forTokens or forEach.

```jsp
<p>Product Codes</p>
<ul>
  <c:forTokens var="productCode" items="${productCodes}" delims=",">
    <li>${productCode}</li>
  </c:forEach>
</ul>
```

Multiple delimiters

```jsp
<c:forTokens var="part" items="${emailAddress}" delims="@.">
  <li>${part}</li>
</c:forTokens>
```

Additionally, you can use begin to specify the first index for loop, end to identify last index, step to specify the amount of increment, varStatus to specify the name of the variable that can be used to get information about the status of the loop. This variable(varStatus) provides first, last, index and count properties. These attributes work with forEach and forTokens both.

```Java
int[] numbers = new int[30];
for(int i - 0; i < 30; i++) {
  numbers[i] = i + 1;
}
session.setAttribute("numbers", numbers);
```

```JSP
<p>Numbers</p>
<ul>
<c:forEach items="${numbers}" var="number" begin="0" end="9" step="1" varStatus="status">
  <li>${number} | First: ${status.first} |
      Last: ${status.last} | Index: ${status.index} |
      Count: ${status.count} </li>
</c:forEach>
</ul>
```

- if: used to conditionally display certain messages.

```jsp
<c:if test="${cart.count > 1}">
  <p>You have ${cart.count} items in your cart.</p>
</c:if>
```

- choose: similar to switch in Java. In this case, otherwise works like default. If all conditions fail, execute code in otherwise. It is also like if-else if-else.

```JSP
<c:choose>
  <c:when test="${cart.count == 0}">
    <p>Your cart is empty.</p>
  </c:when>
  <c:when test="${cart.count == 1}">
    <P>You have 1 item in your cart.</p>
  </c:when>
  <c:otherwise>
    <p>You have ${cart.count} items in your cart.</p>
  </c:otherwise>
</c:choose>
```

- url: allows you to specify URLs relative to the application context.

```jsp
<a href="<c:url value='/cart' />">Continue Shopping</a>
```

```jsp
<a href="<c:url value='/cart?productCode=8601' />">Add to Cart</a>
```

```jsp
<a href="<c:url value='/cart?productCode=${product.code}' />" >
  Add to cart
</a>
```

The same result can be obtained like this:

```jsp
<a href="<c:url value='/cart'>
  <c:param name='productCode' value='${product.code}' />
  </c:url>">
  Add to cart
</a>
```

URL tag encodes the session ID in URLs that returned to the client. This can result in session hijacking. To prevent it we can add following code in web.xml to change tracking mode to COOKIE.

```xml
<session-config>
  <session-timeout>30</session-timeout>
  <tracking-mode>COOKIE</tracking-mode>
</session-config>
```

- set: to set value in an attribute

```jsp
<c:set var="message" scope="session" value="Test message" />
```

To set a value in an attribute of a JavaBean.

```jsp
<c:set target="${user}" property="firstName" value="John" />
```

- remove: to remove an attribute

```jsp
<c:remove var="message" scope="session" />
```

- catch: to catch an exception

```jsp
<c:catch var="e">
  <% // this throws an exception
    int i = 1/0;
  %>
  <p>Result: <c:out value="${i}" /></p>
</c:catch>
<c:if test="${e != null}">
  <c:redirect url="/error_java.jsp" />
</c:if>
```
