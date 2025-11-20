package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Author;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcAuthorRepository implements AuthorRepository {

    private final NamedParameterJdbcOperations jdbc;

    @Override
    public List<Author> findAll() {
        return jdbc.query("select id, full_name from authors", new AuthorRowMapper());
    }

    @Override
    public Optional<Author> findById(long id) {
        var params = new MapSqlParameterSource("id", id);
        return Optional.ofNullable(jdbc.query(
                "select id, full_name from authors where id = :id", params, new AuthorResultSetExtractor()));
    }

    private static class AuthorRowMapper implements RowMapper<Author> {

        @Override
        public Author mapRow(ResultSet rs, int i) throws SQLException {
            return convertResultSetToAuthor(rs);
        }
    }

    private static class AuthorResultSetExtractor implements ResultSetExtractor<Author> {

        @Override
        public Author extractData(ResultSet rs) throws SQLException, DataAccessException {
            if (rs.next()) {
                return convertResultSetToAuthor(rs);
            } else {
                return null;
            }
        }
    }

    private static Author convertResultSetToAuthor(ResultSet rs) throws SQLException {
        long id = rs.getLong("id");
        String fullName = rs.getString("fullName");
        return new Author(id, fullName);
    }
}
