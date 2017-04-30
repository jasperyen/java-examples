/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


function changesel( sel ){
   
    $(".opt").hide();
    
    var val = document.getElementById(sel).value;
    
    $("."+ val).show();
    
    if (val.match("all"))
        $(".opt").show();
}