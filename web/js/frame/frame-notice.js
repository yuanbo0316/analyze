/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
var _is_notice = false;
var ws = null;
var _no_read_count = 0;     //未读通知计算器，在异常查看页面清0
$(function() {
    if (window.webkitNotifications) {
        if (window.webkitNotifications.checkPermission() == 1) { // 0 is PERMISSION_ALLOWED
            setTimeout(function() {
                alertMsg.confirm("检测到没有开户桌面通知，是否开启？", {"okCall": function() {
                        window.webkitNotifications.requestPermission();
                        createSocket();
                        _is_notice = true;
                        $(this).html("关闭通知");
                    }});
            }, 5000);
        } else {
            createSocket();
            _is_notice = true;
            $("#change_notice").html("关闭通知");
        }
    }
    $("#change_notice").click(function() {
        if (window.webkitNotifications) {
            if (window.webkitNotifications.checkPermission() == 1) { // 0 is PERMISSION_ALLOWED
                alertMsg.confirm("检测到没有开户桌面通知，是否开启？", {"okCall": function() {
                        window.webkitNotifications.requestPermission();
                        _is_notice = true;
                        createSocket();
                        $(this).html("关闭通知");
                    }});
            } else {
                if (_is_notice) {
                    _is_notice = false;
                    $(this).html("开启通知");
                } else {
                    _is_notice = true;
                    $(this).html("关闭通知");
                }
            }
        } else {
            alertMsg.error("浏览器不支持桌面通知，请使用chrome！");
            _is_notice = false;
        }
    });
});

function createSocket() {
    if (ws)
        return;
    ws = new WebSocket(location.href.replace("http://", 'ws://').replace(/\/analyze\/.*/, '/analyze/ErrorMsgServlet'));
    ws.onmessage = function(evt) {
        console.log(evt.data);
        notice("服务器端通知(单击可查看)", evt.data, function() {
            this.cancel;
            //跳转到异常查询页面
            navTab.openTab('user01', 'page/user/jtgzfw/user01.html', {title: "New Tab", fresh: true});
            _no_read_count = 0;
            //激活弹出该通知窗口的浏览器窗口
            window.focus();
            //打开IM窗口
            WM.openWinByID();
        });
    };

    ws.onclose = function(evt) {
        console.log("close");
    };

    ws.onopen = function(evt) {
        console.log("open");
    };
}

/**
 * 桌面通知函数
 * @param {string} title 标题
 * @param {string} content 内容
 * @param {function} click 单击回调函数
 * @return {boolean} 通知是否成功
 */
function notice(title, content, click, close) {
    if (_is_notice) {
        _no_read_count++;       //如果没打开，计数器加1
        if (_no_read_count > 1) {
            title = title + " (" + _no_read_count + ")";
        }
        var notification = window.webkitNotifications.createNotification('favicon.ico', title, content);
//        notification.ondisplay = display;
        notification.onclose = close;
        notification.onclick = click;
        notification.replaceId = "error log notice";
        notification.show();
    }
    return _is_notice;
}
