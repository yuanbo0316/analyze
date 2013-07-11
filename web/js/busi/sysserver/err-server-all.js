var jsonStr = sessionStorage.err_server_all;
sessionStorage.removeItem("err_server_all");
var data = JSON.parse(jsonStr);
var o = new AjaxOptions();
o.timeout = 30000;
o.put("begin", data["begin"] ? data["begin"] : getNowDate());
o.put("end", data["end"] ? data["end"] : getNowDateTime());
o.put("service_code", "S34203");
o.sus = function(sus_data) {
    var result_data = sus_data.result;
    var pie = [];
    var categories = [];
    var series_data = [];
    var k = 0;
    for (var i = 0; i < result_data.length; i++) {
        if (parseInt(result_data[i].sum) > 100) {
            series_data[k] = parseInt(result_data[i].sum);
            categories[k] = getParaValue(result_data[i].server + ".service", result_data[i].service_code) + "(" + result_data[i].service_code + ")";
            k++;
        }
    }
    var result = {chart: {
            type: 'column',
            margin: [50, 50, 100, 80]
        },
        title: {
            text: "异常服务(" + data["begin"] + "到" + data["end"] + ")"
        },
        subtitle: {
            text: '只显示执行次数大于100次的服务'
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
    $("#server_all_detail").highcharts(result);
}
;
$.ajax(o);
