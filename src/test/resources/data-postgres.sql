insert into article (content, create_at, title, author_id) values ( 'AAAAA', '2021-03-30 16:33:57.251447', 'CCCCC', 1);
insert into article (content, create_at, title, author_id) values ('BBBBB', '2021-03-30 16:34:10.441270', 'DDDDD', 1);
insert into article (content, create_at, title, author_id) values ('IIIII', '2021-04-04 23:43:50.442632', 'EEEEE', 1);
insert into article (content, create_at, title, author_id) values ('IIIWW', '2021-04-05 06:43:50.422632', 'EWWEE', 1);
insert into article (content, create_at, title, author_id) values ('IIIFF', '2021-04-05 07:47:52.411321', 'EKKEE', 1);


insert into subscription (followee_id, follower_id) values (1, 2);
insert into subscription (followee_id, follower_id) values (2, 3);
insert into subscription (followee_id, follower_id) values (1, 3);

insert into user_reaction (mark, article_id, blogger_user_id) values (null, 1, 1);
insert into user_reaction (mark, article_id, blogger_user_id) values (null, 1, 2);
insert into user_reaction (mark, article_id, blogger_user_id) values (null, 2, 3);


