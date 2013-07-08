/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
var jsonStr = sessionStorage.err_server_detail;
sessionStorage.removeItem("err_server_detail");
var data = JSON.parse(jsonStr);

var o = new AjaxOptions();
o.put("server", data["server"]);
o.put("service_code_detail", data["service_code"]);
o.put("begin", data["begin"] ? data["begin"] : getNowDate());
o.put("end", data["end"] ? data["end"] : getNowDateTime());
o.put("service_code", "S34202");
o.sus = function(sus_data) {
    var result
    var result_data = sus_data.result;
    var pie = [];
    var categories = [];
    var series_data = [];
    for (var i = 0; i < result_data.length; i++) {
        pie[i] = [result_data[i].response_desc+"("+result_data[i].response_code+")", parseInt(result_data[i].sum)];
    }
    result = {chart: {
            plotBackgroundColor: null,
            plotBorderWidth: null,
            plotShadow: false
        },
        title: {
            text: getParaValue(data["server"] + ".service", data["service_code"]) + "(" + data["begin"] + "到" + data["end"] + ")"
        },
        tooltip: {
            pointFormat: '{series.name}: <b>{point.y}</b>',
            percentageDecimals: 1
        },
        plotOptions: {
            pie: {
                allowPointSelect: true,
                cursor: 'pointer',
                dataLabels: {
                    enabled: false
                },
                showInLegend: true
            }
        },
        series: [{
                type: 'pie',
                name: '异常次数',
                data: pie
            }]};

    $("#server_detail").highcharts(result);
};
$.ajax(o);