/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
var jsonStr = sessionStorage.err_server_detail;
var data = JSON.parse(jsonStr);

var o = new AjaxOptions();
o.put("server", data["server"]);
o.put("service_code_detail", data["service_code"]);
o.put("begin", data["begin"] ? data["begin"] : getNowDate());
o.put("end", data["end"] ? data["end"] : getNowDateTime());
o.put("service_code", "S34202");
o.sus = function(sus_data) {
    var categories = [];
    var series = [];
    var time = sus_data.time;
    for(var i=0;i<time.length;i++){        
        categories[i] = time[i].save_time;
    }
    var result = sus_data.result;
    for (var i = 0; i < result.length; i++) {
        series[i] = {
            name: result[i].name,
            data: result[i].counts
        }
    }
    var result = {chart: {
            type: 'column'
        },
        title: {
            text:getParaValue(data["server"]+".service",data["service_code"])
        },
        xAxis: {
            categories: categories
        },
        yAxis: {
            min: 0,
            title: {
                text: '错误次数（次）'
            }
        },
        tooltip: {
            headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
            pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
                    '<td style="padding:0"><b>{point.y} 次</b></td></tr>',
            footerFormat: '</table>',
            shared: true,
            useHTML: true
        },
        plotOptions: {
            column: {
                pointPadding: 0.2,
                borderWidth: 0
            }
        },
        series: series};
          $("#server_detail").highcharts(result);
};
$.ajax(o);