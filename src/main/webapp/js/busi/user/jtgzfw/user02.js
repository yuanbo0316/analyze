/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
var page = navTab.getCurrentPanel();

var o = new AjaxOptions();
o.put("service_code", "S34102");
o.sus = function(data) {
    var categories = [];
    var loginTable = data.login_data;
    var name = ["PC浏览器", "app.js客户端", "总计"]
    //  var name = ["其它", "定时任务", "PC浏览器", "手机浏览器", "app.js客户端", "android客户端", "iphone客户端", "android平板客户端", "ipad客户端", "接口调用", "总计"];
    var s_count = sum(loginTable);
    var series = [];
    series[3] = {type: 'pie',
        name: '登陆',
        data: [],
        center: [30, 10],
        size: 100,
        showInLegend: false,
        dataLabels: {
            enabled: false
        }};
    for (var i = 0; i < 2; i++) {
        series[3].data[i] = {y: s_count[i],
            name: name[i],
            color: Highcharts.getOptions().colors[i]}
    }
    for (var i = 0; i < 3; i++) {
//        series[11].data[i]={ name:name[i],
//                        y: 13,
//                        color: Highcharts.getOptions().colors[0] };
        series[i] = {type: 'spline',
            name: name[i],
            data: [],
            marker: {
                lineWidth: 2,
                lineColor: Highcharts.getOptions().colors[3],
                //fillColor: 'white'
            }};
    }
    for (var i = 0; i < loginTable.length; i++) {
        categories[i] = loginTable[i].save_time;
//        series[0].data[i] = Number(loginTable[i].qt_count);
//        series[1].data[i] = Number(loginTable[i].ds_count);
        series[0].data[i] = Number(loginTable[i].pc_count);
//        series[3].data[i] = Number(loginTable[i].sj_count);
        series[1].data[i] = Number(loginTable[i].app_count);
//        series[5].data[i] = Number(loginTable[i].android_count);
//        series[6].data[i] = Number(loginTable[i].iphone_count);
//        series[7].data[i] = Number(loginTable[i].pb_count);
//        series[8].data[i] = Number(loginTable[i].ipad_count);
//        series[9].data[i] = Number(loginTable[i].jk_count);
        series[2].data[i] = Number(loginTable[i].sum_count);
    }
    $('#log-final-line',navTab.getCurrentPanel()).highcharts({
        chart: {
        },
        title: {
            text: '最近30日登陆情况'
        },
        xAxis: {
            
            categories: categories
        },
         yAxis: {
            title: {
                text: '人/次'
            }
        },
        tooltip: {
            crosshairs: true,
            shared: true
        },
        plotOptions: {
            spline: {
                marker: {
                    radius: 4,
                    lineColor: '#666666',
                    lineWidth: 1
                }
            }
        },
        labels: {
            items: [{
                    html: '30日内登陆情况',
                    style: {
                        left: '15px',
                        top: '90px',
                        color: 'black'
                    }
                }]
        },
        series: series
    });
};
$.ajax(o);

function sum(data) {
    var a = 0;
    var b = 0;
//    var c = 0;
////    var d = 0;
////    var e = 0;
////    var f = 0;
////    var g = 0;
////    var h = 0;
////    var i = 0;
////    var j = 0;
    for (var i = 0; i < data.length; i++) {
//        a = a + Number(data[i].qt_count);
//        b = b + Number(data[i].ds_count);
        a = a + Number(data[i].pc_count);
//        d = d + Number(data[i].sj_count);
        b = b + Number(data[i].app_count);
//        f = f + Number(data[i].android_count);
//        g = g + Number(data[i].iphone_count);
//        h = h + Number(data[i].pb_count);
//        i = i + Number(data[i].ipad_count);
//        j = j + Number(data[i].jk_count);
    }
    return [a, b]
    //   return [a, b, c, d, e, f, g, h, i, j];
}