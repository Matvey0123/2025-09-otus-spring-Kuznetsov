fetch('/api/v1/author')
                .then(response => response.json())
                .then(authors => {
                    const selectElement = document.getElementById('author-name-select');
                    authors.forEach(author => {
                        const newOption = new Option(author.fullName, author.id);
                        selectElement.add(newOption);
                    });
                });

fetch('/api/v1/genre')
                .then(response => response.json())
                .then(genres => {
                    const selectElement = document.getElementById('genres-select');
                    genres.forEach(genre => {
                        const newOption = new Option(genre.name, genre.id);
                        selectElement.add(newOption);
                    });
                });

function saveBook() {
    const titleInput = document.getElementById("book-title-input");
    const authorSelect = document.getElementById('author-name-select');
    const genreSelect = document.getElementById('genres-select');
    const selectedOptions = genreSelect.querySelectorAll("option:checked");
    const selectedGenres = Array.from(selectedOptions).map(option => option.value);

    const book = {
        title: titleInput.value,
        authorId: authorSelect.value,
        genresIds: selectedGenres
    };

    fetch('/api/v1/book', {
                    method: 'POST',
                    headers: {
                      'Accept': 'application/json',
                      'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(book)})
                    .then(rawResponse => {
                        if (!rawResponse.ok) {
                            return rawResponse.json().then(errorDto => { throw new Error(errorDto.text) });
                        }
                        window.location.href = '/';
                    })
                    .catch(error => {
                        alert(error.message);
                    })
}
