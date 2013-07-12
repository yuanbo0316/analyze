/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


initParaSelect("st.server", $("#server", navTab.getCurrentPanel()));

$("#search-button", navTab.getCurrentPanel()).click(function() {
    $("#login_his_list", navTab.getCurrentPanel()).cutPage({
        timeout:30000,
        page_size: 200,
        username_his:$("#username_his",navTab.getCurrentPanel()).val(),
        from_node: $("#from_node", navTab.getCurrentPanel()).val(),
        server: $("#server", navTab.getCurrentPanel()).val(),
        service_code: "S34107"
    }, function(list) {
        for (var i = 0; i < list.length; i++) {
            list[i].from_node = getParaValue("st.node", list[i].from_node);
            list[i].server = getParaValue("st.server", list[i].server);
        }
    });

});

$("#edit_app", navTab.getCurrentPanel()).click(function() {
    var rowData = $(this).getRow();
    if (rowData) {
        sessionStorage.login_person_count = JSON.stringify({username:rowData.username,from_node:'4'});
        $.pdialog.open("navTab.getCurrentPanel()/user/login-person-time.html", 'login-person-time', 'app.js客户端登录详细', {"width": 800, "height": 510});
    }
});

$("#edit_pc", navTab.getCurrentPanel()).click(function() {
    var rowData = $(this).getRow();
    if (rowData) {
        sessionStorage.login_person_count = JSON.stringify({username:rowData.username,from_node:'2'});
        $.pdialog.open("navTab.getCurrentPanel()/user/login-person-time.html", 'login-person-time', '网站登录详细', {"width": 800, "height": 510});
    }
});