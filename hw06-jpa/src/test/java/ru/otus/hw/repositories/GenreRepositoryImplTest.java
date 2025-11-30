package ru.otus.hw.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({GenreRepositoryImpl.class})
class GenreRepositoryImplTest {

    private static final int EXPECTED_GENRES_COUNT = 6;

    @Autowired
    private GenreRepository repository;

    @Autowired
    private TestEntityManager em;

    @Test
    void shouldReturnCorrectGenresById() {
        var actualGenre = repository.findAllByIds(Set.of(1L, 2L));
        var expectedGenre1 = em.find(Genre.class, 1L);
        var expectedGenre2 = em.find(Genre.class, 2L);
        assertThat(actualGenre).containsAll(Set.of(expectedGenre1, expectedGenre2));
    }

    @Test
    void shouldReturnCorrectGenresList() {
        var genres = repository.findAll();
        assertThat(genres).isNotNull().hasSize(EXPECTED_GENRES_COUNT)
                .map(Genre::getName).containsAll(
                        List.of("Genre_1", "Genre_2", "Genre_3", "Genre_4", "Genre_5", "Genre_6"));
    }

}