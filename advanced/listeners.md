# Working with Listeners

You can create listener class that contains code that's executed every time a user starts a new session or when your web application starts. It's a class that listens for various events that can occur during the lifecycle of a web application and provides methods that are executed when those events occur. This lets you initialize the global variables before the first JSP or servlet of an application is requested.

To code a listener, we need to implement listener interface and implement two methods: contextInitialized and contextDestroyed. These are executed when the context is initialized (when application starts) and the later is executed when the context is destroyed. So, clean up code goes in this method.

```java
package com.piyushptel.util;

import javax.servlet.*;
import java.util.*;

import com.piyushpatel.business.*;
import com.piyushpatel.data.*;

public class CartContextListener implements ServletContextListener {
  public void contextInitialized(ServletContextEvent event) {
    ServletContext sc = event.getServletContext();

    // initialize the customer service email
    String custServEmail = sc.getInitParameter("custServEmail");
    sc.setAttribute("custServEmail", custServEmail);

    String productsPath = sc.getRealPath("/WEB-INF/products.txt");
    sc.setAttribute("productsPath", productsPath);

    ArrayList<Product> products = ProductIO.getProducts(productsPath);
    sc.setAttribute("products", products);
  }

  public void contextDestroyed(ServletContextEvent event) {
    // clean up code.
  }
}
```

To register the listener with the web application, we need listener element in web.xml file.

web.xml

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<web-app version="3.0"
        xmlns="http://java.sun.com/xml/ns/javaee"
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xsi:schemaLocation="http://java.sun.com/xml/ns/javaee
          http://java.sun.com/xml/ns/javaee/web-app_3_0.xsd">
  <servlet>
    <servlet-name>CartServlet</servlet-name>
    <servlet-class>com.piyushpatel.cart.CartServlet</servlet-class>
  </servlet>
  <servlet-mapping>
    <servlet-name>CartServlet</servlet-name>
    <url-pattern>/cart</url-pattern>
  </servlet-mapping>

  <!-- register listener class -->
  <listener>
    <listener-class>com.piyushpatel.util.CartContextListener</listener-class>
  </listener>

  <context-param>
    <param-name>custServEmail</param-name>
    <param-value>cust@eemail.com</param-value>
  </context-param>

  <welcome-file-list>
    <welcome-file>index.jsp</welcome-file>
  </welcome-file-list>
</web-app>
```

JSP that uses attributes set by a listener

```jsp
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <title>JSP Servlets with Listener</title>
</head>
<body>
  <h1>CD list</h1>
  <table>
    <tr>
      <th>Description</th>
      <th>Price</th>
      <th>&nbsp;</th>
    </tr>
    <c:forEach var="product" items="${products}">
    <tr>
      <td><c:out value="${product.description}" /></td>
      <td>${product.priceCurrencyFormat}</td>
      <td><form action="cart" method="post">
        <input type="hidden" name="productCode" value="${product.code}">
        <input type="submit" value="Add to Cart">
        </form>
      </td>
    </tr>
    </c:forEach>
  </table>
  <p>For customer service, please contact at ${custServEmail}.</p>

</body>
</html>
```

HttpSessionListener works like a ServletContextListener. It lets you write code that's executed when a new session is created or destroyed.

- ServletContextListener - methods that are executed when the servlet context is initialized and destroyed. (when the application is started and destroyed)
- ServletContextAttributeListener - provides methods that are executed when attributes are added to, removed from, or replaced in the servlet context object.
- HttpSessionListener - provides methods that are executed when the session object is created and destroyed for a user. This happens every time a new user accesses an application and when the session for a user is destroyed.
- HttpSessionAttributeListener - provides methods that are executed when attributes are added to, removed from, or replaced in the session object.
- HttpSessionBindingListener - provides methods that are executed when an object is bound to or unbound from the session.
- HttpSessionActivationListener - provides methods that are executed when the session is activated and deactivated. This happens when the session is migrating to another JVM.
- ServletRequestListener - provides methods that are executed when a request object is initialized and destroyed. This happens every time the server receives and processes a request.
- ServletRequestAttributeListener - provides methods that are executed when attributes are added to, removed from or replaced in request object.

```java
// ServletContextListener interface
contextInitialized(ServletContextEvent e)
contextDestroyed(ServletContextEvent e)

// ServletContextAttributeListener interface
attributeAdded(ServletContextAttributeEvent e)
attributeRemoved(ServletContextAttributeEvent e)
attributeReplaced(ServletContextAttributeEvent e)

// HttpSessionListener interface
sessionCreated(HttpSessionEvent e)
sessionDestroyed(HttpSessionEvent e)

// HttpSessionAttributeListener interface
attributeAdded(HttpSessionBindinEvent e)
attributeRemoved(HttpSessionBindingEvent e)
attributeReplaced(HttpSessionBindingEvent e)

// HttpSessionBindingListener interface
valueBound(HttpSessionBindingEvent e)
valueUnbound(HttpSessionBindingEvent e)

// HttpSessionActivationListener interface
sessionDidActivate(HttpSessionEvent e)
sessionWillPassivate(HttpSessionEvent e)

// ServletRequestListener interface
requestInitialized(ServletRequestEvent e)
requestDestroyed(ServletRequestEvent e)

// servletRequestAttributeListener interface
attributeAdded(ServletRequestAttributeEvent e)
attributeRemoved(ServletRequestAttributeEvent e)
attributeReplaced(ServletRequestAttributeEvent e)
```
