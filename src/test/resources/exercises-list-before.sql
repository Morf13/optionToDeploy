delete from exercises;

insert into exercises(id, title, user_id) values
(1, 'first', 1),
(2, 'second', 1),
(3, 'third', 1),
(4, 'fourth', 2);

alter sequence hibernate_sequence restart with 10;