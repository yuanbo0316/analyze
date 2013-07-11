/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
var page = navTab.getCurrentPanel();

initParaSelect("st.server", $("#server", page));

$("#search-button", page).click(function() {
    $("#username_count_list", page).cutPage({
        timeout: 30000,
        page_size: 200,
        username_ip: $("#username_ip", page).val(),
        server: $("#server", page).val(),
        service_code: "S34108"
    }, function(list) {
        for (var i = 0; i < list.length; i++) {
            list[i].server = getParaValue("st.server", list[i].server);
        }
    });

});

$("#edit", page).click(function() {
    var rowData = $(this).getRow();
    if (rowData) {
        sessionStorage.log_ip_dialog = JSON.stringify({server:$("#server", page).val(),ip:rowData.ip});
        $.pdialog.open("page/user/log-ip-dialog.html", 'log-ip-dialog', rowData.ip+'登录用户详情', {"width": 800, "height": 530});
    }
});

