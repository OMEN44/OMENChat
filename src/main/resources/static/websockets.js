let stompClient
let loggedInUser = "Huon";

const connect = () => {
    if (loggedInUser) {
        stompClient = Stomp.over(new SockJS('http://localhost:4444/omen-chat'))
        stompClient.connect(
            {},
            onConnected,
            () => document.getElementById('status-message').innerHTML = 'Disconnected. Click to reconnect.'
        )
    }
}

const onConnected = () => {
    stompClient.subscribe('/topic/public', onMessageReceived)
    stompClient.send("/app/ping",
        {},
        JSON.stringify({
            label: "ping"
        })
    )
    const status = document.getElementById('status-message')
    status.innerHTML = 'connected!'
}

const sendMessage = (event) => {
    event.preventDefault();
    const messageInput = document.getElementById('input-bar')
    const messageContent = messageInput.value.trim()

    if (messageContent && stompClient) {
        const chatMessage = {
            sender: loggedInUser,
            content: messageInput.value,
            type: 'CHAT',
            time: 1
        }
        stompClient.send("/app/chat.send", {}, JSON.stringify(chatMessage))
        messageInput.value = ''
    }

}

const onMessageReceived = (payload) => {
    const message = JSON.parse(payload.body);
    console.log(message)

    if (message.args[0] === "R0") {
        console.log("ping: " + message.timeSent);
    }

    switch (message.type) {
        case 'CONNECT':
            console.log("new user connected")
            break;
        case 'DISCONNECT':
            console.log("user disconnected")
            break;
        case 'CHAT':
            newMessage(message.sender + message.time, message.sender, message.time, message.content)
            const chat = document.getElementById("message-canvas");
            chat.scrollTop = chat.scrollHeight;
            break;
    }

}