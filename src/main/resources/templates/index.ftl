<html>
<head>
<script type="text/javascript" src="/js/jquery/jquery.min.js"></script>
<script type="text/javascript" src="/js/sockjs-1.1.1.js"></script>
<link ref="styleSheet" href="/css/default.css" /></link>
</head>
<body>

用户Id35：<input type="text" id="userId">
设备Id：<input type="text" id="deviceId">
<br/>
pushToken：<input type="text" id="pushToken">
<button id='connectBtn' onclick="init()">连接</button>
<br/><br/>

to：<input type="text" id="to">
<input type="text" id="input">
<button id='send'>发送</button>

<button id='exit' style="margin-left:50px">退出</button>
<br/><br/>

创建从<input type="text" id="fromCount" style="width: 70px;">到<input type="text" id="toCount" style="width: 70px;">个连接
<button id='testCreateWS' style="margin-left:50px">createWebsockets</button>

<br/>
<button id='onlineSizeBtn' style="margin-left:50px">查询在线数量</button>
<input type="text" id="onlineSizeInput" style="width: 70px;">

<br/><br/>
创建<input type="text" id="threadCount" style="width: 70px;">个线程，每个线程发送<input type="text" id="everyCount" style="width: 70px;">次
<input type="text" id="batchContent">
<button id='batchSendBtn' style="margin-left:50px">批量发送</button>
<button id='receiveSize' style="margin-left:50px">接收了多少</button>
<input type="text" id="receiveSizeInput" style="width: 70px;">
<button id='clearLog' style="margin-left:50px">清空日志</button>

<div id="log">
    <ol>

    </ol>
</div>

<script type="text/javascript">
    /*var $=function(id){
    return document.getElementById(id);
}*/
    $(document).ready(function(){

    });
    var sockws;
    function init() {
        var userId = $('#userId').val();
        var deviceId = $('#deviceId').val();
        var pushToken = $('#pushToken').val();
        if(pushToken){
            sockws = new SockJS('/login?pushToken='+pushToken);
            //sockws = new WebSocket("ws://192.168.85.9:8080/websocket/login?pushToken="+pushToken);
        }else{
            sockws = new SockJS('/login?userId=' + userId + '&deviceId=' + deviceId);
            //sockws = new WebSocket("ws://192.168.85.9:8080/websocket/login?userId=" + userId + "&deviceId=" + deviceId);
        }

        sockws.onopen = function(e) {
            //alert('open');
        };
        sockws.onmessage = function(e) {
            //alert(e.data);
            output(e.data);
        };
        sockws.onclose = function(e) {
            alert('close');
            console.log(e);
        };
        sockws.onheartbeat = function(e) {
            console.log('heartbeat...'+ e.data);
        };

    }
    $("#exit").click(function(){
        sockws.close();
    });

    $("#testCreateWS").click(function(){
        var fromCount = $('#fromCount').val();
        var toCount = $('#toCount').val();
        var data = {fromCount:parseInt(fromCount), toCount:parseInt(toCount)};
        $.ajax({
            url:"/createWebsocket",
            type:"GET",
            data: data,
            dataType: "json",
            success: function(data){
                if(data == '1'){
                    console.info('调用成功');
                }else{
                    console.info('调用失败');
                }
            }
        });
    });

    $("#onlineSizeBtn").click(function(){
        $.ajax({
            url:"/getSessionSize",
            type:"GET",
            //data: data,
            dataType: "json",
            success: function(data){
                $("#onlineSizeInput").val(data);
            }
        });
    });
    $("#batchSendBtn").click(function(){
        var data = {threadCount:$('#threadCount').val(),everyCount:$('#everyCount').val(),
            content:$('#batchContent').val(),testUser:$('#to').val()}
        $.ajax({
            url:"/batchSend",
            type:"POST",
            data: data,
            dataType: "json",
            success: function(data){
                console.info('批量发送完成.');
            }
        });
    });

    $("#receiveSize").click(function(){
        var size = $('#log ol li').length;
        $("#receiveSizeInput").val(size);
    });
    $("#clearLog").click(function(){
        $('#log ol').empty();
    });

    $("#send").click(function(){
        var to = $('#to').val();
        if(!to){
            return;
        }
        var from  = $('#currentUserSpan').html();
        var content  = $('#input').val();
        var msg = {
            "to": to,
            "deviceToken": "xxx",
            "topic": "XXX",
            "data": {
                "notification": {
                    "title": "张三",
                    "alert": content,
                    "badge": 1,
                    "sound": "default(缺省值)/none/XXX",
                    "extras": {
                        "KEY": "VALUE"
                    }
                }
            }
        };

        $.ajax({
            url:"/send",
            type:"POST",
            data: {commandInfo:JSON.stringify(msg)},
            dataType: "json",
            success: function(data){
                if(data){
                    console.log('发送成功');
                }else{
                    console.log('发送失败');
                }
            }
        });
    });


    function output(str) {
        //$('#log').html(str + "<br>" + $('#log').html());
        console.log(str);
        $('#log ol').append('<li>' + str + '</li>');
    }
</script>
</body>
</html>