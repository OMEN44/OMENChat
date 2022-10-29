const onLoginMessage = (payload) => {
    const message = JSON.parse(payload.body);
    if (message.args[0] === "success") {
        loggedInUser = message.args[1];
        switchPage("selector");
        let html = document.getElementById("chats-canvas").innerHTML
        for (let i = 0; i < 15; i++) {
            html = html + `
            <div class="chat-card">
                <div class="chat-top">
                    <div class="chat-top-left">
                        <div class="chat-name">
                            <p>Name of chat</p>
                        </div>
                        <div class="chat-online">
                            <p>online: 12</p>
                        </div>
                    </div>
                    <div class="chat-top-right">
                        <button class="button">Join</button>
                        <button class="button">Delete Chat</button>
                    </div>
                </div>
                <div class="chat-description">
                    <p>Description of chat</p>
                </div>
            </div>
            `
        }
        document.getElementById("chats-canvas").innerHTML = html;
    } else {
        //user not logged in
    }
}

document.getElementById("login-btn").addEventListener("click", (event) => {
    event.preventDefault();

    fetch("http://ip-api.com/json/")
        .then(res => res.json()).then(data => {
        stompClient.send("/app/login",
            {},
            JSON.stringify({
                group: "core",
                label: "login",
                senderId: "guest",
                args: [
                    document.getElementById("username").value,
                    CryptoJS.SHA256(
                        document.getElementById("username").value +
                        document.getElementById("password").value
                    ).toString(),
                    data.lon,
                    data.lat,
                    data.city,
                    data.country,
                    session
                ]
            })
        )
    })
})