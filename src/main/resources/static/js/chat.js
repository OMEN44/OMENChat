const sendMessage = () => {
    const messageInput = document.getElementById('input-bar')
    const messageContent = messageInput.value.trim()

    if (messageContent && stompClient) {
        const chatMessage = {
            sender: loggedInUser,
            content: messageInput.value,
            time: 1
        }
        stompClient.send("/app/chat", {}, JSON.stringify(chatMessage))
        messageInput.value = ''
    }
}

const messagePressed = (message) => {
    console.log(message.id);
}

const newMessage = (messageId, sender, time, content) => {
    let html = document.getElementById("message-canvas").innerHTML
    let messageClass = "message fade-in";
    if (loggedInUser === sender)
        messageClass += " from-user";
    html += `
            <div class="message-block">
                <div id="${messageId}" class="${messageClass}" onclick="messagePressed(this)">
                    <p>${sender} | ${time}</p>
                    <div class="divider"></div>
                    <p>${content}</p>
                </div>
            </div>`;
    document.getElementById("message-canvas").innerHTML = html;
    document.getElementById(messageId).scrollIntoView();
}