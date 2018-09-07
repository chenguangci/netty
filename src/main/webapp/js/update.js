document.write("<script language='javascript' src='/js/jquery-1.10.2.min.js'></script>");
function updateList() {
    var list = $('#char_list');
    list.empty();
    $.get('/updateList', function (result) {
        var data = result.data;
        for (var j in data) {
            console.log(data[j]);
            var div = $("<div>");
            var form = $("<form>"); form.attr("onsubmit", "return false");
            var label = $("<label>"); label.text("发送给" + data[j].toId + "：");
            var input = $("<input>"); input.attr("id", j); input.attr("type", "text");
            form.append(label);
            form.append(input);
            form.append('<input type="button" value="发送消息" ' +
                'onclick="send(' + data[j].fromId + ',' + j + ',' + data[j].toId + ')">');
            div.append(form);
            list.append(div)
        }
    });
}
function send(fromId, id, toId) {
    var message = document.getElementById(id);
    console.log('................'+message.value);
    stompClient.send("/beiyi/welcome", {}, JSON.stringify({'fromId' : fromId, 'message' : message.value, 'toId' : toId }));
}