let u;

const onLoginMessage = (payload) => {
    const message = JSON.parse(payload.body);
    switch (message.args[0]) {
        case "success":
            loggedInUser = message.args[2];
            username = message.args[3]
            schemeId = message.args[1]
            loadChatSelector(loggedInUser);
            // subscribe to the chat selector
            console.log("/chat-selector/" + session)
            stompClient.subscribe("/chat-selector/" + session, (payload) => {
                onChatMessage(payload)
            })
            break;
        case "incorrect":
            document.getElementById("login-status").innerHTML = "Username or password is incorrect.";
            break;
        case "no-account":
            document.getElementById("login-status").innerHTML = "No account found with username: " + u;
            break;
        case "cannot-create":
            document.getElementById("login-status").innerHTML = "Account name " + u + " is already taken!";
            break;
    }
}

document.getElementById("login-btn").addEventListener("click", (event) => {
    event.preventDefault();

    u = document.getElementById("username").value;
    const p = document.getElementById("password").value;

    if (u === "" || p === "") {
        document.getElementById("login-status").innerHTML = "Username and password must be filled in.";
        return;
    }

    fetch("http://ip-api.com/json/")
        .then(res => res.json()).then(data => {
        stompClient.send("/app/login",
            {},
            JSON.stringify({
                group: "core",
                label: "login",
                senderId: 0,
                timeSent: new Date(),
                args: [
                    u,
                    CryptoJS.SHA256(
                        u + p
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

document.getElementById("create-acc").addEventListener("click", (event) => {
    event.preventDefault();

    u = document.getElementById("username").value;
    const p = document.getElementById("password").value;

    if (u === "" || p === "") {
        document.getElementById("login-status").innerHTML = "Username and password must be filled in.";
        return;
    }
    if (p.length <= 4) {
        document.getElementById("login-status").innerHTML = "Password must be longer than 4 digits";
        return;
    }
    if (p.toLowerCase() === p) {
        document.getElementById("login-status").innerHTML = "Password must contain at least 1 capital";
        return;
    }

    fetch("http://ip-api.com/json/")
        .then(res => res.json()).then(data => {
        stompClient.send("/app/login", {}, JSON.stringify({
            group: "core",
            label: "createAcc",
            senderId: 0,
            timeSent: new Date(),
            args: [
                u,
                CryptoJS.SHA256(
                    u + p
                ).toString(),
                data.lon,
                data.lat,
                session
            ]
        }))
    })
})

document.getElementById("forgot-pass").addEventListener("click", (event) => {
    event.preventDefault();
    document.getElementById("login-status").innerHTML = "Not implemented! Bro... remember your passwords....";
})