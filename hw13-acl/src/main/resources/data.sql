insert into authors(full_name)
values ('Author_1'), ('Author_2'), ('Author_3');

insert into genres(name)
values ('Genre_1'), ('Genre_2'), ('Genre_3'),
       ('Genre_4'), ('Genre_5'), ('Genre_6');

insert into books(title, author_id)
values ('BookTitle_1', 1), ('BookTitle_2', 2), ('BookTitle_3', 3);

insert into books_genres(book_id, genre_id)
values (1, 1),   (1, 2),
       (2, 3),   (2, 4),
       (3, 5),   (3, 6);

insert into comments(text, book_id)
values ('comment_1', 1), ('comment_2', 1), ('comment_3', 1),
       ('comment_4', 2), ('comment_5', 2), ('comment_6', 2),
       ('comment_7', 3), ('comment_8', 3), ('comment_9', 3);

insert into users(username, password, role)
values ('viewer', '$2a$10$JbtdyEr2TlrGqhfeXZjY6OZOEaa96DV.TAUqDX4mEnZD0qeXOZBUK', 'ROLE_VIEWER'),
       ('editor', '$2a$10$o9ZxanMKRo01zRVUzbUQc.vvAQIvOb58ZWLvcxg6odRNFKLwOjPkS', 'ROLE_EDITOR');