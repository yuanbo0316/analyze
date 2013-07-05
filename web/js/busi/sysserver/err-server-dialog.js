/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
var jsonStr = sessionStorage.err_server_dialog;
sessionStorage.removeItem("err_server_dialog");
if (jsonStr) {
    padBackData(JSON.parse(jsonStr), "", true);
}

