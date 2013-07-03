/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
var page = navTab.getCurrentPanel();

var o = new AjaxOptions();
o.put("service_code", "S34101");
o.sus = function(data) {
    var categories = [];
    var series = [];
    series[0] = {"name": "成功", "data": []};
    series[1] = {"name": "失败", "data": []};
    series[2] = {"name": "总计", "data": []};
    series[3] = {"name": "超过预警时间", "data": []};
    var regTable = data.reg_table;
    for (var i = 0; i < regTable.length; i++) {
        categories[i] = regTable[i].save_time;
        series[0].data[i] = Number(regTable[i].suc_count);
        series[1].data[i] = Number(regTable[i].fal_count);
        series[2].data[i] = Number(regTable[i].sum_count);
        series[3].data[i] = Number(regTable[i].timeout_count);
    }
        console.debug(json2string(series));
    $('#reg-line').highcharts({
        chart: {type: 'spline'},
        title: {text: '最近10天的用户注册情况'},
//        subtitle: {text: 'Source: WorldClimate.com'},
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
};
$.ajax(o);
