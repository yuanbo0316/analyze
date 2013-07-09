var jsonStr = sessionStorage.err_server_all;
sessionStorage.removeItem("err_server_all");
var data = JSON.parse(jsonStr);
var o = new AjaxOptions();
o.timeout = 30000;
o.put("end",data["end"]);
o.put("begin",data["begin"]);
o.put("server",data["server"]);
o.put("is_timeout",data["is_timeout"]);
o.put("service_code", "S34302");
o.sus = function(sus_data) {
    var result_data = sus_data.result;
    var pie = [];
    var categories = [];
    var series_data = [];
    for (var i = 0; i < result_data.length; i++) {
        series_data[i] = parseInt(result_data[i].sum);
        categories[i] = result_data[i].req_url;
    }
    var result = {chart: {
            type: 'column',
            margin: [50, 50, 100, 80]
        },
        title: {
            text: "异常请求(" + data["begin"] + "到" + data["end"] + ")"
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
                text: '异常次数（次）'
            }
        },
        legend: {
            enabled: false
        },
        tooltip: {
            formatter: function() {
                return '<b>' + this.x + '</b><br/>' +
                        '异常: ' + this.y +
                        '次';
            }
        },
        series: [{
                name: 'Population',
                data: series_data,
                dataLabels: {
                    enabled: false,
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
            }]};
    $("#req_warn_detail").highcharts(result);
};
$.ajax(o);
