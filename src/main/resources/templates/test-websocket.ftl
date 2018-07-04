<!DOCTYPE HTML>
<html>
   <head>
   <meta charset="utf-8">
   <title>websocket test</title>
    
      <script type="text/javascript">
          var ws;
         function webSocketTest()
         {
            var url = document.getElementById('url').value;
            if ("WebSocket" in window)
            {
               // 打开一个 web socket
//               ws = new WebSocket("wss://localhost:8443/eimapp/location?token="+token);
               ws = new WebSocket(url);

               ws.onopen = function()
               {
                  // Web Socket 已连接上，使用 send() 方法发送数据
                  console.log("onopen...");
               };
                
               ws.onmessage = function (evt) 
               { 
                  var received_msg = evt.data;
                  console.log("数据已接收...");
                  document.getElementById('responseMessage').innerText = JSON.stringify(received_msg, null, 4);
               };
                
               ws.onclose = function()
               { 
                  // 关闭 websocket
                  console.log("连接已关闭..."); 
               };
            }
            else
            {
               // 浏览器不支持 WebSocket
               console.log("您的浏览器不支持 WebSocket!");
            }
         }

          function sendMessage(){
              var message = document.getElementById('message').value;
              ws.send(message);
          }
      </script>
        
   </head>
   <body>

      <div>
         url: <input type="text" id="url" style="width: 500px"/>
          <button onclick="webSocketTest()">连接</button>
      </div>
      <br/>
      <div>
          request: <textarea id="message" cols="80" rows="20"></textarea>
      </div>
      <div>
          response: <textarea id="responseMessage" cols="80" rows="20"></textarea>
      </div>
      <div>
          <button onclick="sendMessage()">发送</button>
      </div>
   </body>
</html>