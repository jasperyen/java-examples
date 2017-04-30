/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

changeNew('selectNew');

function hideDelete( check, className ){
    var c = document.getElementsByClassName(className);
    
    if (check.checked) {
        for (var i = 0; i < c.length; i++) {
            c[i].type = "hidden";
        }
    }
    else{
        for (var i = 0; i < c.length; i++) {
            c[i].type = "text";
        }
    }
}

function changeNew(id) {
    var elm = document.getElementById(id);
    var i = elm.value;
    
    for (var j = 0; j < i; j++) {
        var c = document.getElementsByClassName("new_" + j);
        for (var k = 0; k < c.length; k++) {
            c[k].type = "text";
        }
    }
    for (var j = 9; j >=i; j--) {
        var c = document.getElementsByClassName("new_" + j);
        for (var k = 0; k < c.length; k++) {
            c[k].value = "";
            c[k].type = "hidden";
        }
    }
}

