fetch('/api/v1/book')
                .then(response => response.json())
                .then(books => {
                    const tableBody = document.getElementById('bookTableBody');
                    books.forEach(book => {
                        const newRow = tableBody.insertRow();

                        const titleCell = newRow.insertCell(0);
                        const authorCell = newRow.insertCell(1);
                        const genresCell = newRow.insertCell(2);
                        const actionCell = newRow.insertCell(3);

                        titleCell.textContent = book.title;
                        authorCell.textContent = book.author.fullName;
                        genresCell.textContent = book.genres.map(genre => genre.name).join(', ');

                        const commentsButton = document.createElement('button');
                        commentsButton.textContent = 'Посмотреть комменатрии';
                        commentsButton.onclick = function() {
                                        window.location.href = "/comments?bookId=" + book.id;
                                    };

                        const editButton = document.createElement('button');
                        editButton.textContent = 'Изменить';
                        editButton.onclick = function() {
                                        window.location.href = "/books/edit?id=" + book.id;
                                    };

                        const deleteButton = document.createElement('button');
                        deleteButton.textContent = 'Удалить';
                        deleteButton.onclick = function() {
                                        fetch(`/api/v1/book/${book.id}`, {
                                                        method: 'DELETE'
                                                        })
                                                        .then(rawResponse => window.location.href = '/')
                                    };

                        actionCell.appendChild(commentsButton);
                        actionCell.appendChild(editButton);
                        actionCell.appendChild(deleteButton);
                    });
                });