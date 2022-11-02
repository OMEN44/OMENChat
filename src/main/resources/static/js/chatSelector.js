const onChatMessage = (payload) => {
    let message = JSON.parse(payload.body);
}

const loadChatSelector = (user) => {
    stompClient.send(
        "/app/selector",
        {},
        JSON.stringify({
            group: "core",
            label: "getChats",
            senderId: loggedInUser,
            args: [session]
        })
    )
    switchPage("selector")
}

document.getElementById("chat-create").addEventListener("click", (event) =>{
    event.preventDefault();
    stompClient.send(
        "/app/selector",
        {},
        JSON.stringify({
            group: "core",
            label: "createChat",
            senderId: loggedInUser,
            timeSent: new Date(),
            args: [
                document.getElementById("chat-input").value
            ]
        })
    )
})

document.getElementById("chat-search").addEventListener("click", (event) => {
    event.preventDefault()
    stompClient.send(
        "/app/selector",
        {},
        JSON.stringify({
            group: "core",
            label: "search",
            senderId: loggedInUser,
            timeSent: new Date(),
            args: [
                document.getElementById("chat-input").value
            ]
        })
    )
})

/*
<div className="chat-card">
    <div className="chat-top">
        <div className="chat-top-left">
            <div className="chat-name">
                <p>Name of chat</p>
            </div>
            <div className="chat-online">
                <p>online: 12</p>
            </div>
        </div>
        <div className="chat-top-right">
            <button className="button">Join</button>
            <button className="button">Delete Chat</button>
        </div>
    </div>
    <div className="chat-description">
        <p>Description of chat</p>
    </div>
</div>*/