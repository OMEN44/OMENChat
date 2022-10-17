let stompClient
let connected = false;
let loggedInUser = null;

/*
* \omen-chat
*  ├───\login
*  ├───\system
*  └───\chat
*       └───\<chat-room-id>
*
*/

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
    stompClient.subscribe('/chat', () => {
        console.log("chat")
    })
    stompClient.subscribe("/login", (payload) => {
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