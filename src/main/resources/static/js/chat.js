/*
 * To send a message to this endpoint the following labels can be used:
 *  - sendMessage
 *  - getMessages
 *
 * The arguments must be as follows:
 *  0: target (chat id)
 *  1: content
 *  2: message id
 *  3: date sent
 *  4: sender name (for display)
 *  5: session
 * -----or-----
 *  0: message
 *  1: ...
 */

//switch statement for handling the received messages
const onChatMessage = (payload) => {
    if (payload.label != null) {
        let html = "";
        switch (payload.label) {
            case "sendMessage":
                html = document.getElementById("message-canvas").innerHTML
                let messageClass = "message";
                if (loggedInUser === payload.senderId.toString())
                    messageClass += " from-user";

                html += `
                    <div class="message-block">
                        <div id="${payload.args[2]}" class="${messageClass}" onclick="messagePressed(this)">
                            <p>${payload.args[4]} | ...</p>
                            <div class="divider"></div>
                            <p>${payload.args[1]}</p>
                        </div>
                    </div>`;
                document.getElementById("message-canvas").innerHTML = html;
                document.getElementById(payload.args[2]).scrollIntoView();
                break;
            case "getMessages":
                html = ""
                for (const m of payload.args) {
                    let messageClass = "message";
                    if (u === m[4])
                        messageClass += " from-user";

                    html = `
                    <div class="message-block">
                        <div id="${m[2]}" class="${messageClass}" onclick="messagePressed(this)">
                            <p>${m[4]} | ...</p>
                            <div class="divider"></div>
                            <p>${m[1]}</p>
                        </div>
                    </div>` + html
                }
                document.getElementById("message-canvas").innerHTML = html;
                // document.getElementById(payload.args[payload.args.length - 1][2]).scrollIntoView();
                let canvas = document.getElementById("message-canvas")
                canvas.scrollTop = canvas.scrollHeight;
                break;
        }
    }
}

//loader function to be called when switching to the page
const loadChat = (id, name) => {
    chatId = id;
    switchPage("chat");
    setTitle(name);
    stompClient.subscribe("/chat/" + session, (payload) => {
        onChatMessage(JSON.parse(payload.body));
    }, {id: chatId})
    sendToChat("getMessages", null)
}

//event listeners for buttons
document.getElementById("exit-chat").addEventListener("click", (event) => {
    event.preventDefault();
    sendToChat("exit", null);
    stompClient.unsubscribe(chatId);
    loadChatSelector();
});

document.getElementById("invite-button").addEventListener('click', (event) => {
    event.preventDefault();
    sendToChatSelector("invite", chatId, document.getElementById("invite").value, null)
})

document.getElementById("send").addEventListener('click', (event) => {
    event.preventDefault();
    const messageContent = document.getElementById('input-bar').value.trim()
    const d = new Date();
    let time = d.getHours() + ":" + d.getMinutes() + " AM"
    if (d.getHours() > 12)
        time = d.getHours() - 12 + ":" + d.getMinutes() + " PM"

    if (messageContent && stompClient) {
        sendToChat("sendMessage", messageContent)
    }
    document.getElementById("input-bar").value = "";
})

const messagePressed = (message) => {
    console.log(message.id);
}

//template message function
const sendToChat = (label, content) => {
    sendJson("/app/chat", {
        group: "core",
        label: label,
        senderId: loggedInUser,
        timeSent: new Date(),
        args: [
            chatId,
            content,
            null, // message id is determined server side

            session
        ]
    })
}



