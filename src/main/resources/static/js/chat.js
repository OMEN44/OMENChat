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
            senderId: loggedInUser,
            timeSent: time,
            args: [messageInput.value]
        }
        stompClient.send("/app/chat", {}, JSON.stringify(chatMessage))
        messageInput.value = ''
    }
})

const messagePressed = (message) => {
    console.log(message.id);
}

const newMessage = (time, sender, args) => {
    let html = document.getElementById("message-canvas").innerHTML
    let messageClass = "message";
    if (loggedInUser === sender)
        messageClass += " from-user";

    html += `
            <div class="message-block">
                <div id="${args[0]}" class="${messageClass}" onclick="messagePressed(this)">
                    <p>${sender} | ${time}</p>
                    <div class="divider"></div>
                    <p>${args[1]}</p>
                </div>
            </div>`;
    document.getElementById("message-canvas").innerHTML = html;
    document.getElementById(args[0]).scrollIntoView();
}