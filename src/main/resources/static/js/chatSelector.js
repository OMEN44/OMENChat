const onChatMessage = (payload) => {
    let message = JSON.parse(payload.body);
    switch (message.label) {
        case "chatExists":
            document.getElementById("status").innerHTML = `<p>This chat already exists!</p>`
            document.getElementById("chat-input").value = "";
            break;
        case "showChats":
            if (message.args.length !== 0) {
                let html = "";
                for (let i = 0; i < message.args.length; i++) {
                    let buttons = `<button data="${message.args[i].chatId}" class="join-chat button">Join</button>
                                   <button data="${message.args[i].chatId}" class="leave-chat button">Leave Chat</button>`
                    if (message.args[i].ownerId.toString() === loggedInUser) {
                        buttons = buttons + `<button data="${message.args[i].chatId}" class="delete-chat button">Delete Chat</button>`
                    }
                    html = html +`<div class="chat-card">
                            <div class="chat-top">
                                <div class="chat-top-left">
                                    <div class="chat-name">
                                        <p>${message.args[i].chatName}</p>
                                    </div>
                                    <div class="chat-online">
                                        <p data="${message.args[i].chatId}" id="online">online: 12</p>
                                    </div>
                                </div>
                                <div class="chat-top-right">
                                    ${buttons}
                                </div>
                            </div>
                            <div class="chat-description">
                                <p>${message.args[i].description}</p>
                            </div>
                        </div>
                    `
                }
                document.getElementById("chats-canvas").innerHTML = html;
                for(let e of document.getElementsByClassName("join-chat")) {
                    e.addEventListener("click", (event) => {
                        event.preventDefault();
                        joinChat(event);
                    })
                }
                for(let e of document.getElementsByClassName("leave-chat")) {
                    e.addEventListener("click", (event) => {
                        event.preventDefault();
                        leaveChat(event);
                    })
                }
                for(let e of document.getElementsByClassName("delete-chat")) {
                    e.addEventListener("click", (event) => {
                        event.preventDefault();
                        deleteChat(event);
                    })
                }
            }
            break;
        case "joinChat":
            loadChat(message.args[0], message.args[1], message.args.splice(0, 2))
            break;
    }
}

const loadChatSelector = () => {
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
    console.log(document.getElementById("chat-input").value.length)
    if (document.getElementById("chat-input").value.length >= 31) {
        document.getElementById("status").innerHTML = `<p>Chat name cannot be longer than 30 characters</p>`
        document.getElementById("chat-input").value = "";
        return;
    }
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

const joinChat = (event) => {
    sendJson("/app/selector", {
        group: "core",
        label: "joinChat",
        senderId: loggedInUser,
        timeSent: new Date(),
        args: [
            event.target.getAttribute("data")
        ]
    })
}

const leaveChat = (event) => {
    sendJson("/app/selector", {
        group: "core",
        label: "leaveChat",
        senderId: loggedInUser,
        timeSent: new Date(),
        args: [
            event.target.getAttribute("data")
        ]
    })
}

const deleteChat = (event) => {
    sendJson("/app/selector", {
        group: "core",
        label: "deleteChat",
        senderId: loggedInUser,
        timeSent: new Date(),
        args: [
            event.target.getAttribute("data")
        ]
    })
}