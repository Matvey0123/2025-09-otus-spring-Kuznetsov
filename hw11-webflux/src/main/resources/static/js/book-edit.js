const urlString = window.location.search;
const urlParams = new URLSearchParams(urlString);
const bookId = urlParams.get('id');

fetch(`/api/v1/book/${bookId}`)
                .then(response => response.json())
                .then(book => {
                    const bookTitleInput = document.getElementById('book-title-input');
                    bookTitleInput.value = book.title;
                });

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
    const urlString = window.location.search;
    const urlParams = new URLSearchParams(urlString);
    const bookId = urlParams.get('id');
    const titleInput = document.getElementById("book-title-input");
    const authorSelect = document.getElementById('author-name-select');
    const genreSelect = document.getElementById('genres-select');
    const selectedOptions = genreSelect.querySelectorAll("option:checked");
    const selectedGenres = Array.from(selectedOptions).map(option => option.value);

    const book = {
        id: bookId,
        title: titleInput.value,
        authorId: authorSelect.value,
        genresIds: selectedGenres
    };

    fetch(`/api/v1/book/${bookId}`, {
                    method: 'PATCH',
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
