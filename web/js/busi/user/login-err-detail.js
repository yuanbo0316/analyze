/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
var jsonStr = sessionStorage.log_err_detail;
sessionStorage.removeItem("log_err_detail");
var data = JSON.parse(jsonStr);

var o = new AjaxOptions();
o.timeout=30000;
o.put("server", data["server"]);
o.put("begin", data["begin"]);
o.put("end", data["end"]);
o.put("service_code", "S34110");
o.put("username_err", data["username_err"]);
o.put("ip_addr", data["ip_err"]);
o.sus = function(sus_data) {
    var result
    var result_data = sus_data.result;
    var pie = [];
    var categories = [];
    var series_data = [];
    for (var i = 0; i < result_data.length; i++) {
        pie[i] = [result_data[i].response_desc + "(" + result_data[i].response_code + ")", parseInt(result_data[i].sum)];
    }
    result = {chart: {
            plotBackgroundColor: null,
            plotBorderWidth: null,
            plotShadow: false
        },
        title: {
            text: "用户" + data["username_err"] + "登录失败统计(" + data["begin"] + "到" + data["end"] + ")"
        },
        subtitle: {
            text: data["ip_err"]
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