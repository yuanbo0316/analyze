/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
var jsonStr = sessionStorage.login_person_count;
sessionStorage.removeItem("login_person_count");
var data_json = JSON.parse(jsonStr);


var options = new AjaxOptions();
options.put("service_code", "S34105");
options.put("username", data_json["username"]);
options.put("from_node", data_json["from_node"]);
options.put("server", data_json["server"]);
options.sus = function(data) {
    var now_date = new Date();
    var list = data.list;
    var j = 0;
    var k = 0;
    var app_data = [];
    var pc_data = [];
    var test = [00, 01, 02, 03, 04, 05, 06, 07, 08, 09, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23];
    var date = new Date();
    for (var i = 0; i < list.length; i++) {
        date.setDate(parseInt((list[i].save_time).substring(8)));
        date.setMonth(parseInt((list[i].save_time).substring(5, 7)));
        date.setFullYear(parseInt((list[i].save_time).substring(0, 4)));
        if (list[i].from_node == "2") {
            pc_data[j] = [29 - (now_date.getDate() - date.getDate()), parseInt(list[i].hour)];
            j++;
        } else {
            app_data[k] = [29 - (now_date.getDate() - date.getDate()), parseInt(list[i].hour)];
            k++;
        }
    }
    console.log(pc_data);
    console.log(app_data);
    var charts = {
        chart: {
           marginTop:70
        },
        xAxis: {
            title: {
                text: ''
            },
            categories: getNearDate(new Date(), 30)
        },
        yAxis: {
            title: {
                text: '登陆时间(时)'
            },
            categories: [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23]
        },
        title: {
            text: '用户' + data_json["username"] + '近30天登录情况'
        },
        plotOptions: {
            scatter: {
                marker: {
                    radius: 5,
                    states: {
                        hover: {
                            enabled: true,
                            lineColor: 'rgb(100,100,100)'
                        }
                    }
                },
                states: {
                    hover: {
                        marker: {
                            enabled: false
                        }
                    }
                },
                tooltip: {
                    headerFormat: '<b>{series.name}</b><br>',
                    pointFormat: '登录时间：{point.y}点'
                }
            }
        },
        series: [{
                type: 'scatter',
                name: 'PC端登录',
                color: 'rgba(223, 83, 83, .5)',
                data: pc_data,
                marker: {
                    radius: 4
                }
            }, {
                type: 'scatter',
                name: 'app端登录',
                color: 'rgba(119, 152, 191, .5)',
                data: app_data,
                marker: {
                    radius: 4
                }
            }]};
    $("#person_time_detail").highcharts(charts);
};
$.ajax(options);


function getNearDate(today, len) {
    var list = [];
    for (var i = len - 1; i >= 0; i--) {
        list[i] = today.getFullYear() + "-" + (today.getMonth() + 1) + "-" + today.getDate();
        today.setDate(today.getDate() - 1);
    }
    return list;
}