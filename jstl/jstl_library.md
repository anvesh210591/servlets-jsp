# How to use JSTL library

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
