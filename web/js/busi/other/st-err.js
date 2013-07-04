/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

var page = navTab.getCurrentPanel();

for (var i = 0; i < 24; i++) {
    var o = i < 10 ? "0" + i : i;
    var option = $("<option/>").attr("value", o).html(o);
    $("#begin-hour,#end-hour", page).append(option);
}

for (var i = 0; i < 60; i++) {
    var o = i < 10 ? "0" + i : i;
    var option = $("<option/>").attr("value", o).html(o);
    $("#begin-minute,#end-minute,#begin-second,#end-second", page).append(option);
}

initParaSelect("st.server", $("#server", page));

$("#search-button", page).click(function() {
    if ($("#search-form", page).valid()) {
        //%20=" "   %3A=":"
//        var begin = $("#begin", page).val() + "%20" + $("#begin-hour", page).val() + "%3A" + $("#begin-minute", page).val() + "%3A" + $("#begin-second", page).val();
//        var end = $("#end", page).val() + "%20" + $("#end-hour", page).val() + "%3A" + $("#end-minute", page).val() + "%3A" + $("#end-second", page).val();
        var begin = $("#begin", page).val() + " " + $("#begin-hour", page).val() + ":" + $("#begin-minute", page).val() + ":" + $("#begin-second", page).val();
        var end = $("#end", page).val() + " " + $("#end-hour", page).val() + ":" + $("#end-minute", page).val() + ":" + $("#end-second", page).val();
        $("#error_list", page).cutPage({
            begin: begin,
            end: end,
            server: $("#server", page).val(),
            service_code: "S34501"
        }, function(list) {
            for (var i = 0; i < list.length; i++) {
                list[i].server = getParaValue("st.server", list[i].server);
            }
        });
    }
});

$("#search-today", page).click(function() {
    $("#error_list", page).cutPage({
        begin: getNowDate(),
        end: getNowDateTime(),
        server: $("#server", page).val(),
        service_code: "S34501"
    }, function(list) {
        for (var i = 0; i < list.length; i++) {
            list[i].server = getParaValue("st.server", list[i].server);
        }
    });
}).trigger("click");

$("#edit", page).click(function() {
    var rowData = $(this).getRow();
    if (rowData) {
        sessionStorage.st_err_detail = JSON.stringify(rowData);
        $.pdialog.open("page/other/st-err-dialog.html", 'st_err_dialog', rowData.msg, {"width": 800, "height": 420});
    }
});

