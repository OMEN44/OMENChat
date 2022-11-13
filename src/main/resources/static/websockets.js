let stompClient, session, chatId;
let connected = false;
let loggedInUser = null;

const serverEndPoint = 'http://localhost:4444/omen-chat';

//connect to the websocket
const connect = () => {
    stompClient = Stomp.over(new SockJS(serverEndPoint))
    stompClient.connect(
        {},
        onConnected,
        () => {
            document.getElementById('status-message').innerHTML = 'Disconnected. Click to reconnect.'
            connected = false;
            loggedInUser = null
        }
    )
}

//when connecting
const onConnected = () => {
    //get session Id
    let url = stompClient.ws._transport.url;
    url = url.replace(serverEndPoint.replace("http", "ws") + "/", "");
    url = url.replace("/websocket", "");
    url = url.replace(/^[0-9]+\//, "");
    session = url;

    //user shouldn't be connected to a chat yet

    /*stompClient.subscribe('/chat', (payload) => {
        const message = JSON.parse(payload.body);
        newMessage(message.timeSent, message.senderId, message.args)
    })*/
    //connect to login endpoint
    stompClient.subscribe("/login/" + session, (payload) => {
        onLoginMessage(payload)
    })

    //display connected message
    const status = document.getElementById('status-message')
    status.innerHTML = 'connected!'
    connected = true;
}

const sendJson = (endpoint, json) => {
    stompClient.send(
        endpoint,
        {},
        JSON.stringify(json)
    )
}