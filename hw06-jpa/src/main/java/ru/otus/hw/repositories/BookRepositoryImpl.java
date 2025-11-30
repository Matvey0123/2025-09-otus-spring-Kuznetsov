package ru.otus.hw.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Book;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.springframework.data.jpa.repository.EntityGraph.EntityGraphType.FETCH;

@Repository
@RequiredArgsConstructor
public class BookRepositoryImpl implements BookRepository {

    private final CommentRepository commentRepository;

    @PersistenceContext
    private final EntityManager em;

    @Override
    public Optional<Book> findById(long id) {
        var entityGraph = em.getEntityGraph("book_authors_genres_entity_graph");
        var book = em.find(Book.class, id, Map.of(FETCH.getKey(), entityGraph));
        return Optional.ofNullable(book);
    }

    @Override
    public List<Book> findAll() {
        var entityGraph = em.getEntityGraph("book_authors_genres_entity_graph");
        var query = em.createQuery("select b from Book b", Book.class);
        query.setHint(FETCH.getKey(), entityGraph);
        return query.getResultList();
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == 0) {
            em.persist(book);
            return book;
        }
        return em.merge(book);
    }

    @Override
    public void deleteById(long id) {
        var comments = commentRepository.findByBookId(id);
        comments.forEach(em::remove);
        var book = em.find(Book.class, id);
        if (book != null) {
            em.remove(book);
        }
    }
}
