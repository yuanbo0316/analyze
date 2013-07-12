/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */




var categories = [];
var series1 = [];
var series2 = [];
series1[0] = {"name": "交通公众服务网登录次数", "data": []};
series1[1] = {"name": "交通公众服务网用户人数", "data": []};
series2[0] = {"name": "交通安全协会登录次数", "data": []};
series2[1] = {"name": "交通安全协会用户人数", "data": []};
var o = new AjaxOptions();
o.timeout = 30000;
o.put("service_code", "S34106");
o.sus = function(data) {

    var list = data.list;
    var j = 0;
    var k = 0;
    for (var i = 0; i < list.length; ) {
        if (list[i].server == "jtgzfw") {
            categories[j] = list[i].save_time;
            series1[0].data[j] = parseInt(list[i].login_times);
            series1[1].data[j] = parseInt(list[i].user_count);
            j++;
        } else {
            series2[0].data[k] = parseInt(list[i].login_times);
            series2[1].data[k] = parseInt(list[i].user_count);
            k++;
        }
        if (j > k) {
            series2[0].data[k] = 0;
            series2[1].data[k] = 0;
            k++;
        }
        i++;
    }
     $('#log-final-line', navTab.getCurrentPanel()).highcharts({
        chart: {type: 'spline'},
        title: {text: '最近30天的用户登录情况'},
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
        series: series1
    });
};
$.ajax(o);

$("#search-button", navTab.getCurrentPanel()).click(function() {
    var series;
    if ($("#server_user", navTab.getCurrentPanel()).val() == "jtgzfw") {
        series = series1;
    } else {
        series = series2;
    }
    $('#log-final-line', navTab.getCurrentPanel()).highcharts({
        chart: {type: 'spline'},
        title: {text: '最近30天的用户登录情况'},
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
        series: series
    });
});