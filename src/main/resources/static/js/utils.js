let lightModeBool = true;

const lightMode = () => {
    if (lightModeBool) {
        document.body.style.setProperty('--background',"#ffffff")
        document.body.style.setProperty('--text-colour',"#ffffff")
        document.body.style.setProperty('--primary-colour',"#18426c")
        document.body.style.setProperty('--secondary-colour',"#006ba4")
        document.body.style.setProperty('--message-colour',"#0d344f")
        document.body.style.setProperty('--message-hover-colour',"#082d3d")
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

/* Toggle between adding and removing the "responsive" class to nav when the user clicks on the icon */
function myFunction() {
    const topNav = document.getElementById("topNav");
    if (topNav.className === "nav") {
        topNav.className += " responsive";
    } else {
        topNav.className = "nav";
    }
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
