<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>redis的订阅推送功能</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
    <script type="text/javascript" src="js/jquery.min.js"></script>
    <script type="text/javascript" src="js/jquery.json-2.2.min.js"></script>
     <meta charset="UTF-8"/>
    <script type="text/javascript">
    var orderFlag=0;
    var uuid;
    function   KeyDown(){     
    	if   ((event.keyCode==116)||(event.ctrlKey   &&   event.keyCode==82)){   //Ctrl   +   R   
    	event.keyCode=0;   
    	event.returnValue=false;   
    	}   
    	}
    function processEvents(events) {
        if (events.length) {
            $('#logs').append('<span style="color: blue;">[client] ' + events.length + ' events</span><br/>');
        } else {
            $('#logs').append('<span style="color: red;">[client] no event</span><br/>');
        }
        for (var i in events) {
            $('#logs').append('<span>[event]渠道： ' + events[i].key+'  值：'+events[i].value + '</span><br/>');
        }
    }
    function order(){
    console.log(" into order...");
        $.getJSON('ajax',{wd:$('#subChannel').val(),uuid:uuid}, function(data) {
            console.log(data[0].uuid);
            retData=data[0];
            if(orderFlag!=1&&retData.result=="true"){
            	console.log("start long_polling...");
            	uuid=retData.uuid;
            long_polling(uuid);
            orderFlag=1;
            }
        });
    }
    function long_polling(uuid) {
    $.getJSON('ajax',{uuid:uuid}, function(events) {
    processEvents(events);
    console.log("发送请求..."+uuid);
     long_polling(uuid);
    });
    	
    }
        jQuery(function($) {
        
        });
    </script>
</head>
<body onkeydown="KeyDown()"    oncontextmenu="event.returnValue=false"> 
<input type="text" id="subChannel">
<input type="button" onclick="order();" value="订阅">
<div id="logs" style="font-family: monospace;">
</div>
</body>
</html>


