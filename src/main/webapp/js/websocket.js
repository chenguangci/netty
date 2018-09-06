var webSocket;
function send(message) {
    if (!window.webSocket) {
        return;
    }
    if (webSocket.readyState === WebSocket.OPEN) {
        webSocket.send(message + '|' + '1')
    } else {
        alert('webSocket链接没有建立')
    }
}

//开启连接
function openWebSocket() {
    if (!window.WebSocket) {
        window.WebSocket = window.MozWebSocket;
    }
    if (window.WebSocket) {
        webSocket = new WebSocket("ws://localhost:8080/change-notice");
        webSocket.onmessage = function (event) {
            var ta = document.getElementById('responseContext');
            ta.value += event.data + '\r\n'
        };
        webSocket.onopen = function (event) {
            var ta = document.getElementById('responseContext');
            ta.value = "您当前的浏览器支持websocket\r\n";
        };
        webSocket.onclose = function (event) {
            var ta = document.getElementById('responseContext');
            ta.value = "";
            ta.value = "链接已经关闭"
        }
    } else {
        alert("您的浏览器不支持webSocket")
    }
}