/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
var page = navTab.getCurrentPanel();

initParaSelect("st.server", $("#server", page));

$("#search-button", page).click(function() {
    $("#login_his_list", page).cutPage({
        timeout:30000,
        page_size: 200,
        username_his:$("#username_his",page).val(),
        from_node: $("#from_node", page).val(),
        server: $("#server", page).val(),
        service_code: "S34107"
    }, function(list) {
        for (var i = 0; i < list.length; i++) {
            list[i].from_node = getParaValue("st.node", list[i].from_node);
            list[i].server = getParaValue("st.server", list[i].server);
        }
    });

});

$("#edit", page).click(function() {
    var rowData = $(this).getRow();
    if (rowData) {
        sessionStorage.login_person_count = JSON.stringify(rowData);
        $.pdialog.open("page/user/login-person-time.html", 'login-person-time', rowData.username, {"width": 800, "height": 510});
    }
});