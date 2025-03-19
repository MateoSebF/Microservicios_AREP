const API_URL = "https://backendeci.duckdns.org:8080";

// Redirige al login de Cognito
function login() {
    window.location.href = '/oauth2/authorization/cognito';
}

function logout() {
    fetch(API_URL+"/logout", { 
        method: "GET", 
        credentials: "include" 
    })
    .then(response => {
        // Como el backend hace un redirect, la respuesta no vendrá como JSON, pero podemos verificar si fue exitosa
        if (response.redirected) {
            window.location.href = response.url; // Redirige a la URL de Cognito/logout
        } else {
            console.error("Logout failed");
            alert("Error logging out.");
        }
    })
    .catch(error => console.error("Logout error:", error));
}



// Crear un nuevo Stream
async function createStream() {
    const title = document.getElementById("streamTitle").value;

    const response = await fetch(`${API_URL}/streams`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({ title }),
        credentials: "include" // Enviar cookies (JSESSIONID)
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
    const response = await fetch(`${API_URL}/streams`, { credentials: "include" });
    const streams = await response.json();

    const streamContainer = document.getElementById("streams");
    streamContainer.innerHTML = "";

    streams.forEach(stream => {
        const streamElement = document.createElement("div");
        streamElement.classList.add("border-top", "my-1", "stream");
        streamElement.innerHTML = `
            <div class="col-11 my-4 mx-auto d-flex flex-column">
                <h3>${stream.title}</h3>
                <div id="posts-${stream.id}" class="posts"></div>
                <div class="border-top d-flex flex-column">
                    <textarea class="special-text-area my-3" id="postContent-${stream.id}" placeholder="Write something..."></textarea>
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
    console.log("Creating post for stream", streamId);
    const content = document.getElementById(`postContent-${streamId}`).value;
    const userResponse = await fetch(`${API_URL}/users/me`, { credentials: "include" });
    const userData = await userResponse.json();
    const userId = userData.id;

    const response = await fetch(`${API_URL}/streams/${streamId}/posts`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({ userId: userId, content: content, timestamp: new Date().toISOString() }),
        credentials: "include" // Enviar cookies (JSESSIONID)
    });

    if (response.ok) {
        alert("Post created!");
        loadPosts(streamId);
    } else {
        alert("Error creating post.");
    }
}

// Cargar Posts
async function loadPosts(streamId) {
    console.log("Loading posts for stream", streamId);
    const response = await fetch(`${API_URL}/streams/${streamId}/posts`, { credentials: "include" });
    let posts = await response.json();

    // Ordenar los posts por timestamp (más viejo primero)
    posts.sort((a, b) => new Date(a.timestamp) - new Date(b.timestamp));

    const postContainer = document.getElementById(`posts-${streamId}`);
    postContainer.innerHTML = "";

    for (const post of posts) {
        console.log(post);

        const userResponse = await fetch(`${API_URL}/users/id/${post.userId}`, { credentials: "include" });
        const userData = await userResponse.json();

        // Crear post
        const postElement = document.createElement("div");
        postElement.classList.add("post", "py-3", "border-top");

        // Formatear timestamp
        const titlePost = document.createElement("h6");
        titlePost.classList.add("text-muted");
        const timestamp = new Date(post.timestamp*1000);
        titlePost.innerHTML = `<strong>@${userData.username}</strong> - ${timestamp.toLocaleString()}`;

        const contentPost = document.createElement("p");
        contentPost.innerHTML = post.content;

        postElement.appendChild(titlePost);
        postElement.appendChild(contentPost);
        postContainer.appendChild(postElement);
    }
}

// Obtener usuario autenticado
async function getUserInfo() {
    const welcome = document.getElementById("welcomeMessage");

    try {
        const response = await fetch(`${API_URL}/users/me`, { credentials: "include" });
        if (!response.ok) throw new Error("User not authenticated");

        const userData = await response.json();
        welcome.innerHTML = `Welcome, ${userData.username}!`;
    } catch (error) {
        console.error("Error fetching user info:", error);
        welcome.innerHTML = "Not logged in";
    }
}

// Cargar datos al iniciar
if (window.location.pathname.endsWith("home.html")) {
    getUserInfo();
    window.onload = loadStreams;
}

// Hacer que el formulario de crear stream sea fijo al hacer scroll
document.addEventListener("DOMContentLoaded", function () {
    const form = document.getElementById("streamMake");
    const offset = form.getBoundingClientRect().top; // Distancia inicial desde la parte superior

    window.addEventListener("scroll", function () {
        let scrollY = window.scrollY;
        form.style.position = "fixed"; // Se mantiene fijo en la pantalla
        form.style.top = `${Math.max(offset, 20)}px`; // Mantiene la distancia inicial (mínimo 20px)
    });
});

