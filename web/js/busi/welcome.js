/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
function abde() {
    var o = new AjaxOptions();
    o.put("service_code", "S34103");
    o.timeout = 30000;
    o.sus = function(data) {
        var label1 = '车主注册短信验证';
        var label2 = '违章缴费订单';
        var all_data1 = [];
        var all_data2 = [];
        var today_data1 = [];
        var today_data2 = [];
        var today = data.today;
        var all = data.all;
        if (today[0] == null) {
            today[0] = {
                sum: 0
            }
            label1 = '无用户注册'
        }
        if (today[1] == null) {
            today[1] = {
                sum: 0
            }
            label2 = '无用户缴费'
        }
        if (today[2] == null) {
            today[2] = {
                sum: 0
            }
        }
        if (today[3] == null) {
            today[3] = {
                sum: 0
            }
        }
        today_data1[0] = {
            name: "未缴费（笔）",
            y: parseInt(today[1].sum) - parseInt(today[2].sum),
            color: 'RGB(139, 188, 33)'
        };
        today_data1[1] = {
            name: "已缴费（笔）",
            y: parseInt(today[2].sum)
        };
        today_data2[0] = {
            name: "失效（条）",
            y: parseInt(today[3].sum) - parseInt(today[0].sum),
            color: 'RGB(98,66,137)'
        };
        today_data2[1] = {
            name: "有效（条）",
            y: parseInt(today[0].sum),
            color: 'RGB(170, 25, 25)'
        };
        all_data1[0] = {
            name: "未缴费（笔）",
            y: parseInt(all[1].sum) - parseInt(all[2].sum),
            color: 'RGB(139, 188, 33)'
        };
        all_data1[1] = {
            name: "已缴费（笔）",
            y: parseInt(all[2].sum)
        };
        all_data2[0] = {
            name: "失效（条）",
            y: parseInt(all[3].sum) - parseInt(all[0].sum),
            color: 'RGB(98,66,137)'
        };
        all_data2[1] = {
            name: "有效（条）",
            y: parseInt(all[0].sum),
            color: 'RGB(170, 25, 25)'
        };
        var result1 = {chart: {
            },
            title: {
                text: '近10日情况统计'
            },
            tooltip: {
                formatter: function() {
                    var s;
                    s = '' +
                            this.point.name + ': ' + this.y;
                    return s;
                }
            },
            labels: {
                items: [{
                        html: '违章缴费订单',
                        style: {
                            left: '80px',
                            top: '10px',
                            color: 'black'
                        }
                    }, {
                        html: '车主注册短信验证',
                        style: {
                            left: '280px',
                            top: '10px',
                            color: 'black'
                        }
                    }]
            },
            series: [{
                    type: 'pie',
                    name: '笔',
                    data: all_data1,
                    center: [90, 90],
                    size: 150,
                    showInLegend: false,
                    dataLabels: {
                        enabled: false
                    }
                }, {
                    type: 'pie',
                    name: '条',
                    data: all_data2,
                    center: [310, 90],
                    size: 150,
                    showInLegend: false,
                    dataLabels: {
                        enabled: false
                    }
                }]};
        var result2 = {chart: {
            },
            title: {
                text: '今日情况统计'
            },
            tooltip: {
                formatter: function() {
                    var s;
                    s = '' +
                            this.point.name + ': ' + this.y + ' 次';
                    return s;
                }
            },
            labels: {
                items: [{
                        html: label2,
                        style: {
                            left: '80px',
                            top: '10px',
                            color: 'black'
                        }
                    }, {
                        html: label1,
                        style: {
                            left: '280px',
                            top: '10px',
                            color: 'black'
                        }
                    }]
            },
            series: [{
                    type: 'pie',
                    name: 'Total consumption',
                    data: today_data1,
                    center: [90, 90],
                    size: 150,
                    showInLegend: false,
                    dataLabels: {
                        enabled: false
                    }
                }, {
                    type: 'pie',
                    name: 'Total consumption',
                    data: today_data2,
                    center: [310, 90],
                    size: 150,
                    showInLegend: false,
                    dataLabels: {
                        enabled: false
                    }
                }]};
        $("#all-detail").highcharts(result1);
        $("#today-detail").highcharts(result2);
    }
    $.ajax(o);
}
