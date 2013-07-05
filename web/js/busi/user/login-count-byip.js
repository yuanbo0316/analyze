/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


initParaSelect("st.server", $("#server", navTab.getCurrentPanel()));

$("#search-button", navTab.getCurrentPanel()).click(function() {
    $("#username_count_list", navTab.getCurrentPanel()).cutPage({
        timeout: 30000,
        page_size: 200,
        username_ip: $("#username_ip", navTab.getCurrentPanel()).val(),
        server: $("#server", navTab.getCurrentPanel()).val(),
        service_code: "S34108"
    }, function(list) {
        for (var i = 0; i < list.length; i++) {
            list[i].server = getParaValue("st.server", list[i].server);
        }
    });

});

$("#edit", navTab.getCurrentPanel()).click(function() {
    var rowData = $(this).getRow();
    if (rowData) {
        sessionStorage.log_ip_dialog = JSON.stringify({server:$("#server", navTab.getCurrentPanel()).val(),ip:rowData.ip});
        $.pdialog.open("page/user/log-ip-dialog.html", 'log-ip-dialog', rowData.ip+'登录用户详情', {"width": 800, "height": 530});
    }
});

