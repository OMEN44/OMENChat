document.getElementById("login-btn").addEventListener("click", (event) => {
    event.preventDefault();
    $.getJSON("https://api.ipify.org?format=json", function(data) {
        stompClient.send("/app/command",
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
                    data.ip
                ]
            })
        )
    })


})