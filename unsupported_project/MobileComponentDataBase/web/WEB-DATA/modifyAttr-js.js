/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

function changeNew(id) {
    var elm = document.getElementById(id);
    var i = elm.value;
    
    for (var j = 0; j < i; j++) {
        var n = document.getElementById("new_" + j);
        n.type = "text";
    }
    for (var j = 9; j >=i; j--) {
        var n = document.getElementById("new_" + j);
        n.value = "";
        n.type = "hidden";
    }
}

function clickDel(check, id) {
    var elm = document.getElementById(id);
    console.log(elm.type);
   if (check.checked){
       elm.value = "";       
       elm.type="hidden";
   }
   else {
       elm.type = "text";
   }
}