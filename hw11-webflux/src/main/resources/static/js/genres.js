fetch('/api/v1/genre')
                .then(response => response.json())
                .then(genres => {
                    const tableBody = document.getElementById('tableBody');
                    genres.forEach(genre => {
                        const row = document.createElement('tr');
                        row.innerHTML = `
                            <td>${genre.name}</td>
                        `;
                        tableBody.appendChild(row);
                    });
                })