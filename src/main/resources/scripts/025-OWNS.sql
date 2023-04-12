DELETE FROM OWNS WHERE 1=1;
INSERT INTO OWNS(record_label_id, song_id ) SELECT record_label_id, CREATES.song_id FROM SIGNS, CREATES WHERE SIGNS.artist_id = CREATES.artist_id and CREATES.is_collaborator= false;
