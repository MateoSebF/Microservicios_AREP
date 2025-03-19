const API_URL = "http://localhost:8080"; // Cambia esto si el backend está en otro dominio

function login(){
    window.location.href='/oauth2/authorization/cognito'
}

// Login User
async function loginUser() {
    const email = document.getElementById("loginEmail").value;
    const password = document.getElementById("loginPassword").value;

    const response = await fetch(`${API_URL}/users/login`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ email, password })
    });

    if (response.ok) {
        const data = await response.json();
        console.log(data);
        localStorage.setItem("token", data.id); // Guarda el token
        window.location.href = "home.html"; // Redirige a home
    } else {
        alert("Invalid credentials.");
    }
}

// Register User
async function registerUser() {
    const username = document.getElementById("username").value;
    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;

    const response = await fetch(`${API_URL}/users`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ username, email, password })
    });

    if (response.ok) {
        alert("User registered! You can now log in.");
        window.location.href = "index.html";
    } else {
        alert("Error registering user.");
    }
}

// Logout User
function logout() {
    localStorage.removeItem("token");
    window.location.href = "index.html";
}


// Crear un nuevo Stream
async function createStream() {
    const title = document.getElementById("streamTitle").value;
    const token = localStorage.getItem("token");

    if (!token) {
        alert("You need to log in first.");
        return;
    }

    const response = await fetch(`${API_URL}/streams`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`
        },
        body: JSON.stringify({ title })
    });

    if (response.ok) {
        alert("Stream created!");
        loadStreams();
    } else {
        alert("Error creating stream.");
    }
}

// Cargar Streams y sus Posts
async function loadStreams() {
    const response = await fetch(`${API_URL}/streams`);
    const streams = await response.json();

    const streamContainer = document.getElementById("streams");
    streamContainer.innerHTML = "";

    streams.forEach(stream => {
        const streamElement = document.createElement("div");
        streamElement.classList.add("border-top");
        streamElement.classList.add("my-1");
        streamElement.classList.add("stream");
        streamElement.innerHTML = `
            <div class="col-11 my-4 mx-auto d-flex flex-column">
                <h3>${stream.title}</h3>
                <div id="posts-${stream.id}" class="posts"></div>
                <div class="border-top d-flex flex-column">
                    <textarea class="special-text-area my-3 " id="postContent-${stream.id}" placeholder="Write something..."></textarea>
                    <button class="btn btn-secondary ml-auto col-2" onclick="createPost('${stream.id}')">Post</button>
                </div>
            </div>
        `;

        streamContainer.appendChild(streamElement);
        loadPosts(stream.id);
    });
}

// Crear un Post dentro de un Stream
async function createPost(streamId) {
    const content = document.getElementById(`postContent-${streamId}`).value;
    const token = localStorage.getItem("token");

    if (!token) {
        alert("You need to log in first.");
        return;
    }

    const response = await fetch(`${API_URL}/streams/${streamId}/posts`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`
        },
        body: JSON.stringify({ 
            userId: token,
            content: content,
            timestamp: new Date().toISOString
        })
    });

    if (response.ok) {
        alert("Post created!");
        loadPosts(streamId);
    } else {
        alert("Error creating post.");
    }
}

async function loadPosts(streamId) {
    console.log("Loading posts for stream", streamId);
    const response = await fetch(`${API_URL}/streams/${streamId}/posts`);
    let posts = await response.json();

    // Ordenar los posts por timestamp (mas viejo primero)
    posts.sort((a, b) => a.timestamp - b.timestamp);

    const postContainer = document.getElementById(`posts-${streamId}`);
    postContainer.innerHTML = "";

    for (const post of posts) {
        console.log(post);

        // Obtener información del usuario
        const userResponse = await fetch(`${API_URL}/users/id/${post.userId}`);
        const userData = await userResponse.json();

        // Crear post
        const postElement = document.createElement("div");
        postElement.classList.add("post", "py-3", "border-top");

        // Formatear timestamp
        const titlePost = document.createElement("h6");
        titlePost.classList.add("text-muted");
        const timestamp = new Date(post.timestamp * 1000);
        titlePost.innerHTML = `<strong>@${userData.username}</strong> - ${timestamp.toLocaleString()}`;

        const contentPost = document.createElement("p");
        contentPost.innerHTML = post.content;

        postElement.appendChild(titlePost);
        postElement.appendChild(contentPost);
        postContainer.appendChild(postElement);
    }
}
// Load posts when page loads (only for home.html)
if (window.location.pathname.endsWith("home.html")) {
    const getInfo = async () => {
        const welcome = document.getElementById("welcomeMessage");
        const token = localStorage.getItem("token");
        const user = await fetch(`${API_URL}/users/id/${token}`);
        const userData = await user.json();
        welcome.innerHTML = `Welcome, ${userData.username}!`;
    }
    getInfo();
    window.onload = loadStreams;
}


document.addEventListener("DOMContentLoaded", function () {
    const form = document.getElementById("streamMake");
    const offset = form.getBoundingClientRect().top; // Distancia inicial desde la parte superior

    window.addEventListener("scroll", function () {
        let scrollY = window.scrollY;
        form.style.position = "fixed"; // Se mantiene fijo en la pantalla
        form.style.top = `${Math.max(offset, 20)}px`; // Mantiene la distancia inicial (mínimo 20px)
    });
});
