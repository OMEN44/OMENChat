const switchPage = (page) => {
    switch (page.toLowerCase()) {
        case "login":
            document.getElementById("chat-selector").className = "identifier hide"
            document.getElementById("login").className = "identifier"
            document.getElementById("chat").className = "identifier hide"
            break;
        case "chat":
            document.getElementById("chat-selector").className = "identifier hide"
            document.getElementById("login").className = "identifier hide"
            document.getElementById("chat").className = "identifier"
            break;
        case "selector":
            document.getElementById("chat-selector").className = "identifier"
            document.getElementById("login").className = "identifier hide"
            document.getElementById("chat").className = "identifier hide"
            break;
        default:
            console.log("default")
    }
}