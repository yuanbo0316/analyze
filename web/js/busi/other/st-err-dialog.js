/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

var jsonStr = sessionStorage.st_err_detail;
sessionStorage.removeItem("st_err_detail");
if (jsonStr) {
    padBackData(JSON.parse(jsonStr), "", true);
}
