# Working with Filters

You can code a filter class that examines an HTTP request and does some processing based on the values of HTTP request headers. It can intercept HTTP request and execute code before or after the requested servlet or JSP is executed. Ideal for cross-cutting concerns.

It allows you to create modular code that can be applied to different parts of an application. You can use web.xml file to control when filters are executed. This allows you to apply filters to different parts of an application, and to turn them on or off.

Logging, authentication, compression, image processing all these can be performed using filters.

To create a filter, we create a class that implements Filter interface and implement three methods init, doFilter and destroy.

```Java
package com.piyushpatel2005.filters;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class TestFilter implements Filter {
  private FilterConfig filterConfig = null;

  @Override
  poublic void init(FilterConfig filterConfig) {
    this.filterConfig = filterConfig;
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    HttpServletRequest httpRequest = (HttpServletRequest) request;
    HttpServletResponse httpResponse = (HttpServletResponse) response;
    ServletContext sc = filterConfig.getServletContext();

    String filterName = filterConfig.getFilterName();
    String servletPath = "Servlet Path: " + httpRequest.getServletPath();

    sc.log(filterName + " | " + servletPath + " | before request.");

    // pass control to next filter or servlet
    chain.doFilter(httpRequest, httpResponse);
    sc.log(filterName + " | " + servletPath + " | after request.");
  }

  @Override
  public void destroy() {
    filterConfig = null;
  }
}
```

