const switchPage = (page) => {
    switch (page.toLowerCase()) {
        case "login":
            document.getElementById("login").className = "identifier"
            document.getElementById("chat").className = "identifier hide"
            break;
        case "chat":
            document.getElementById("login").className = "identifier hide"
            document.getElementById("chat").className = "identifier"
            break;
        case "calender":

            break;
        default:
            console.log("default")
    }
}