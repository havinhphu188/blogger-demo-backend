SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

CREATE TABLE public.article (
                                id integer NOT NULL,
                                content character varying(255),
                                create_at timestamp without time zone,
                                title character varying(255),
                                author_id integer
);


ALTER TABLE public.article OWNER TO postgres;

ALTER TABLE public.article ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
        START WITH 1
        INCREMENT BY 1
        NO MINVALUE
        NO MAXVALUE
        CACHE 1
        );

CREATE TABLE public.blogger_user (
                                     id integer NOT NULL,
                                     bio character varying(255),
                                     display_name character varying(255),
                                     password character varying(255),
                                     username character varying(255)
);


ALTER TABLE public.blogger_user OWNER TO postgres;

ALTER TABLE public.blogger_user ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
START WITH 1
          INCREMENT BY 1
          NO MINVALUE
          NO MAXVALUE
          CACHE 1
          );


CREATE SEQUENCE public.hibernate_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.hibernate_sequence OWNER TO postgres;

CREATE TABLE public.subscription (
                                     id integer NOT NULL,
                                     followee_id integer,
                                     follower_id integer
);


ALTER TABLE public.subscription OWNER TO postgres;

ALTER TABLE public.subscription ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
START WITH 1
          INCREMENT BY 1
          NO MINVALUE
          NO MAXVALUE
          CACHE 1
          );


CREATE TABLE public.user_reaction (
                                      id integer NOT NULL,
                                      mark character varying(255),
                                      article_id integer,
                                      blogger_user_id integer
);


ALTER TABLE public.user_reaction OWNER TO postgres;

ALTER TABLE public.user_reaction ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1
);

ALTER TABLE ONLY public.article
    ADD CONSTRAINT article_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.blogger_user
    ADD CONSTRAINT blogger_user_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.subscription
    ADD CONSTRAINT subscription_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.blogger_user
    ADD CONSTRAINT username_unique UNIQUE (username);

ALTER TABLE ONLY public.blogger_user
    ADD CONSTRAINT displayname_unique UNIQUE (display_name);

ALTER TABLE ONLY public.user_reaction
    ADD CONSTRAINT user_reaction_once UNIQUE (article_id, blogger_user_id);


ALTER TABLE ONLY public.user_reaction
    ADD CONSTRAINT user_reaction_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.article
    ADD CONSTRAINT fk_article_author FOREIGN KEY (author_id) REFERENCES public.blogger_user(id);

ALTER TABLE ONLY public.user_reaction
    ADD CONSTRAINT fk_user_reaction_user FOREIGN KEY (blogger_user_id) REFERENCES public.blogger_user(id);

ALTER TABLE ONLY public.subscription
    ADD CONSTRAINT fk_subscription_follower FOREIGN KEY (follower_id) REFERENCES public.blogger_user(id);

ALTER TABLE ONLY public.subscription
    ADD CONSTRAINT fk_subscription_followee FOREIGN KEY (followee_id) REFERENCES public.blogger_user(id);

ALTER TABLE ONLY public.user_reaction
    ADD CONSTRAINT fk_user_reaction_article FOREIGN KEY (article_id) REFERENCES public.article(id);
