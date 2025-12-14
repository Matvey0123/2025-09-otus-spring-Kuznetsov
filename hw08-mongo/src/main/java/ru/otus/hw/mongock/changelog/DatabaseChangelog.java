package ru.otus.hw.mongock.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;

@ChangeLog
public class DatabaseChangelog {

    private Author author1;

    private Author author2;

    private Author author3;

    private Genre genre1;

    private Genre genre2;

    private Genre genre3;

    private Genre genre4;

    private Genre genre5;

    private Genre genre6;

    private Book book1;

    private Book book2;

    private Book book3;

    @ChangeSet(order = "000", id = "dropDB", author = "matvey", runAlways = true)
    public void dropDB(MongoDatabase database) {
        database.drop();
    }

    @ChangeSet(order = "001", id = "initAuthors", author = "matvey")
    public void initAuthors(AuthorRepository authorRepository) {
        author1 = authorRepository.save(new Author("1", "Author_1"));
        author2 = authorRepository.save(new Author("2", "Author_2"));
        author3 = authorRepository.save(new Author("3", "Author_3"));
    }

    @ChangeSet(order = "002", id = "initGenres", author = "matvey")
    public void initGenres(GenreRepository genreRepository) {
        genre1 = genreRepository.save(new Genre("1", "Genre_1"));
        genre2 = genreRepository.save(new Genre("2", "Genre_2"));
        genre3 = genreRepository.save(new Genre("3", "Genre_3"));
        genre4 = genreRepository.save(new Genre("4", "Genre_4"));
        genre5 = genreRepository.save(new Genre("5", "Genre_5"));
        genre6 = genreRepository.save(new Genre("6", "Genre_6"));
    }

    @ChangeSet(order = "003", id = "initBooks", author = "matvey")
    public void initBooks(BookRepository bookRepository) {
        book1 = bookRepository.save(new Book("1", "BookTitle_1", author1, List.of(genre1, genre2)));
        book2 = bookRepository.save(new Book("2", "BookTitle_2", author2, List.of(genre3, genre4)));
        book3 = bookRepository.save(new Book("3", "BookTitle_3", author3, List.of(genre5, genre6)));
    }

    @ChangeSet(order = "004", id = "initComments", author = "matvey")
    public void initComments(CommentRepository commentRepository) {
        commentRepository.save(new Comment("1", "comment_1", book1.getId()));
        commentRepository.save(new Comment("2", "comment_2", book1.getId()));
        commentRepository.save(new Comment("3", "comment_3", book1.getId()));

        commentRepository.save(new Comment("4", "comment_4", book2.getId()));
        commentRepository.save(new Comment("5", "comment_5", book2.getId()));
        commentRepository.save(new Comment("6", "comment_6", book2.getId()));

        commentRepository.save(new Comment("7", "comment_7", book3.getId()));
        commentRepository.save(new Comment("8", "comment_8", book3.getId()));
        commentRepository.save(new Comment("9", "comment_9", book3.getId()));
    }
}
