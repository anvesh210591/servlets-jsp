# Java Servlets and JSP

Web application components:
  - Clients (Web browser)
  - Server (web server)

## Basics of Web Interaction:

Web browser converts HTML language into web pages. It renders code in HTML into static web pages. Web server and browsers interact using HTTP protocol. It is a request response cycle. Like browser requests for a resource located on the web server and the web server responds with a response.

**Servlet/JSP**

lower level API, provides higher degree of control over HTML, CSS, JS returned to the browser.

**JSF**

higher level API, does more work for developer

**Spring Framework**

higher level API, but still gives more control over HTML, CSS and JS returned to the browser.

- Servlets store Java code that does server-side processing and JSP store HTML that defines user interface.

All servlet/JSP engines must implement servlet/JSP part of Java EE specification. This makes servlet/JSP code portable between servlet/JSP engines and application server. Tomcat includes web server and JSP engine. It should be able to access JDK to execute Java codes. A servlet/JSP engine is also known as servlet/JSP container.

## Table of Contents:

1. [Basics of Java Web Applications](introduction/directory_structure.md)
2. [MVC Patterns](mvc/mvc_patterns.md)
3. [How to create Servlets](servlets/create_servlet.md)
4. [JSP and Tag Library](jsp/jsp.md)
5. [Cookies and Session](sessions/cookies_and_session.md)
6. [JSP Expression Language](jsp/el.md)
7. [How to use JSTL](jsp/jstl.md)
8. [How to create Custom tags](jsp/create_custom_tags.md)
9. [How to use MySQL](databases/mysql.md)
10. [How to use JDBC](databases/jdbc.md)
11. [JPA to work with Database](databases/jpa.md)
12. [JavaMail API](advanced/javamail.md)
