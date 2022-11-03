const onChatMessage = (payload) => {
    let message = JSON.parse(payload.body);
    switch (message.label) {
        case "chatExists":
            document.getElementById("status").innerHTML = `<p>This chat already exists!</p>`
            break;
        case "showChats":
            if (message.args.length !== 0) {
                let html = "";
                for (let i = 0; i < message.args.length; i++) {
                    html = html +`<div class="chat-card">
                            <div class="chat-top">
                                <div class="chat-top-left">
                                    <div class="chat-name">
                                        <p>${message.args[i].chatName}</p>
                                    </div>
                                    <div class="chat-online">
                                        <p id="online-${message.args[i].chatId}">online: 12</p>
                                    </div>
                                </div>
                                <div class="chat-top-right">
                                    <button id="join-chat-${message.args[i].chatId}" class="button">Join</button>
                                    <button id="leave-chat-${message.args[i].chatId}" class="button">Leave Chat</button>
                                </div>
                            </div>
                            <div class="chat-description">
                                <p>${message.args[i].description}</p>
                            </div>
                        </div>
                    `
                }
                document.getElementById("chats-canvas").innerHTML = html;
            }
    }
    console.log(message)
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

document.getElementById("chat-create").addEventListener("click", (event) => {
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
                document.getElementById("chat-input").value,
                "description not implemented... oopsy"
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