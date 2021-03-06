/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



initParaSelect("st.server", $("#server", navTab.getCurrentPanel()));
$("#search-button", navTab.getCurrentPanel()).click(function() {
    if ($("#sql_name", navTab.getCurrentPanel()).val() == null || $("#server", navTab.getCurrentPanel()).val() == null) {
        alertMsg("请选择监控服务和SQL语句");
        return;
    }
    var o = new AjaxOptions();
    o.timeout = 30000;
    o.put("server", $("#server", navTab.getCurrentPanel()).val());
    o.put("sql_name", $("#sql_name", navTab.getCurrentPanel()).val());
    o.put("service_code", "S34401");
    o.sus = function(data) {
        var list = data.list;
        var m_list = [];
        var d_list = [];
        var h_list = [];
        var series = [];
        var categories = [];
        var year = "";
        var day = "";
        var month = "";
        if (list[0]) {
            if ((list[0].save_time).substring(0, 4) == (list[list.length - 1].save_time).substring(0, 4)) {
                year = $("#sql_name", navTab.getCurrentPanel()) + '运行情况(' + (list[0].save_time).substring(0, 4) + '年)';
            } else {
                year = $("#sql_name", navTab.getCurrentPanel()).val() + '运行情况(' + (list[0].save_time).substring(0, 4) + '年到' + (list[list.length - 1].save_time).substring(0, 4) + '年)';
            }
        } else {
            year =$("#sql_name", navTab.getCurrentPanel()).val() + '未执行';
        }
        series[0] = {"name": "最长运行时间", "data": []};
        series[1] = {"name": "最短运行时间", "data": []};
        series[2] = {"name": "平均运行时间", "data": []};
        var k = 0, j = 0, m = 0;
        for (var i = 0; i < list.length; i++) {
            if (list[i].forever == "2") {
                m_list[k] = list[i];
                k++;
            } else if (list[i].forever == "1") {
                d_list[j] = list[i]
                j++;
            } else {
                h_list[m] = list[i];
                m++;
            }
        }
        for (var i = 0; i < k; i++) {
            categories[i] = (m_list[i].save_time).substring(5, 7);
            series[0].data[i] = parseInt(m_list[i].max_run_time);
            series[1].data[i] = parseInt(m_list[i].min_run_time);
            series[2].data[i] = parseInt(m_list[i].avg_run_time);
        }
        for (var i = 0; i < j; i++) {
            if (month == (d_list[i].save_time).substring(5, 7)) {
                categories[i + k] = "";
            } else {
                categories[i + k] = (d_list[i].save_time).substring(5, 10);
            }
            series[0].data[k + i] = parseInt(d_list[i].max_run_time);
            series[1].data[k + i] = parseInt(d_list[i].min_run_time);
            series[2].data[k + i] = parseInt(d_list[i].avg_run_time);
        }
        for (var i = 0; i < m; i++) {
            if (day == (h_list[i].save_time).substring(5, 10)) {
                categories[k + j + i] = "";
            } else {
                categories[k + j + i] = h_list[i].save_time.substring(5);
            }
            series[0].data[k + j + i] = parseInt(h_list[i].max_run_time);
            series[1].data[k + j + i] = parseInt(h_list[i].min_run_time);
            series[2].data[k + j + i] = parseInt(h_list[i].avg_run_time);
        }

        $('#sql_run_time', navTab.getCurrentPanel()).highcharts({
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
});


