/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

function pic_req_warn_all_run() {
    var jsonStr = sessionStorage.pic_req_warn_all;
    sessionStorage.removeItem("pic_req_warn_all");
    if (jsonStr) {
        padBackData(JSON.parse(jsonStr), "", true);
    }
    var data = JSON.parse(jsonStr);
     var o = new AjaxOptions();
    o.timeout = 30000;
    o.put("end", data["end"]);
    o.put("begin",data["begin"]);
    o.put("service_code", "S34304");
    o.sus = function(data) {
        var list = data.list;
        var series = [];
        var categories = [];
        var year = "";
        if (list[0]) {
            if ((list[0].req_time).substring(0, 4) == (list[list.length - 1].req_time).substring(0, 4)) {
                year = '照片请求运行情况(' + (list[0].req_time).substring(0, 4) + '年)';
            } else {
                year =  '照片请求运行情况(' + (list[0].req_time).substring(0, 4) + '年到' + (list[list.length - 1].req_time).substring(0, 4) + '年)';
            }
        } else {
            year ='照片请求未执行';
        }
        series[0] = {"name": "运行时间", "data": []};
        for (var i = 0; i < list.length; i++) {
            categories[i] = (list[i].req_time).substring(5);
            series[0].data[i] = parseInt(list[i].run_time);
        }
      

        $('#pic_req_warn_detail').highcharts({
            chart: {type: 'spline'},
            title: {text:  year },
            xAxis: {
                tickInterval: 20,
                categories: categories,
                showLastLabel: true,
                labels: {
                    rotation: 0,
                    align: 'right',
                    style: {
                        fontSize: '12px',
                        fontFamily: 'Verdana, sans-serif'
                    }
                }
            },
            yAxis: {
                title: {
                    text: '毫秒（ms）'
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
                        lineWidth: 1,
                        enabled: false
                    }
                }
            },
            series: series
        });
    }
    $.ajax(o);

}

pic_req_warn_all_run();