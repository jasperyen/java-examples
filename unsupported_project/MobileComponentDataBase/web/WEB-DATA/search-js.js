/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

changeCate(document.getElementById("category"));

function changeCate(cate){
    document.getElementById('vendor').style.display = "none";
    document.getElementById('cpu_code_name').style.display = "none";
    document.getElementById('memory_type').style.display = "none";
    document.getElementById('memory_frequency').style.display = "none";
    document.getElementById('memory_capacity').style.display = "none";
    document.getElementById('sensor_type').style.display = "none";
    document.getElementById('wwan_type').style.display = "none";
    document.getElementById('wlan_max_speed').style.display = "none";
    document.getElementById('panel_size').style.display = "none";
    document.getElementById('touch_panel_ic_vendor').style.display = "none";
    document.getElementById('touch_panel_ic').style.display = "none";
    document.getElementById('camera_pixel').style.display = "none";
    document.getElementById('camera_sensor_ic').style.display = "none";
    
    if(cate.value.match("cpu")){
        document.getElementById('vendor').style.display = "table-row";
        document.getElementById('cpu_code_name').style.display = "table-row";
    }
    else if(cate.value.match("memory")){
        document.getElementById('memory_type').style.display = "table-row";
        document.getElementById('memory_frequency').style.display = "table-row";
        document.getElementById('memory_capacity').style.display = "table-row";
        document.getElementById('vendor').style.display = "table-row";
    }
    else if(cate.value.match("sensor")){
        document.getElementById('vendor').style.display = "table-row";
        document.getElementById('sensor_type').style.display = "table-row";
    }
    else if(cate.value.match("ethernet")){
        document.getElementById('vendor').style.display = "table-row";
    }
    else if(cate.value.match("storage")){
        document.getElementById('vendor').style.display = "table-row";
    }
    else if(cate.value.match("wwan")){
        document.getElementById('vendor').style.display = "table-row";
        document.getElementById('wwan_type').style.display = "table-row";
    }
    else if(cate.value.match("wlan")){
        document.getElementById('vendor').style.display = "table-row";
        document.getElementById('wlan_max_speed').style.display = "table-row";
    }
    else if(cate.value.match("kbc_ebc")){
        document.getElementById('vendor').style.display = "table-row";
    }
    else if(cate.value.match("click_pad")){
        document.getElementById('vendor').style.display = "table-row";
    }
    else if(cate.value.match("lcd_panel")){
        document.getElementById('vendor').style.display = "table-row";
        document.getElementById('panel_size').style.display = "table-row";
    }
    else if(cate.value.match("touch_panel")){
        document.getElementById('vendor').style.display = "table-row";
        document.getElementById('panel_size').style.display = "table-row";
        document.getElementById('touch_panel_ic_vendor').style.display = "table-row";
        document.getElementById('touch_panel_ic').style.display = "table-row";
    }
    else if(cate.value.match("camera")){
        document.getElementById('vendor').style.display = "table-row";
        document.getElementById('camera_pixel').style.display = "table-row";
        document.getElementById('camera_sensor_ic').style.display = "table-row";
    }
    else if(cate.value.match("button")){
    }
    else if(cate.value.match("card_reader")){
        document.getElementById('vendor').style.display = "table-row";
    }
    else if(cate.value.match("antennas")){
    }
    else if(cate.value.match("keyboard")){
        document.getElementById('vendor').style.display = "table-row";
        document.getElementById('keyboard_type').style.display = "table-row";
        document.getElementById('keyboard_os').style.display = "table-row";
    }
    else if(cate.value.match("battery")){
        document.getElementById('vendor').style.display = "table-row";
    }
    else if(cate.value.match("charger")){
        document.getElementById('vendor').style.display = "table-row";
    }
    else if(cate.value.match("graphic")){
        document.getElementById('vendor').style.display = "table-row";
    }
    else if(cate.value.match("audio_codec")){
        document.getElementById('vendor').style.display = "table-row";
    }
    else if(cate.value.match("panel_interface_bridge")){
        document.getElementById('vendor').style.display = "table-row";
    }
    else if(cate.value.match("external_storage_card")){
        document.getElementById('vendor').style.display = "table-row";
    }
    else if(cate.value.match("odd")){
        document.getElementById('vendor').style.display = "table-row";
    }
    else if(cate.value.match("speaker")){
        document.getElementById('vendor').style.display = "table-row";
    }
    else if(cate.value.match("mic")){
        document.getElementById('vendor').style.display = "table-row";
    }
    else if(cate.value.match("io_port")){
    }
    else if(cate.value.match("os")){
    }
    
    

}