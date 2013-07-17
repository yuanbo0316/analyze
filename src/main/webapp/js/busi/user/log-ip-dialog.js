/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

var jsonStr = sessionStorage.log_ip_dialog;
sessionStorage.removeItem("log_ip_dialog");
var data = JSON.parse(jsonStr);

$("#ip_username_table").cutPage({
    timeout: 30000,
    page_size: 200,
    ip_addr: data["ip"],
    server: data["server"],
    service_code: "S34111"
}, function(list) {
    for (var i = 0; i < list.length; i++) {
        list[i].from_node = getParaValue("st.node", list[i].from_node);
        list[i].server = getParaValue("st.server", list[i].server);
    }
});




