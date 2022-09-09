const switchPage = (page) => {
    switch (page.toLowerCase()) {
        case "login":
            document.getElementById("content").innerHTML = `
                <div class="title-div">
                    <h2>Login</h2>
                </div>
                <div id="login-canvas" class="login-canvas">
                    <form id="login-form">
                        <div class="user-div">
                            <label>Username</label>
                            <input id="user" type="text" class="login-username">
                        </div>
                        <div class="pass-div">
                            <label>Password</label>
                            <input id="pass" type="password" class="login-password">
                        </div>
                        <div class="login-buttons">
                            <button type="submit" id="login-button">Submit</button>
                            <button type="button" id="register-button">Create account</button>
                        </div>
                        <div class="change-log">
                            <p>Change Log</p>
                        </div>
                    </form>
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