<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root version="2.0" xmlns="http://www.w3c.org/1999/xhtml"
          xmlns:jsp="http://java.sun.com/JSP/Page">

    <jsp:output doctype-root-element="html"
                doctype-public="-//W3C//DTD XHTML 1.0 Transitional//EN"
                doctype-system="http://www.w3c.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"/>

    <jsp:directive.page session="false"
                        contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"/>

    <html>
    <head>
        <title>${pageContext.servletContext.servletContextName}</title>
    </head>
    <body>
    Running <b>${pageContext.servletContext.servletContextName}</b>
    </body>
    </html>
</jsp:root>