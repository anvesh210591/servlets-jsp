# Sessions and Cookies

## What is session?

Keeping track of users as they move around a site is known as session tracing. HTTP is a stateless protoco. For working with session, the user must have per-session cookies enabled in their browsers.

To get a session object, use getSession on request object. It will create or return an existing session object. You can use getAttribute and setAttribute on session objects and they return object so you need to cast appropriately. However, here the scope of the object is session. To remove an attribute, we can use removeAttribute method.

| Method | Description |
--------|:------------------:|
| getSession() | returns HTTP object associated with the  request. It there is no associated object, it will create one. |
| setAttribute(String name, Object o) | stores any object in session as an attribute and specifies a name for attribute |
| getAttribute(String name) | returns value of the specified attribute as an object. It no attribute exists, then returns null. |
| removeAttribute(String name) | removes the specified attribute from this session. |
| getAttributeNames() | applied on session object and returns enumeration with naes of all attributes. Then you can use hasMoreElements and nextElement methods to loop through the names. |
| getId() | returns the ID that servlet engine is using for the current session. |
| isNew() | to check if the client is new or if the client chooses to not join the session. Returns true if the client is accessing the site for the first time in a new session or if cookies have been disabled. |
| setMaxInactiveInterval(int seconds) | to set the maximum session interval. To create a session that won't be invalidated until the user closes the browser, supply -1 as a parameter. |
| invalidate() | to invalidate session object and unbinds any objects bound to it. |


Here, are some examples of these methods.

```Java
HttpSession session = request.getSession();
session.setAttribute("productCode" , productCode);

Cart cart = new Cart(productCode);
session.setAttribute("cart", cart);

String productCode = (String) session.getAttribute("productCode");

session.removeAttribute("productCode");
```


```Java
Enumeration names = session.getAttributeNames();
while(names.hasMoreElements()) {
  System.out.println((String) names.nextElement());
}

String jSessionId = session.getId();

session.setMaxInactiveInterval(60 * 60 * 24);   // one day

session.invalidate();
```

## Thread-safe access of session

Usually, for each request, container creates a separate thread. So, session is mostly thread-safe. But if the user tries to access the same page and set or get attribute using two tabs of the browser, then there might be problem of concurrency.

For synchronization, you can't synchronize session object like this because servlet specification does not guarantee that it will return the same session object every time.

```Java
synchronized(session) {
  session.setAttribute("cart", cart);
}
```

However, it guarantees that the session ID will always be the same, even if session object is different. So, we can lock object based on session ID.

```Java
Cart cart;
final Object lock = request.getSession().getId().intern();
synchronized(lock) {
  cart = (Cart) session.getAttribute("cart");
}
```

## What are cookies?

Cookie is a name/value pair. We can create our own cookie and once created we can include it in  the server's response to the browser. Browser stores the cookie on client machine and sends it back with subsequent requests. But you can't rely on that as they might be disabled.

Examples of cookies:

```
jsessionid=D1F3GH5H5KSDFI5JSDF46H
user_id=87
email=jsmith@hotmail.com
```

Browsers generally accept only 20 cooies from each site and total 300 cookies. They can limit each cookie to 4 KBs. A cookie can be associated with one or more subdomain names.

They can be used to allow users to skip login and registration forms, to customize pages, to focus advertising. A per-session cookie that holds session ID is automatically created for each session.

| Method | Description |
---------|:------------:|
| Cookie(String name, String value) | Constructor to create a cookie with specified name and value |
| setMaxAge(int seconds) | to persist cookie for specified time. To create per-session cookie, set the cookie's maximum age to -1. |
| setPath(String path) | To allow entire application to access the cookie set the path to "/". |
| getName() | returns a string for the nae of the cookie. |
| getValue() | returns string that contains the value of the cookid |
| addCookie(Cookie c) | adds specified cookie to response. |
| getCookies() | returns an array of cookie objects with request. If no cookies were sent, this method returns a null value. |

Code to add Cookie

```Java
Cookie c = new Cookie("userIdCookie", userId);
c.setMaxAge(60 * 60 * 24 * 365); // one year
c.setPath("/");
response.addCookie(c);
```

Code to get cookie from request

```Java
Cookie[] cookies = request.getCookies();
String cookieName = "userIdCookie";
String cookieValue = ""
for(Cookie cookie: cookies) {
  if(cookieName.equals(cookie.getName()))
    cookieValue = cookie.getValue();
}
```

To delete a cookie, we can setMaxAge to 0.

```java
Cookie[] cookies = request.getCookies();
for(Cookie cookie: cookies) {
  cookie.setMaxAge(0);
  cookie.setPath("/");
  response.addCookie(cookie);
}
```

By default, when a browser returns a cookie, it returns it to the directory that originally sent that cookie to the browser and all subdirectories of that directory. That's not what you want. That's why it is common to use the setPath method to set the path for the cookie to "/" so that entire application can access it.

| Method | Description |
-----------|:-----------:|
| setPath(String path) | To make the cookie available to a directory and its subdirectories, you can specify path like /cart. |
| setDomain(String domainPattern) | By default, browser only returns a cookie to the host that sent the cookie. To return a cookie to other hosts within the same domain, you can set a domain pattern like .google.com. Then it will be available to image.google.com and igoogle.google.com. |
| setSecure(boolean flag) | By default, cookies are sent over a regular connection or an encrypted connection. To protect cookies that store sensitive data, you can supply a true to this method. |
| setVersion(int version) | By default, Java creates cookies tha use version 0 of cookie protocol. |

- There are also getters for these methods.
