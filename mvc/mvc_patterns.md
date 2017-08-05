# MVC Patterns for Servlet/JSP Applications

## Model 1 Pattern:

Here, JSP handles both request and response of the application. JSP does all the processing of the application. JSP interacts with Java classes and objects that represent the data of the business objects in the processing. Processing and presentation both are handled by JSPs. It is not recommended.

## Model 2 (MVC) Pattern:
Git

- separates the code for application into three layers: model, view and controller.
- Model defines business layer, implemented by JavaBeans
- View defines presentation, uses HTML or JSP to present the view to the browser.
- Controller manages the flow of application.

- WEB-INF folder  contains Deployment descriptor. It describes how the web application shoudl be configured on the server. That's why it's given the name.

## Deployment descriptor (web.xml)

The following code shows simple web.xml content.

```xml
<?xml version= 11 l.0 11 encoding=''UTF-8 11 ?>
<web-app version="3.1"
          xmlns="http://xmlns.jcp.org/xml/ns/javaee"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee
                          http://xmlns.jcp.org/xml/ns/javaee/web-app_3_1.xsd">
  <servlet>
    <servlet-name>Servlet1</servlet-name>
    <servlet-class>com.piyushpatel2005.Servlet1</servlet-class>
  </servlet>
  <servlet-mapping>
    <servlet-name>Servlet1</servlet-name>
    <url-pattern>/servlet1</url-pattern>
  </servlet-mapping>

  <session-config>
    <session-timeout>30</session-timeout>
  </session-config>

  <welcome-file-list>
    <welcome-file>index.html</welcome-file>
    <welcome-file>index.jsp</welcome-file>
  </welcome-file-list>
</web-app>
```

- When servlet controls the flow of the application. It is known as a controller.
- Servlet provides a request object that can be used to get data from HTTP request.
- response object can be used to send HTTP response. Usually, you forward request and response to JSP page.

## Java Beans

Java Beans follow three rules.
1. It must contain a no-arg constructor.
2. It must contain set and get methods for all the properties that need to be accessed by JSPs. For boolean properties get method is replaced by is method. `isEqual`.
3. It must implement the Serializable or Externalizable interface.

Java Beans are type of plain old Java object (POJO).
