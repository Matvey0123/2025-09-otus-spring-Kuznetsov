package ru.otus.hw.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Book;

@Repository
@RequiredArgsConstructor
public class BookRepositoryImpl implements BookRepositoryCustom {

    private final CommentRepository commentRepository;

    @PersistenceContext
    private final EntityManager em;

    @Override
    public void deleteByIdWithComments(long id) {
        var comments = commentRepository.findByBookId(id);
        comments.forEach(em::remove);
        var book = em.find(Book.class, id);
        if (book != null) {
            em.remove(book);
        }
    }
}
