

console.log("started!!!!");

document.getElementById("shortenBtn").addEventListener("click", function() {
    console.log("butten clicked !!!!");
    const urlInput = document.getElementById("urlInput").value;
    fetch("/api/v1/data/shorten", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({ longUrl: urlInput })
    })
        .then(response => response.json())
        .then(data => {
            const displayArea = document.getElementById("shortenedUrl");
            if (data.shortUrl) {
                displayArea.innerHTML = `<p class="text-success">Shortened URL: <a href="${data.shortUrl}" target="_blank">${data.shortUrl}</a></p>`;
            } else {
                displayArea.innerHTML = "<p class='text-danger'> 유효하지 않은 URL입니다.</p>";
            }
        })
        .catch(error => {
            console.error('Error:', error);
            document.getElementById("shortenedUrl").innerHTML = "<p class='text-danger'>Error: Could not process your request.</p>";
        });
});
