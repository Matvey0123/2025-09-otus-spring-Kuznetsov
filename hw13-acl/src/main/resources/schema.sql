create table authors (
    id bigserial,
    full_name varchar(255),
    primary key (id)
);

create table genres (
    id bigserial,
    name varchar(255),
    primary key (id)
);

create table books (
    id bigserial,
    title varchar(255),
    author_id bigint,
    foreign key (author_id) references authors(id),
    primary key (id)
);

create table books_genres (
        book_id bigint not null,
        genre_id bigint not null,
        foreign key (book_id) references books(id),
        foreign key (genre_id) references genres(id)
);

create table comments (
        id bigserial,
        text varchar(255),
        book_id bigint,
        foreign key (book_id) references books(id),
        primary key (id)
);

create table users (
    id bigserial,
    username varchar(255),
    password varchar(255),
    role varchar(50),
    primary key (id)
);