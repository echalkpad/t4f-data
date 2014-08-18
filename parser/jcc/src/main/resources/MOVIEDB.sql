
-- DDL and INSERTs for MOVIE database to be used with 
-- JoAnn Brereton's JavaCC to SQL paper.

-- Create the database

DROP DB MOVIEDB;

CREATE DB MOVIEDB;

CONNECT TO MOVIEDB;

-- MOVIE table

CREATE TABLE MOVIE
  (MOVIE_ID INTEGER NOT NULL,   
   DIRECTOR VARCHAR(128) NOT NULL,
   TITLE VARCHAR(128) NOT NULL);

ALTER TABLE MOVIE
  ADD CONSTRAINT PKEY_MOVIE PRIMARY KEY (MOVIE_ID);

-- ACTOR table.  

CREATE TABLE ACTOR
  (MOVIE_ID INTEGER NOT NULL,
   NAME VARCHAR(128) NOT NULL);

ALTER TABLE ACTOR
  ADD CONSTRAINT PKEY_ACTOR PRIMARY KEY (NAME, MOVIE_ID);


-- KEYWORD table. 

CREATE TABLE KEYWORD
  (MOVIE_ID INTEGER NOT NULL,
   KEYWORD VARCHAR(128) NOT NULL);

ALTER TABLE KEYWORD
  ADD CONSTRAINT PKEY_KEYWORD PRIMARY KEY (MOVIE_ID, KEYWORD);


-- Foreign keys linking ACTOR and KEYWORD back to MOVIE.

ALTER TABLE ACTOR
  ADD CONSTRAINT MOVIE_ID FOREIGN KEY (MOVIE_ID)
  REFERENCES MOVIE(MOVIE_ID)
  ON DELETE CASCADE;

ALTER TABLE KEYWORD
  ADD CONSTRAINT MOVIE_ID FOREIGN KEY (MOVIE_ID)
  REFERENCES MOVIE(MOVIE_ID)
  ON DELETE CASCADE;


--  Sprinkle some data into the tables.  

INSERT INTO MOVIE(MOVIE_ID, DIRECTOR, TITLE)
            VALUES(1, 'Tony Scott','Top Gun');

INSERT INTO MOVIE(MOVIE_ID, DIRECTOR, TITLE)
            VALUES(2, 'Steven Spielberg','Raiders of the Lost Ark');

INSERT INTO MOVIE(MOVIE_ID, DIRECTOR, TITLE)
            VALUES(3, 'Richard Donner','Superman');

INSERT INTO MOVIE(MOVIE_ID, DIRECTOR, TITLE)
            VALUES(4, 'George Lucas','Star Wars');

INSERT INTO MOVIE(MOVIE_ID, DIRECTOR, TITLE)
            VALUES(5, 'Peter Weir', 'Witness');


INSERT INTO ACTOR(MOVIE_ID, NAME)
            VALUES(1, 'Tom Cruise');

INSERT INTO ACTOR(MOVIE_ID, NAME)
            VALUES(1, 'Kelly McGillis');

INSERT INTO ACTOR(MOVIE_ID, NAME)
            VALUES(1, 'Val Kilmer');

INSERT INTO ACTOR(MOVIE_ID, NAME)
            VALUES(1, 'Anthony Edwards');

INSERT INTO ACTOR(MOVIE_ID, NAME)
            VALUES(1, 'Tom Skerritt');

INSERT INTO ACTOR(MOVIE_ID, NAME)
            VALUES(2, 'Harrison Ford');

INSERT INTO ACTOR(MOVIE_ID, NAME)
            VALUES(2, 'Karen Allen');

INSERT INTO ACTOR(MOVIE_ID, NAME)
            VALUES(2, 'Paul Freeman');

INSERT INTO ACTOR(MOVIE_ID, NAME)
            VALUES(2, 'Ronald Lacey');

INSERT INTO ACTOR(MOVIE_ID, NAME)
            VALUES(2, 'John Rhys-Davies');

INSERT INTO ACTOR(MOVIE_ID, NAME)
            VALUES(3, 'Marlon Brando');

INSERT INTO ACTOR(MOVIE_ID, NAME)
            VALUES(3, 'Gene Hackman');

INSERT INTO ACTOR(MOVIE_ID, NAME)
            VALUES(3, 'Christopher Reeve');

INSERT INTO ACTOR(MOVIE_ID, NAME)
            VALUES(3, 'Ned Beatty');

INSERT INTO ACTOR(MOVIE_ID, NAME)
            VALUES(3, 'Margot Kidder');

INSERT INTO ACTOR(MOVIE_ID, NAME)
            VALUES(4, 'Mark Hamill');

INSERT INTO ACTOR(MOVIE_ID, NAME)
            VALUES(4, 'Harrison Ford');

INSERT INTO ACTOR(MOVIE_ID, NAME)
            VALUES(4, 'Carrie Fisher');

INSERT INTO ACTOR(MOVIE_ID, NAME)
            VALUES(4, 'Peter Cushing');

INSERT INTO ACTOR(MOVIE_ID, NAME)
            VALUES(4, 'Alec Guinness');

INSERT INTO ACTOR(MOVIE_ID, NAME)
            VALUES(5, 'Harrison Ford');

INSERT INTO ACTOR(MOVIE_ID, NAME)
            VALUES(5, 'Kelly McGillis');

INSERT INTO ACTOR(MOVIE_ID, NAME)
            VALUES(5, 'Josef Sommer');

INSERT INTO ACTOR(MOVIE_ID, NAME)
            VALUES(5, 'Lukas Haas');

INSERT INTO ACTOR(MOVIE_ID, NAME)
            VALUES(5, 'Danny Glover');

INSERT INTO KEYWORD(MOVIE_ID, KEYWORD)
            VALUES(1, 'action');

INSERT INTO KEYWORD(MOVIE_ID, KEYWORD)
            VALUES(1, 'drama');

INSERT INTO KEYWORD(MOVIE_ID, KEYWORD)
            VALUES(1, 'romance');

INSERT INTO KEYWORD(MOVIE_ID, KEYWORD)
            VALUES(2, 'action');

INSERT INTO KEYWORD(MOVIE_ID, KEYWORD)
            VALUES(2, 'adventure');

INSERT INTO KEYWORD(MOVIE_ID, KEYWORD)
            VALUES(3, 'action');

INSERT INTO KEYWORD(MOVIE_ID, KEYWORD)
            VALUES(3, 'family');

INSERT INTO KEYWORD(MOVIE_ID, KEYWORD)
            VALUES(3, 'adventure');

INSERT INTO KEYWORD(MOVIE_ID, KEYWORD)
            VALUES(3, 'sci-fi');

INSERT INTO KEYWORD(MOVIE_ID, KEYWORD)
            VALUES(4, 'sci-fi');

INSERT INTO KEYWORD(MOVIE_ID, KEYWORD)
            VALUES(4, 'action');

INSERT INTO KEYWORD(MOVIE_ID, KEYWORD)
            VALUES(4, 'adventure');

INSERT INTO KEYWORD(MOVIE_ID, KEYWORD)
            VALUES(4, 'fantasy');

INSERT INTO KEYWORD(MOVIE_ID, KEYWORD)
            VALUES(5, 'drama');

INSERT INTO KEYWORD(MOVIE_ID, KEYWORD)
            VALUES(5, 'romance');

INSERT INTO KEYWORD(MOVIE_ID, KEYWORD)
            VALUES(5, 'thriller');


CONNECT RESET;





