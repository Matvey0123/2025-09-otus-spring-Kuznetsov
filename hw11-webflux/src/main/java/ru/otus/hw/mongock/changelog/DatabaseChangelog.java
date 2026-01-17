package ru.otus.hw.mongock.changelog;

import com.mongodb.client.result.InsertManyResult;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoDatabase;
import io.mongock.api.annotations.BeforeExecution;
import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackBeforeExecution;
import io.mongock.api.annotations.RollbackExecution;
import io.mongock.driver.mongodb.reactive.util.MongoSubscriberSync;
import io.mongock.driver.mongodb.reactive.util.SubscriberSync;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.Conventions;
import org.bson.codecs.pojo.PojoCodecProvider;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;

import java.util.List;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

@ChangeUnit(id = "library-initializer", order = "1", author = "matvey")
public class DatabaseChangelog {

    private final Author author1 = new Author("1", "Author_1");

    private final Author author2 = new Author("2", "Author_2");

    private final Author author3 = new Author("3", "Author_3");

    private final Genre genre1 = new Genre("1", "Genre_1");

    private final Genre genre2 = new Genre("2", "Genre_2");

    private final Genre genre3 = new Genre("3", "Genre_3");

    private final Genre genre4 = new Genre("4", "Genre_4");

    private final Genre genre5 = new Genre("5", "Genre_5");

    private final Genre genre6 = new Genre("6", "Genre_6");

    private final Book book1 = new Book("1", "BookTitle_1", author1, List.of(genre1, genre2));

    private final Book book2 = new Book("2", "BookTitle_2", author2, List.of(genre3, genre4));

    private final Book book3 = new Book("3", "BookTitle_3", author3, List.of(genre5, genre6));

    @BeforeExecution
    public void beforeExecution(MongoDatabase mongoDatabase) {
        SubscriberSync<Void> subscriber = new MongoSubscriberSync<>();
        mongoDatabase.createCollection("authors").subscribe(subscriber);
        mongoDatabase.createCollection("genres").subscribe(subscriber);
        mongoDatabase.createCollection("books").subscribe(subscriber);
        mongoDatabase.createCollection("comments").subscribe(subscriber);
        subscriber.await();
    }

    @RollbackBeforeExecution
    public void rollbackBeforeExecution(MongoDatabase mongoDatabase) {
        SubscriberSync<Void> subscriber = new MongoSubscriberSync<>();
        mongoDatabase.getCollection("authors").drop().subscribe(subscriber);
        mongoDatabase.getCollection("genres").drop().subscribe(subscriber);
        mongoDatabase.getCollection("books").drop().subscribe(subscriber);
        mongoDatabase.getCollection("comments").drop().subscribe(subscriber);
        subscriber.await();
    }

    @Execution
    public void execution(MongoDatabase mongoDatabase, MongoClient mongoClient) {
        var codecRegistry = fromRegistries(List.of(fromProviders(
                mongoClient.getCodecRegistry(),
                PojoCodecProvider.builder()
                        .register("ru.otus.hw.models")
                        .conventions(Conventions.DEFAULT_CONVENTIONS)
                        .build())));

        fillAuthors(mongoDatabase, codecRegistry);
        fillGenres(mongoDatabase, codecRegistry);
        fillBooks(mongoDatabase, codecRegistry);
        fillComments(mongoDatabase, codecRegistry);
    }

    @RollbackExecution
    public void rollbackExecution(MongoDatabase mongoDatabase) {
        MongoSubscriberSync<Void> subscriber = new MongoSubscriberSync<>();
        mongoDatabase.getCollection("authors").drop().subscribe(subscriber);
        mongoDatabase.getCollection("genres").drop().subscribe(subscriber);
        mongoDatabase.getCollection("books").drop().subscribe(subscriber);
        mongoDatabase.getCollection("comments").drop().subscribe(subscriber);
        subscriber.get();
    }

    private void fillAuthors(MongoDatabase mongoDatabase, CodecRegistry codecRegistry) {
        SubscriberSync<InsertManyResult> subscriber = new MongoSubscriberSync<>();
        mongoDatabase.getCollection("authors", Author.class)
                .withCodecRegistry(codecRegistry)
                .insertMany(List.of(
                        author1,
                        author2,
                        author3
                ))
                .subscribe(subscriber);

        subscriber.getFirst();
    }

    private void fillGenres(MongoDatabase mongoDatabase, CodecRegistry codecRegistry) {
        SubscriberSync<InsertManyResult> subscriber = new MongoSubscriberSync<>();
        mongoDatabase.getCollection("genres", Genre.class)
                .withCodecRegistry(codecRegistry)
                .insertMany(List.of(
                        genre1,
                        genre2,
                        genre3,
                        genre4,
                        genre5,
                        genre6
                ))
                .subscribe(subscriber);

        subscriber.getFirst();
    }

    private void fillBooks(MongoDatabase mongoDatabase, CodecRegistry codecRegistry) {
        SubscriberSync<InsertManyResult> subscriber = new MongoSubscriberSync<>();
        mongoDatabase.getCollection("books", Book.class)
                .withCodecRegistry(codecRegistry)
                .insertMany(List.of(
                        book1,
                        book2,
                        book3
                ))
                .subscribe(subscriber);

        subscriber.getFirst();
    }

    private void fillComments(MongoDatabase mongoDatabase, CodecRegistry codecRegistry) {
        SubscriberSync<InsertManyResult> subscriber = new MongoSubscriberSync<>();
        mongoDatabase.getCollection("comments", Comment.class)
                .withCodecRegistry(codecRegistry)
                .insertMany(List.of(
                        new Comment("1", "comment_1", "1"),
                        new Comment("2", "comment_2", "1"),
                        new Comment("3", "comment_3", "1"),
                        new Comment("4", "comment_4", "2"),
                        new Comment("5", "comment_5", "2"),
                        new Comment("6", "comment_6", "2"),
                        new Comment("7", "comment_7", "3"),
                        new Comment("8", "comment_8", "3"),
                        new Comment("9", "comment_9", "3")
                ))
                .subscribe(subscriber);

        subscriber.getFirst();
    }
}
