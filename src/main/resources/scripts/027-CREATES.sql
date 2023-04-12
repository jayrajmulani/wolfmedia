DELETE FROM CREATES WHERE 1=1;
INSERT INTO CREATES (song_id, artist_id, is_collaborator) VALUES (1001, 2001, false);
INSERT INTO CREATES (song_id, artist_id, is_collaborator) VALUES (1002, 2001, false);
INSERT INTO CREATES (song_id, artist_id, is_collaborator) VALUES (1002, 2002, true);
INSERT INTO CREATES (song_id, artist_id, is_collaborator) VALUES (1003, 2002, false);
INSERT INTO CREATES (song_id, artist_id, is_collaborator) VALUES (1004, 2002, false);