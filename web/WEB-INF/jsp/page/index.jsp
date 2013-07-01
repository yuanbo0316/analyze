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
         <script type="text/javascript" src="../javascript/jquery-1.7.1.js"></script>
         <script type="text/javascript" src="../javascript/highcharts.js"></script>
         <script type="text/javascript" src="../javascript/modules/exporting.js"></script>
         <script type="text/javascript">    
             $(function(){
                 var a = ${out_json};
                 var date = a.avg_time;
                 var categories = a.sql_name;
                 var b =  {chart: {
                type: 'column',
                margin: [ 50, 50, 100, 80]
            },
            title: {
                text: 'World\'s largest cities per 2008'
            },
            xAxis: {
                categories: categories,
                labels: {
                    rotation: -45,
                    align: 'right',
                    style: {
                        fontSize: '13px',
                        fontFamily: 'Verdana, sans-serif'
                    }
                }
            },
            yAxis: {
                min: 0,
                title: {
                    text: 'Population (millions)'
                }
            },
            legend: {
                enabled: false
            },
                     plotOptions: {
                column: {
                    cursor: 'pointer',
                    point: {
                        events: {
                            click: function() {
                                alert(this.name);
                            }
                        }
                    },
                    dataLabels: {
                        enabled: true,
                        color: colors[0],
                        style: {
                            fontWeight: 'bold'
                        },
                        formatter: function() {
                            return this.y +'%';
                        }
                    }
                }
            },
            tooltip: {
                formatter: function() {
                    return '<b>'+ this.x +'</b><br/>'+
                        'Avg_run_time is: '+ Highcharts.numberFormat(this.y, 1) +
                        ' ms' +'<input id="btn_service",type="text",value="button",name="button"/>'  ;
                }
            },
            series: [{                
                name: 'Population',
                data:date ,
                dataLabels: {
                    enabled: true,
                    rotation: -90,
                    color: '#FFFFFF',
                    align: 'right',
                    x: 4,
                    y: 10,
                    style: {
                        fontSize: '13px',
                        fontFamily: 'Verdana, sans-serif'
                    }
                }
            }]
        };
             $('#container').highcharts(b) ;
             });
         </script>
        <title>JSP Page</title>
    </head>
    <body>
        1
       <div id="container" style="min-width: 400px; height: 400px; margin: 0 auto"></div>
    </body>
</html>
