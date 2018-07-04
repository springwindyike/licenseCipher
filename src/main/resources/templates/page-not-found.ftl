<#import "/spring.ftl" as spring>
<html>
<head>
<#--
<script type="text/javascript" src="<@spring.url "/js/jquery/jquery.min.js" />"></script>
-->
<script>
	 function loadRootPage()
	{ 
		window.location.href="<@spring.url "/" />"; 
	} 
 
 		window.setTimeout("loadRootPage();",3000); 
</script>

</head>
<body>
	
	<div class="title" style="text-align:center;padding-top:200px;">
		<p><h3>非常抱歉, 您输入的地址不存在...</h3><p>
		<p>3秒钟后将自动跳转至<a href='<@spring.url "/" />'>欢迎页</a>
	</div>
<body>
</html>