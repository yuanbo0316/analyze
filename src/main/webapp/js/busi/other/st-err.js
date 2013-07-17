/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



for (var i = 0; i < 24; i++) {
    var o = i < 10 ? "0" + i : i;
    var option = $("<option/>").attr("value", o).html(o);
    $("#begin-hour,#end-hour", navTab.getCurrentPanel()).append(option);
}

for (var i = 0; i < 60; i++) {
    var o = i < 10 ? "0" + i : i;
    var option = $("<option/>").attr("value", o).html(o);
    $("#begin-minute,#end-minute,#begin-second,#end-second", navTab.getCurrentPanel()).append(option);
}

initParaSelect("st.server", $("#server", navTab.getCurrentPanel()));

$("#search-button", navTab.getCurrentPanel()).click(function() {
    if ($("#search-form", navTab.getCurrentPanel()).valid()) {
        //%20=" "   %3A=":"
//        var begin = $("#begin", navTab.getCurrentPanel()).val() + "%20" + $("#begin-hour", navTab.getCurrentPanel()).val() + "%3A" + $("#begin-minute", navTab.getCurrentPanel()).val() + "%3A" + $("#begin-second", navTab.getCurrentPanel()).val();
//        var end = $("#end", navTab.getCurrentPanel()).val() + "%20" + $("#end-hour", navTab.getCurrentPanel()).val() + "%3A" + $("#end-minute", navTab.getCurrentPanel()).val() + "%3A" + $("#end-second", navTab.getCurrentPanel()).val();
        var begin = $("#begin", navTab.getCurrentPanel()).val() + " " + $("#begin-hour", navTab.getCurrentPanel()).val() + ":" + $("#begin-minute", navTab.getCurrentPanel()).val() + ":" + $("#begin-second", navTab.getCurrentPanel()).val();
        var end = $("#end", navTab.getCurrentPanel()).val() + " " + $("#end-hour", navTab.getCurrentPanel()).val() + ":" + $("#end-minute", navTab.getCurrentPanel()).val() + ":" + $("#end-second", navTab.getCurrentPanel()).val();
        $("#error_list", navTab.getCurrentPanel()).cutPage({
            begin: begin,
            end: end,
            server: $("#server", navTab.getCurrentPanel()).val(),
            service_code: "S34501"
        }, function(list) {
            for (var i = 0; i < list.length; i++) {
                list[i].server = getParaValue("st.server", list[i].server);
            }
        });
    }
});

$("#search-today", navTab.getCurrentPanel()).click(function() {
    $("#error_list", navTab.getCurrentPanel()).cutPage({
        begin: getNowDate(),
        end: getNowDateTime(),
        server: $("#server", navTab.getCurrentPanel()).val(),
        service_code: "S34501"
    }, function(list) {
        for (var i = 0; i < list.length; i++) {
            list[i].server = getParaValue("st.server", list[i].server);
        }
    });
});

$("#edit", navTab.getCurrentPanel()).click(function() {
    var rowData = $(this).getRow();
    if (rowData) {
        sessionStorage.st_err_detail = JSON.stringify(rowData);
        $.pdialog.open("page/other/st-err-dialog.html", 'st_err_dialog', rowData.msg, {"width": 800, "height": 420});
    }
});

