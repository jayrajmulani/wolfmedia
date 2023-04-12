DELETE FROM ARTIST_TYPE WHERE 1=1;
ALTER TABLE ARTIST_TYPE AUTO_INCREMENT = 1;
insert into ARTIST_TYPE (name) values ('BAND');
insert into ARTIST_TYPE (name) values ('MUSICIAN');
insert into ARTIST_TYPE (name) values ('COMPOSER');
insert into ARTIST_TYPE (name) values ('SINGER');
insert into ARTIST_TYPE (name) values ('LYRICIST');