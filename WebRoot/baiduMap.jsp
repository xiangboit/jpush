<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
<script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=lqVPEv89dzsVbEdfI5GPRUnT"></script>
<title>百度地图的Hello, World</title>
<style type="text/css">
body, html,#allmap {width: 100%;height: 100%;overflow: hidden;margin:0;}
</style>
</head>
<body>
<div id="allmap"></div>
</body>
</html>
<script type="text/javascript">

// 百度地图API功能
var map = new BMap.Map("allmap");            // 创建Map实例
var point = new BMap.Point(116.404, 39.915);    // 创建点坐标
map.centerAndZoom(point,15);                     // 初始化地图,设置中心点坐标和地图级别。
map.enableScrollWheelZoom();                            //启用滚轮放大缩小
var local = new BMap.LocalSearch(map, {
  renderOptions:{map: map}
});
local.search("移动营业厅");
</script>

