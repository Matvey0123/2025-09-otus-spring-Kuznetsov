const urlString = window.location.search;
const urlParams = new URLSearchParams(urlString);
const bookId = urlParams.get('bookId');

fetch(`/api/v1/book/${bookId}/comment`)
                .then(response => response.json())
                .then(comments => {
                    const tableBody = document.getElementById('tableBody');
                    comments.forEach(comment => {
                        const row = document.createElement('tr');
                        row.innerHTML = `
                            <td>${comment.text}</td>
                        `;
                        tableBody.appendChild(row);
                    });
                })