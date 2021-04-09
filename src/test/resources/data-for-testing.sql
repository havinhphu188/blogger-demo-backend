INSERT INTO blogger_user (password, username, bio, display_name)
VALUES('$2a$10$9kmnSJGJyIh4zUMubQEtzeug4VO2Qyna6jcg4WrR.PUmAYvZEgkJK','user1', 'Test user 1', 'username 1');
INSERT INTO blogger_user (password, username, bio, display_name)
VALUES('$2a$10$9kmnSJGJyIh4zUMubQEtzeug4VO2Qyna6jcg4WrR.PUmAYvZEgkJK','user2', 'Test user 2', 'username 2');
INSERT INTO blogger_user (password, username, bio, display_name)
VALUES('$2a$10$9kmnSJGJyIh4zUMubQEtzeug4VO2Qyna6jcg4WrR.PUmAYvZEgkJK','user3', 'Test user 3', 'username 3');
INSERT INTO blogger_user (password, username, bio, display_name)
VALUES('$2a$10$9kmnSJGJyIh4zUMubQEtzeug4VO2Qyna6jcg4WrR.PUmAYvZEgkJK','havinhphu','Love writing','Ha Vinh Phu');
INSERT INTO blogger_user (password, username, bio, display_name)
VALUES('$2a$10$9kmnSJGJyIh4zUMubQEtzeug4VO2Qyna6jcg4WrR.PUmAYvZEgkJK','dan_abramov','author of redux','Dan Abramov');
INSERT INTO blogger_user (password, username, bio, display_name)
VALUES('$2a$10$9kmnSJGJyIh4zUMubQEtzeug4VO2Qyna6jcg4WrR.PUmAYvZEgkJK','m_fowler','Write about software architecture, best practice','Martin Fowler');
INSERT INTO blogger_user (password, username, bio, display_name)
VALUES('$2a$10$9kmnSJGJyIh4zUMubQEtzeug4VO2Qyna6jcg4WrR.PUmAYvZEgkJK','uncle_bob','Agile expert','Robert Martin');

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


