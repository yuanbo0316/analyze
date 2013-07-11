/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
var jsonStr = sessionStorage.user_trake_dialog;
sessionStorage.removeItem("user_trake_dialog");
if (jsonStr) {
    padBackData(JSON.parse(jsonStr), "", true);
}
var data = JSON.parse(jsonStr);

var json = data["args"];
try {
    if (json == "")
        json = "\"\"";
    var obj = eval("[" + json + "]");
    var html = ProcessObject(obj[0], 0, false, false, false);
    $id("args_json").innerHTML = "<PRE class='CodeContainer'>" + html + "</PRE>";
} catch (e) {
    alert("JSON数据格式不正确:\n" + e.message);
    $id("Canvas").innerHTML = "";
}