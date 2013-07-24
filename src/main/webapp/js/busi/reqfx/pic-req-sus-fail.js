/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
var jsonStr = sessionStorage.pic_req_sus_fail;
sessionStorage.removeItem("pic_req_sus_fail");
var data = JSON.parse(jsonStr);

var o = new AjaxOptions();
o.timeout = 30000;
o.put("begin", data["begin"]);
o.put("end", data["end"]);
o.put("service_code", "S34305");
o.sus = function(sus_data) {
    var result
    var result_data = sus_data.result;
    var pie = [];
    var categories = [];
    var series_data = [];
    pie[0] = ["请求成功", parseInt(result_data.sum)];
    pie[1]=["请求失败",parseInt(result_data.all_test)-parseInt(result_data.sum)]
    result = {chart: {
            plotBackgroundColor: null,
            plotBorderWidth: null,
            plotShadow: false
        },
        title: {
            text: "照片请求情况" + "(" + data["begin"] + "到" + data["end"] + ")"
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
                name: '次数',
                data: pie
            }]};

    $("#pic_req_sus_fail").highcharts(result);
};
$.ajax(o);