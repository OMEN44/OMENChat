let lightModeBool = true;

const lightMode = () => {
    if (lightModeBool) {
        document.body.style.setProperty('--background',"#000000")
        document.body.style.setProperty('--text-colour',"#000000")
        document.body.style.setProperty('--primary-colour',"#2d6c18")
        document.body.style.setProperty('--secondary-colour',"#29a400")
        document.body.style.setProperty('--message-colour',"#254f0d")
        document.body.style.setProperty('--message-hover-colour',"#153d08")
        document.getElementById("light-button").innerHTML = "Dark mode";
        lightModeBool = false;
    } else {
        document.body.style.setProperty('--background',"#000000")
        document.body.style.setProperty('--text-colour',"#ffffff")
        document.body.style.setProperty('--primary-colour',"#6C0E18FF")
        document.body.style.setProperty('--secondary-colour',"#a40000")
        document.body.style.setProperty('--message-colour',"#3d080d")
        document.body.style.setProperty('--message-hover-colour',"#4f0d13")
        document.getElementById("light-button").innerHTML = "Light mode";
        lightModeBool = true;
    }
}

const setTitle = (title) => {
    if (title.toString().length > 32) {
        return false;
    }
    document.getElementById("title").innerHTML = title;
    return true;
}

const messagePressed = (message) => {
    console.log(message.id);
}

const newMessage = (messageId, sender, time, content) => {
    let html = document.getElementById("message-canvas").innerHTML
    let messageClass = "message fade-in";
    if (loggedInUser === sender)
        messageClass += " from-user";
    html += `
            <div class="message-block">
                <div id="${messageId}" class="${messageClass}" onclick="messagePressed(this)">
                    <p>${sender} | ${time}</p>
                    <div class="divider"></div>
                    <p>${content}</p>
                </div>
            </div>`;
    document.getElementById("message-canvas").innerHTML = html;
    document.getElementById(messageId).scrollIntoView();
}

const login = (register) => {
    const username = document.getElementById("user").value;
    const password = document.getElementById("pass").value;

    const hashPass = CryptoJS.SHA256(password);

    const loginQuery = {
        type: 'Q',
        command: 1,
        args: [username, hashPass.toString(), register],
        timeSent: "now",
        senderId: null
    }
    stompClient.send("/app/command", {}, JSON.stringify(loginQuery))

    /*fetch('http://localhost:5000/login/' + username)
        .then(response => response.json())
        .then(data => {
            if (data['data'].length === 0) {
                document.getElementById("login-error").innerHTML =
                    `<p style="color: #f44336">This user does not exist</p>`
            } else {
                data['data'].forEach(function ({accessLevel, pass}) {
                    if (accessLevel === 0) {
                        if (pass === password) {
                            toPage("home.html", [`lvl=0`])
                        } else {
                            document.getElementById("login-error").innerHTML =
                                `<p style="color: #f44336">Incorrect user or password</p>`
                        }
                    } else {
                        if (pass === sha256(password)) {
                            toPage("home.html", [`lvl=${accessLevel}`])
                        } else {
                            document.getElementById("login-error").innerHTML =
                                `<p style="color: #f44336">Incorrect user or password</p>`
                        }
                    }
                })
            }
        })*/
}

/*document.getElementById("login-bton").addEventListener("click", (event) => {
    event.preventDefault()
    login(false)
})

document.getElementById("register-button").addEventListener("click", (event) => {
    event.preventDefault()
    login(true)
})*/

document.getElementById("status-div").addEventListener("click", () => {
    let text = document.getElementById("status-message").innerHTML;
    if (text === "Disconnected. Click to reconnect.") {
        connect()
    }
})
