/*
To send a message to this endpoint the following labels can be used:
 - getChats
 - createChat
 - search
 - joinChat
 - leaveChat
 - deleteChat

The arguments must be as follows:
 0: chat id
 1: input bar
 2: description
 3: session
 */

//Handler for receiving payload from server
const onChatSelectorMessage = (payload) => {
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
            loadChat(message.args[0], message.args[1])
            break;
    }
}

//function for loading the chat selector
const loadChatSelector = () => {
    sendToChatSelector("getChats", null, null, null)
    switchPage("selector")
}

//event listeners for buttons
document.getElementById("chat-refresh").addEventListener("click", (event) => {
    event.preventDefault();
    loadChatSelector()
})

document.getElementById("chat-create").addEventListener("click", (event) => {
    event.preventDefault();
    if (document.getElementById("chat-input").value.length >= 31) {
        document.getElementById("status").innerHTML = `<p>Chat name cannot be longer than 30 characters</p>`
        document.getElementById("chat-input").value = "";
        return;
    }
    sendToChatSelector("createChat", null, document.getElementById("chat-input").value,
        "description not implemented... oopsy"
    )
})

document.getElementById("chat-search").addEventListener("click", (event) => {
    event.preventDefault()
    sendToChatSelector("search", null, document.getElementById("chat-input").value, null)
})

const joinChat = (event) => {
    sendToChatSelector("joinChat", event.target.getAttribute("data"), null, null)
}

const leaveChat = (event) => {
    sendToChatSelector("leaveChat", event.target.getAttribute("data"), null, null)
}

const deleteChat = (event) => {
    sendToChatSelector("deleteChat", event.target.getAttribute("data"), null, null)
}

//method to be used when sending messages to the server
const sendToChatSelector = (label, id, input, description) => {
    sendJson("/app/selector", {
        group: "core",
        label: label,
        senderId: loggedInUser,
        timeSent: new Date(),
        args: [
            id,
            input,
            description,
            session
        ]
    })
}