/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

var page = navTab.getCurrentPanel();

//根据不同的server生成相应的service_code
var test;
var ss = [];
$("#server", page).change(function() {
    if ($("#server", page).val() == 'jtgzfw') {
        test = getParaList("jtgzfw.service");
        for (var i = 0; i < test.length; i++) {
            ss[i] = test[i].col_value;
        }
        $("#op_combox_service_code_detail li").each(function() {
            $(this).css("display", "block");
            if ($(this).attr("id") == null || ss.indexOf($(this).attr("id")) != -1) {

            } else {
                $(this).css("display", "none");
            }
        });
    } else if ($("#server", page).val() == 'jtgzfw_man') {
        test = getParaList("jtgzfw_man.service");
        for (var i = 0; i < test.length; i++) {
            ss[i] = test[i].col_value;
        }
        $("#op_combox_service_code_detail li").each(function() {
            $(this).css("display", "block");
            if ($(this).attr("id") == null || ss.indexOf($(this).attr("id")) != -1) {

            } else {
                $(this).css("display", "none");
            }
        });
    } else if ($("#server", page).val() == 'jtaqxh') {
        test = getParaList("jtaqxh.service");
        for (var i = 0; i < test.length; i++) {
            ss[i] = test[i].col_value;
        }
        $("#op_combox_service_code_detail li").each(function() {
            $(this).css("display", "block");
            if ($(this).attr("id") == null || ss.indexOf($(this).attr("id")) != -1) {

            } else {
                $(this).css("display", "none");
            }
        });
    } else {
        $("#op_combox_service_code_detail li").each(function() {
            $(this).css("display", "block");
        });
    }
});
initParaSelect("st.server", $("#server", page));
initServiceParaSelect("jtgzfw.service", $("#service_code_detail", page));
initServiceParaSelect("jtgzfw_man.service", $("#service_code_detail", page));
initServiceParaSelect("jtaqxh.service", $("#service_code_detail", page));

$("#service_code_detail", page).combox();
$("#search-button", page).click(function() {
    if ($("#service_code_detail", page).val() == null || $("#server", page).val() == null) {
        alertMsg("请选择监控服务和服务码");
        return;
    }
    var o = new AjaxOptions();
    o.timeout = 30000;
    o.put("server", $("#server", page).val());
    o.put("service_code_detail", $("#service_code_detail", page).val());
    o.put("service_code", "S34204");
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
        if ((list[0].save_time).substring(0, 4) == (list[list.length - 1].save_time).substring(0, 4)) {
            year = (list[0].save_time).substring(0, 4) + '年';
        } else {
            year = (list[0].save_time).substring(0, 4) + '年到' + (list[list.length - 1].save_time).substring(0, 4) + '年';
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

        $('#service_run_time', page).highcharts({
            chart: {type: 'spline'},
            title: {text: getParaValue($("#server", page).val() + '.service', $("#service_code_detail", page).val()) + '运行情况(' + year + ')'},
            xAxis: {
                tickInterval: 8,
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


