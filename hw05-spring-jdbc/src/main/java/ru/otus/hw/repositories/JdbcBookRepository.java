package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcBookRepository implements BookRepository {

    private final NamedParameterJdbcOperations jdbc;

    @Override
    public Optional<Book> findById(long id) {
        var params = new MapSqlParameterSource("id", id);
        return Optional.ofNullable(jdbc.query("""
                select b.id, b.title, b.author_id, b.genre_id, a.full_name, g.name
                from books b
                join authors a on a.id = b.author_id
                join genres g on g.id = b.genre_id
                where b.id = :id
                """, params, new BookResultSetExtractor()));
    }

    @Override
    public List<Book> findAll() {
        return jdbc.query("""
                select b.id, b.title, b.author_id, b.genre_id, a.full_name, g.name
                from books b
                join authors a on a.id = b.author_id
                join genres g on g.id = b.genre_id
                """, new BookRowMapper());
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == 0) {
            return insert(book);
        }
        return update(book);
    }

    @Override
    public void deleteById(long id) {
        var params = new MapSqlParameterSource("id", id);
        jdbc.update("delete from books where id = :id", params);
    }

    private Book insert(Book book) {
        var keyHolder = new GeneratedKeyHolder();
        var params = new MapSqlParameterSource()
                .addValue("title", book.getTitle())
                .addValue("authorId", book.getAuthor().getId())
                .addValue("genreId", book.getGenre().getId());
        jdbc.update("""
                insert into books (title, author_id, genre_id)
                values (:title, :authorId, :genreId)
                """, params, keyHolder);
        book.setId(keyHolder.getKeyAs(Long.class));
        return book;
    }

    private Book update(Book book) {
        var params = new MapSqlParameterSource()
                .addValue("title", book.getTitle())
                .addValue("authorId", book.getAuthor().getId())
                .addValue("genreId", book.getGenre().getId())
                .addValue("id", book.getId());
        int affectedRows = jdbc.update("""
                update books
                set title = :title, author_id = :authorId, genre_id = :genreId
                where id = :id
                """, params);
        if (affectedRows == 0) {
            throw new EntityNotFoundException("no such book");
        }
        return book;
    }

    private static class BookRowMapper implements RowMapper<Book> {

        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            return convertResultSetToBook(rs);
        }
    }

    private static class BookResultSetExtractor implements ResultSetExtractor<Book> {

        @Override
        public Book extractData(ResultSet rs) throws SQLException, DataAccessException {
            if (rs.next()) {
                return convertResultSetToBook(rs);
            } else {
                return null;
            }
        }
    }

    private static Book convertResultSetToBook(ResultSet rs) throws SQLException {
        long id = rs.getLong("id");
        String title = rs.getString("title");
        long authorId = rs.getLong("author_id");
        long genreId = rs.getLong("genre_id");
        String authorFullName = rs.getString("full_name");
        String genreName = rs.getString("name");
        Author author = new Author(authorId, authorFullName);
        Genre genre = new Genre(genreId, genreName);
        return new Book(id, title, author, genre);
    }
}
