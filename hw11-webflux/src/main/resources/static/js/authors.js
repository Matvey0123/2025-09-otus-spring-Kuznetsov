fetch('/api/v1/author')
                .then(response => response.json())
                .then(authors => {
                    const tableBody = document.getElementById('tableBody');
                    authors.forEach(author => {
                        const row = document.createElement('tr');
                        row.innerHTML = `
                            <td>${author.fullName}</td>
                        `;
                        tableBody.appendChild(row);
                    });
                })