/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

function changeSelect(name) {
    var j = document.getElementById(name).value;
    
    for (var i = 1; i <= j; i++) {
        document.getElementById(name + "_" + i).style.display = 'table-row';
    }
    for (var i = 10 ; i > j; i--) {
        if (!name.match("Description"))
            document.getElementById(name + "_" + i).getElementsByTagName("select")[0].value = "";
        
        else {
            document.getElementById(name + "_" + i).getElementsByTagName("input")[0].value = "";
            document.getElementById(name + "_" + i).getElementsByTagName("input")[1].value = "";
        }
        document.getElementById(name + "_" + i).style.display = 'none';
    }
    
}
