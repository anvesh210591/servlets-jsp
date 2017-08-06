# How to develop Servlet Code

## Servlet class

In practice, servlets almost always extend HttpServlet class.

- doGet method is used to handle GET request for the servlet.
- doPost method is used to handle POST request for the servlet.
- Both  methods receive request and respons eobject to get request parameters and send response back to the client.
- It is necessary to set ContentType before getting PrintWriter object from the response in order to get correct PrintWriter object for a given content type.

```Java
public class TestServlet extends HttpServlet {

  protected void doPost(HttpServletRequest request, HttpServletResponse response) {
    doGet(request, response);
  }

  protected void doGet(HttpServletRequest request, HttpServletResponse response) {
    response.setContentType("text/html");
    PrintWriter out = response.getWriter();
    try {
      out.println("<h1>HTML form</h1>");
    }
    catch(Exception e) {
      // handle exception
    }
    finally {
      out.close();
    }
  }
}
```

## How to map a servlet in web.xml

- servlet-class specifies the class for the servlet.
- servlet-name specifies unique name for the servlet.
- url-pattern specifies URL or URLs that are mapped to specified servlet. url-pattern must start with forward slash(/).
- As of Servlet 3.0, @WebServlet annotation can be used to map servlet to specific url-pattern.

**Example mapping in web.xml**

```xml
<servlet>
  <servlet-name>EmailListServlet</servlet-name>
  <servlet-class>com.piyushpatel2005.EmailListServlet</servlet-class>
</servlet>
<servlet>
  <servlet-name>TestServlet</servlet-name>
  <servlet-class>com.piyushpatel2005.TestServlet</servlet-class>
</servlet>
<servlet-mapping>
  <servlet-name>EmailListServlet</servlet-name>
  <url-pattern>/emailList</url-pattern>
</servlet-mapping>
<servlet-mapping>
  <servlet-name>TestServlet</servlet-name>
  <url-pattern>/email/*</url-pattern>
</servlet-mapping>
```

## Mapping using Annotation

```Java
package com.piyushpatel;

@WebServlet("/test")
public class TestServlet extends HttpServlet {
  ... ... ...
}
```

```Java
@WebServlet(urlPatterns={"/emailList", "/email/*"})
```

```Java
@WebServlet(name="EmailServlet", urlPatterns={"/test"})
```

**Note:** If you use both, web.xml and annotations to map the same servlet name, the mapping in web.xml overrides the mapping in the annotation.

## Methods for working with data

To get different parameters from the form, you can use getParameter method of request object.
If there are more than one values, you can use getParameterValues() method.

You can get servlet context from servlet using getServletContext() and can use getRealPath(relative path) to get real path for a file.

To set an attribute on request object, use setAttribute("attribute name", Object o).

When you work with request attributes, the attributes are reset between request. For example, if you set an attribute and forward the request to JSP. That attribute is availabe here, but after that request, the same attribute is not available in the same session.

## How to forward requests

To forward request, we use request dispatcher.

```Java
String url = "index.jsp";
this.getServletContext().getRequestDispatcher(url).forward(request, response);
```

Forward a request to a servlet

```Java
String url = "/cart/displayInvoice";
this.getServletContext().getRequestDispatcher().forward(request, response);
```

- Response can be redirected using sendRedirect() method of response object. that doesn't forward request and response objects. So, it's useful only for external redirects.

```Java
response.sendRedirect("http://www.google.com");
```

## Additional elements for initialization parameters

- context-param: defines parameter that's available to all servlets within an application. context-param comes under web-app element.
- init-param: defines name/value pair for an initialization parameter for a servlet. init-param comes under servlet element.
- param-name: defines the name of a parameter.
- param-value: defines the values of a parameter.

Using annotations, you can define initialization parameters for a servlet.

```Java
@WebServlet(urlPatterns={"/emailList"}, initParams={@InitParam(name="relativePath", value="/WEB-INF/Email.txt")})

```

To retrieve initialization parameters that are available to all servlets, you get servlet context and then call `getInitParameter` method.

To retrieve initialization parameters that's only available to current servlet, you `getServletConfig` to get ServletConfig object and then call `getInitParameter()` method.

**Code to get an initialization parameter available to all servlets**

```Java
String custServEmail = this.getServletContext().getInitParameter("custServEmail");
```

**Code to get initialization parameter for a servlet**

```Java
String relativePath = this.getServletConfig().getInitParameter("relativePath");
```

## Implement Custom Error Page

We can specify custom error page using error-page element.

- error-page: specifies an HTML page or JSP that's displayed when app encounters an uncaught exception or certain type of status code.
- error-code: specifies the number of valid HTTP status code
- exception-type: uses the fully qualified class name to specify a Java exception.
- location: specifies the location of the HTML pageor JSP that's displayed. The location element must start with forward slash.

**XML tags to provide error-handling for 404 status**

```xml
<error-page>
  <error-code>404</error-code>
  <location>/error_404.jsp</location>
</error-page>
```

**XML tags to provide error handling for all Java execptions**

```xml
<error-page>
  <exception-type>java.lang.Throwable</exception-type>
  <location>/error_java.jsp</location>
</error-page>
```

You can get exception information from `pageContext.exception["class"]` and `pageContext.exception.message`.

## Servlet life cycle

Servlet engine creates only one instance of a servlet. This usually occurs when the servlet engine starts or when the servlet is first requested. When servlet engine creates the instance of the servlet, it calls the `init` method. So, we can override it in servlet to supply any initialization code.

After creation of servlet, each request for servlet starts a thread that calls `service` method. This method checks the method that's specified in HTTP request and calls appropriate `doGet` or `doPost` method. We shouldn't override service method, but should override doGet or doPost method.

If servlet has been idle for some time or if the servlet engine is shut down, the servlet engine unloads the instances of servlets that it has created. Before unloading, it  calls `destroy` method of the servlet. So, cleanup code can be written in this method. destroy method is not  called if the server crashes.

An instance variable of a servlet belongs to one instance of the servlet and is shared by any threads that request the servlet. Instance variables created in init method are not thread-safe. That's why you should not use instance variables.

## Write debugging information to a file

To write data to a log file, we can use log methods of HttpServlet class.

log(String messgae) : writes the specified message to the servers's log file.
loc(String message, Throwable e): writes the specified message to server's log file followed by the stack trace of the exception.
