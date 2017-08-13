# Working with HTTP requests and responses

**MIME types:**

text/plain
text/html
text/css
text/xml
text/csv
text/tab-separated-values
image/gif
image/jpeg
image/png
image/tiff
image/x-xbitmap: Windows bitmap image
application/atom-xml
application/rss-xml
application/pdf
applicaiton/postscript
application/zip
application/gzip
application/octet-stream
application/msword
application/vnd.ms-excel
audio/x-midi
audio/mpeg
audio/vnd.wav
video/mpeg
video/x-flv

## HTTP request headers

- accept: specifies preferred order of MIME types that the browser can accept. The '*/*' type indicates that the browser can handle any MIME type.
- accept-charset: the character sets that the browser can accept.
- accept-encoding: types of compression encoding that the browser can accept.
- accept-language: standard language codes for the languages that the browser prefers. For English, it is 'en' or 'en-us'.
- authorization: Identifies authorization level for the browser.
- connection: type of connection that's being used by the browser.
- cookie: specifies any cookies that were previously sent by the current server.
- host: specifies host and port of the machine that originally sent the request.
- pragma: a value of 'no-cache' indicates to browsers, proxy servers, and gateways that this document should not be cached.
- referer: indicates the URL of the referring web page.
- user-agent: type of browser.

## HTTP response headers

- cache-control: controls when and how browser caches a page.
- content-disposition: specifies that the response includes an attached binary file.
- content-length: specifies length of the body of the response in bytes.
- content-type: specifies the MIME type of the response document.
- content-encoding: specifies the type of encoding that response uses.
- expires: specifies the time that the page no longer be cached.
- last-modified: specifies the time when the document was last modified.
- location: works with status codes in the 300s to specify the new location of the document.
- pragma: turns off caching for older browsers when it is set to a value of 'no-cache'
- refresh: specifies number of seconds before the browser should ask for an updated page.
- www-authenticate: works with 401(unauthorized) status code to specify the authentication type and realm.

### Methods with request

getContentType() - returns MIME type of the body of the request.

getContentLength() - returns int value for the number of bytes in the request body that are made available by the input stream. It length unknown, it returns -1

getCookies() - returns array of Cookie objects.

getAuthType() - returns the authentication type that's being used by the server.

getRemoteUser() - returns the username of the user making this request, if user has been authenticated. If not authenticated, returns null.

```Java
String language = request.getHeader("accept-language");
if(language.startsWith("es"))
  jsp = "index.spanish.jsp";
else
  jsp = "index.english.jsp";
```

```Java
String browser = request.getHeader("user-agent");
if(browser.indexOf("MSIE") > -1)
  doIECode();
else
  doGenericCode();
```

Get all request headers

```Java
Enumeration<String> headerNames = request.getHeaderNames();
Map<String, String> headers = new HashMap<String, String> ();

while(headerNames.hasMoreElements()) {
  String headerName = headerNames.nextElement();
  headers.put(headerName, request.getHeader(headerName));
}
request.setAttribute("headers", headers);
```

```jsp
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<h1>Request Headers</h1>
<table>
  <tr>
    <th>Name</th>
    <th>Value</th>
  </tr>
  <c:forEach var="headers" items="${headers}">
  <tr>
    <td><c:out value="${headers.key}" /></td>
    <td><c:out value="${headers.value}" /></td>
  </tr>
  </c:forEach>
</table>
```

## Methods with response object

- setStatus(int code) - sets the status code for the response
- setHeader(String name, String value) - sets a reesponse header with specified name and value
- setIntHeader(String name, int value) - sets a response header with the specified name and int value.
- setDateHeader(String name, long value) - sets a response header with specified name and value.
- setContentType(String mimeType) - sets the MIME type of response sent to the client
- setContentLength(int lengthInBytes) - sets the length of the content body in Content-Length header.
- addCookie(Cookie cookie) - Adds the specified cookie to the response.

```java
response.setContentType("text/html");
response.setContentLength(403);
response.setHeader("pragma", "no-cache");
response.setIntHeader("refresh", 60);
response.setDateHeader("expires", currentDate.getTime() + 60 * 60);
```

## How to create virtual pages

These are pages that are generated from template using data from the database.

Servlet mapping:

`/users/*`

Possible URLs

`
/users/jane_doe
/users/john_smith
`

Servlet code

```java
@Override
public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
  String userString = request.getPAthInfo();
  User user = null;
  if(userString != null) {
    String username = userString.substring(1);
    user = UserDB.getByUsername(username);
  }

  if(user != null) {
    request.setAttribute("user", user);
    getServletContext().getRequestDispatcher("/user.jsp").forward(request, response);
  } else {
    response.sendError(404, "The requeseted user page doesn't exist.");
  }
}
```