Filter configuration in web.xml file

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<web-app version="3.0" xmlns="http://java.sun.com/xml/ns/javaee"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://java.sun.com/xml/ns/javaee
  http://java.sun.com/xml/ns/javaee/web-app_3_0.xsd">
  <filter>
    <filter-name>TestFilter1</filter-name>
    <filter-class>com.piyushpatel2005.filters.TestFilter1</filter-class>
  </filter>
  <filter>
    <filter-name>TestFilter2</filter-name>
    <filter-class>com.piyushpatel2005.filters.TestFilter2</filter-class>
  </filter>
  <filter>
    <filter-name>TestFilter3</filter-name>
    <filter-class>com.piyushpatel2005.filter.TestfTestFilter3</filter-class>
  </filter>

  <filter-mapping>
    <filter-name>TestFilter1</filter-name>
    <url-pattern>/*</url-pattern>
  </filter-mapping>
  <filter-mapping>
    <filter-name>TestFilter2</filter-name>
    <url-pattern>/*</url-pattern>
    <dispatcher>REQUEST</dispatcher>
    <dispatcher>FORWARD</dispatcher>
  </filter-mapping>
  <filter-mapping>
    <filter-name>TestFilter3</filter-name>
    <servlet-name>DownloadServlet</servlet-name>
  </filter-mapping>

  <servlet>
    <servlet-name>Downloadservlet</servlet-name>
    <servlet-class>com.piyushpatel2005.servlets.DownoadServlet</servlet-class>
  </servlet>
  <servlet-mapping>
    <servlet-name>DownloadServlet</servlet-name>
    <url-pattern>/download</url-pattern>
  </servlet-mapping>
  <session-config>
    <session-timeout>30</session-timeout>
  </session-config>

  <welcome-file-list>
    <welcome-file>index.jsp</welcome-file>
  </welcome-file-list>
</web-app>
```

TestServlet1 is executed for all requests to all urls.
TestServlet2 is executed for all requests that come from clients and also for all requests forwarded within application.
TestFilter3 is mapped to /download because it is executed when DownloadServlet is executed.

Filters are executed in the order they are specified.

For index.jsp

TestFilter1 -> TestFilter2 -> servlet for this index.jsp -> TestFilter2 -> TestFilter1

- dispatcher: specifies the types of requests that cause the filter to be executed. Valid values include REQUEST(requets from the client), FORWARD(forwards from the client), ERROR(when application uses an error handler) and INCLUDE(when application uses include).


## One side processing

Filters can also process only one side (request-side or response-side). It is executed before servlet execution and after serlvet executing but before sending response back. To do only request-side processing, you code for doFilter method before you call doFilter method of the FilterChain object.

**request-side processing**

```java
package com.piyushpatel2005.filters;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class LogRequestFilter implements Filter {
  private FilterConfig filterConfig = null;

  @Override
  public void init(FilterConfig filterConfig) {
    this.filterConfig = filterConfig;
  }

  @Override
  public void doFilter(
          ServletRequest request,
          ServletResponse response,
          FilterChain chain
  ) throws IOException, ServletException {
    HttpServletRequest httpRequest = (HttpServletRequest) request;
    ServletContext sc = filterConfig.getServletContext();

    String logString = filterConfig.getFilterName() + " | ";
    logString += "Servlet path: " + httpRequest.getServletPath() + " | ";

    Cookie[] cookies = httpRequest.getCookies();
    String emailAddress = util.CookieUtil.getCookieValue(cookies, "emailCookie");
    logString += "Email cookie: ";
    if(emailAddress.length() != 0)
      logString += emailAddress;
    else
      logString += "not found";

    sc.log(logString);

    chain.doFilter(httpRequest, response);
  }

  @Override
  public void destroy() {
    filterConfig = null;
  }
}
```

**response-side processing**

```java
public class LogResponseFilter implements Filter {
  private FilterConfig filterConfig = null;

  @Override
  public void init(FilterConfig filterConfig) {
    this.filterConfig = filterConfig;
  }

  @Override
  public void doFilter(
      ServletRequest request,
      ServletResponse response,
      FilterChain chain
  ) throws IOException, ServletException {
    chain.doFilter(request, response);

    HttpServletRequest httpRequest = (HttpServletRequest) request;
    HttpServletResponse httpResponse = (HttpServletResponse) response;
    ServletContext sc = filterConfig.getServletContext();

    String logString = filterConfig.getFilterName() + " | ";
    logString += "Servlet path: " + httpRequest.getservletPath() + " | ";
    logString += "Content Type: " + httpResponse.getContentType();

    sc.log(logString);
  }

  @Override
  public void destroy() {
    filterConfig = null;
  }
}
```

## Init parameters for filters

web.xml

```xml
<filter>
  <filter-name>TestInitParamsFilter</filter-name>
  <filter-class>filtesr.TestInitParamsFilter</filter-class>
  <init-param>
    <param-name>logFilename</param-name>
    <param-value>logfile.log</param-value>
  </init-param>
</filter>
```

How to read init Param

```java
String logFilename = filterConfig.getInitParameter("logFilename");
```

## Restricting certain IP addresses

```java
public class SecurityFilter implements Filter {
  private FilterConfig filterConfig = null;
  private String[] allowedHosts = null;

  @Override
  public vodi init(FilterConfig filterConfig) throws ServletException {
    this.filterConfig = filterConfig;
    String hostsString = filterConfig.getInitParameter("allowedHosts");
    if(hostsString != null && !hostsString.trim().equals("")) {
      allowedHosts = hostsString.split("\n");
    }
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    HttpServletRequest httpRequest = (HttpServletRequest) request;
    HttpServletResponse httpResponse = (HttpServletResponse) response;

    String remoteAddress = httpRequest.getRemoteAddr();
    boolean allowed = false;
    for(String host: allowedHosts) {
      if(host.trim().equals(remoteAddress)) {
        allowed = true;
        break;
      }
    }
    if(allowed) {
      chain.doFilter(request, response);
    } else {
      filterConfig.getServletContext().log("Attempted admin access from unauthorized IP: " + remoteAddress);
      httpResponse.sendError(404);
      chain.doFilter(request, response);
    }
  }

  @Override
  public void destroy () {
    filterConfig = null;
  }
}
```

web.xml

```xml
<filter>
  <filter-name>SecurityFilter</filter-name>
  <filter-class>com.piyushpatel2005.SecurityFilter</filter-class>
  <init-param>
    <param-name>allowedHosts</param-name>
    <param-value>
      0:0:0:0:0:0:0:1
      127.0.0.1
    </param-value>
  </init-param>
</filter>
<filter-mapping>
  <filter-name>SecurityFilter</filter-name>
  <url-pattern>/admin/*</url-pattern>
</filter-mapping>
```
