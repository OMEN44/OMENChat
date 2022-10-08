const switchPage = (page) => {
    switch (page.toLowerCase()) {
        case "login":
            document.getElementById("content").innerHTML = `
                <div class="title-div">
                    <h2>Login</h2>
                </div>
                <form class="login-form">
                    <input id="username" type="text">
                    <input id="password" type="password">
                    <button id="login-btn" type="submit">Login</button>
                    <button id="create-acc" type="button">Create Account</button>
                    <div class="divider" style="background-color: var(--secondary-colour)"></div>
                    <button id="forgot-pass" type="button">Forgot Password</button>
                </form>
                <div class="login-form">
                    <p>Change Log:</p>
                </div>
                `
            break;
        case "chat":
            document.getElementById("content").innerHTML = `
                <div class="title-div">
                    <h2 id="title">Title</h2>
                </div>
                <div id="message-canvas" class="message-canvas">
                
                </div>
                <form id="message-controller" class="input-div" name="message-sender">
                    <input class="message-input" id="input-bar" type="text" autoFocus>
                    <button class="message-button" id="send" type="submit">Send</button>
                </form>
                `

            document.getElementById('message-controller').addEventListener('submit', sendMessage)
            break;
        case "register":

            break;
        default:
            console.log("default")
    }
}