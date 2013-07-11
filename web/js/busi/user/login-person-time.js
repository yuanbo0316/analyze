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
options.sus = function(data) {
    var now_date = new Date();
    var now_today = now_date.getFullYear() + '-' + (now_date.getMonth() + 1) + '-' + (now_date.getDate() + 1);
    var list = data.list;
    var j = 0, n = 0, m = 0, q = 0;
    var k = 0;
    var one_data = [];
    var two_data = [];
    var three_data = [];
    var four_data = [];
    var five_data = [];
    var test = [00, 01, 02, 03, 04, 05, 06, 07, 08, 09, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23];
    var date = new Date();
    for (var i = 0; i < list.length; i++) {
        date.setDate(parseInt((list[i].save_time).substring(8)));
        date.setMonth(parseInt((list[i].save_time).substring(5, 7)));
        date.setFullYear(parseInt((list[i].save_time).substring(0, 4)));
        if (list[i].opt == "1") {
            one_data[j] = [29 - DateDiff(list[i].save_time, now_today), timeChange(list[i].hour)];
            j++;
        } else if (list[i].opt == "2") {
            two_data[k] = [29 - DateDiff(list[i].save_time, now_today), timeChange(list[i].hour)];
            k++;
        } else if (list[i].opt == "3") {
            three_data[k] = [29 - DateDiff(list[i].save_time, now_today), timeChange(list[i].hour)];
            n++;
        } else if (list[i].opt == "4") {
            four_data[k] = [29 - DateDiff(list[i].save_time, now_today), timeChange(list[i].hour)];
            m++;
        } else {
            five_data[k] = [29 - DateDiff(list[i].save_time, now_today), timeChange(list[i].hour)];
            q++;
        }
    }
    var charts = {
        chart: {
            marginTop: 70
        },
        xAxis: {
            title: {
                text: ''
            },
            tickInterval: 4,
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
        tooltip: {
            formatter: function() {
                return '<b>' + '登录时间：' + '</b><br/>' + this.x + ' ' + floatChange(this.y)

            }
        },
        series: [{
                type: 'scatter',
                name: '登录',
                color: 'rgba(223, 83, 83, .5)',
                data: one_data,
                marker: {
                    radius: 4
                }
            }, {
                type: 'scatter',
                name: '注销',
                color: 'rgba(119, 152, 191, .5)',
                data: two_data,
                marker: {
                    radius: 4
                }
            }, {
                type: 'scatter',
                name: '超时自动注销',
                color: 'rgba(30,98,188, .5)',
                data: three_data,
                marker: {
                    radius: 4
                }
            }, {
                type: 'scatter',
                name: '客户端超时自动登录',
                color: 'rgba(122,171,40, .5)',
                data: four_data,
                marker: {
                    radius: 4
                }
            }, {
                type: 'scatter',
                name: '登录失败',
                color: 'rgba(80,56,119, .5)',
                data: five_data,
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
function  DateDiff(sDate1, sDate2) {    //sDate1和sDate2是2006-12-18格式    
    var aDate, oDate1, oDate2, iDays
    aDate = sDate1.split("-")
    oDate1 = new Date(aDate[1] + '-' + aDate[2] + '-' + aDate[0])    //转换为12-18-2006格式    
    aDate = sDate2.split("-")
    oDate2 = new Date(aDate[1] + '-' + aDate[2] + '-' + aDate[0])
    iDays = parseInt(Math.abs(oDate1 - oDate2) / 1000 / 60 / 60 / 24)    //把相差的毫秒数转换为天数   
    return  iDays + 1
}

function timeChange(time) {
    var a = parseInt(time.substring(0, 2)) + parseFloat(time.substring(3, 5)) / 60 + parseFloat(time.substring(6)) / 3600;
    return a;
}

function floatChange(time) {
    var a = Math.floor(time);
    var b = Math.floor((time - a) * 60);
    var c = Math.floor(((time - a) * 60 - b) * 60);
    if (b < 10) {
        b = '0' + b;
    }
    if (c < 10) {
        c = '0' + c;
    }
    return a + ":" + b + ":" + c;
}