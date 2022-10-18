let stompClient, session;
let connected = false;
let loggedInUser = null;

const connect = () => {
    stompClient = Stomp.over(new SockJS('http://localhost:4444/omen-chat'))
    stompClient.connect(
        {},
        onConnected,
        () => {
            document.getElementById('status-message').innerHTML = 'Disconnected. Click to reconnect.'
            connected = false;
        }
    )
}

const onConnected = () => {
    let url = stompClient.ws._transport.url;
    url = url.replace("ws://localhost:4444/omen-chat/", "");
    url = url.replace("/websocket", "");
    url = url.replace(/^[0-9]+\//, "");
    console.log("Your current session is: " + url);
    session = url;

    stompClient.subscribe('/chat', (payload) => {
        const message = JSON.parse(payload.body);
        newMessage(message.timeSent, message.senderId, message.args)
    })
    stompClient.subscribe("/login/" + session, (payload) => {
        onLoginMessage(payload)
    })
    stompClient.send("/app/system",
        {},
        JSON.stringify({
            label: "ping"
        })
    )
    const status = document.getElementById('status-message')
    status.innerHTML = 'connected!'
    connected = true;
}

const onMessageReceived = (payload) => {

    const message = JSON.parse(payload.body);

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