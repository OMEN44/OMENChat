const onLoginMessage = (payload) => {
    const message = JSON.parse(payload.body);
    if (message.args[0] === "success") {
        loggedInUser = message.args[1];
        switchPage("chat");
        setTitle("Welcome, " + loggedInUser + "!")
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