<%-- 
    Document   : index
    Created on : 2013-6-22, 14:32:59
    Author     : lianzt
--%>

<%@page contentType="text/html" pageEncoding="GBK"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=GBK">
         <script type="text/javascript" src="javascript/jquery-1.7.1.js"></script>
         <script type="text/javascript" src="javascript/highcharts.js"></script>
         <script type="text/javascript" src="javascript//modules/exporting.js"></script>
         <script type="text/javascript">    
             $(function(){                 
                window.location.href ="/analyze/" + "_page/index.do";
             });
         </script>
        <title>JSP Page</title>
    </head>
    <body>
       <div id="container" style="min-width: 400px; height: 400px; margin: 0 auto"></div>
    </body>
</html>
