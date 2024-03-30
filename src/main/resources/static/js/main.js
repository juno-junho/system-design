console.log("started!!!!"); // todo 제거 예정

document.getElementById("shortenBtn").addEventListener("click", function() {
    console.log("butten clicked !!!!"); // todo 제거 예정
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
            console.log("data: ", data); // todo 제거 예정
            const displayArea = document.getElementById("shortenedUrl");
            if (data.shortenUrl) {
                displayArea.innerHTML = `<p class="text-success">Shortened URL: <a href="juno.com/${data.shortenUrl}" target="_blank">juno.com/${data.shortenUrl}</a></p>`; // todo url 변경 예정
            } else {
                displayArea.innerHTML = "<p class='text-danger'> 유효하지 않은 URL입니다.</p>";
            }
        })
        .catch(error => {
            console.error('Error:', error);
            document.getElementById("shortenedUrl").innerHTML = "<p class='text-danger'>Error: Could not process your request.</p>";
        });
});
