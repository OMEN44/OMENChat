const loadChat = (id, name, messages) => {
    chatId = id;
    switchPage("chat");
    setTitle("Current chat: " + name);
    let html = ""
    for (const m of messages) {
        let messageClass = "message";
        if (loggedInUser === m.senderId)
            messageClass += " from-user";

        html += `
            <div class="message-block">
                <div id="${m.id}" class="${messageClass}" onclick="messagePressed(this)">
                    <p>${m.senderId} | ...</p>
                    <div class="divider"></div>
                    <p>${m.content}</p>
                </div>
            </div>`
    }
    document.getElementById("message-canvas").innerHTML = html;
    stompClient.subscribe("/chat/" + session, (payload) => {
        const message = JSON.parse(payload.body);
        newMessage(message.args[0], message.timeSent, message.senderId, message.args[1]);
    })
}

document.getElementById("send").addEventListener('click', (event) => {
    event.preventDefault();
    const messageInput = document.getElementById('input-bar')
    const messageContent = messageInput.value.trim()
    const d = new Date();
    let time = d.getHours() + ":" + d.getMinutes() + " AM"
    if (d.getHours() > 12)
        time = d.getHours() - 12 + ":" + d.getMinutes() + " PM"

    if (messageContent && stompClient) {
        const chatMessage = {
            group: "core",
            senderId: loggedInUser,
            timeSent: new Date(),
            args: [messageInput.value, chatId]
        }
        stompClient.send("/app/chat", {}, JSON.stringify(chatMessage))
        messageInput.value = ''
    }
})

const messagePressed = (message) => {
    console.log(message.id);
}

const newMessage = (id, time, sender, content) => {
    let html = document.getElementById("message-canvas").innerHTML
    let messageClass = "message";
    if (loggedInUser.toString() === sender.toString())
        messageClass += " from-user";

    html += `
            <div class="message-block">
                <div id="${id}" class="${messageClass}" onclick="messagePressed(this)">
                    <p>${sender} | ${time}</p>
                    <div class="divider"></div>
                    <p>${content}</p>
                </div>
            </div>`;
    document.getElementById("message-canvas").innerHTML = html;
    document.getElementById(id).scrollIntoView();
}