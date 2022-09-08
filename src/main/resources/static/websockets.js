'use strict'

let stompClient
let username

const connect = () => {
    username = loggedInUser;
    if (username) {
        stompClient = Stomp.over(new SockJS('http://localhost:4444/omen-chat'))
        stompClient.connect(
            {},
            onConnected,
            () => document.getElementById('status-message').innerHTML = 'Disconnected.'
        )
    }
}

const onConnected = () => {
    stompClient.subscribe('/topic/public', onMessageReceived)
    stompClient.send("/app/chat.newUser",
        {},
        JSON.stringify({sender: loggedInUser, type: 'CONNECT'})
    )
    const status = document.getElementById('status-message')
    status.innerHTML = 'Websocket is successfully connected!'
}

const sendMessage = (event) => {
    event.preventDefault();
    const messageInput = document.getElementById('input-bar')
    const messageContent = messageInput.value.trim()

    if (messageContent && stompClient) {
        const chatMessage = {
            sender: username,
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