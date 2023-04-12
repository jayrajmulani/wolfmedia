DELETE FROM HISTORICAL_EPISODE_PLAY_COUNT WHERE 1=1;
insert into HISTORICAL_EPISODE_PLAY_COUNT(podcast_id , episode_num, month, year, play_count) values (5001, 1, 1, 2023, 30);
insert into HISTORICAL_EPISODE_PLAY_COUNT(podcast_id , episode_num, month, year, play_count) values (5001, 1, 2, 2023, 30);
insert into HISTORICAL_EPISODE_PLAY_COUNT(podcast_id , episode_num, month, year, play_count) values (5001, 1, 3 ,2023, 40);

insert into HISTORICAL_EPISODE_PLAY_COUNT(podcast_id , episode_num, month, year, play_count) values (5001, 2, 1, 2023, 60);
insert into HISTORICAL_EPISODE_PLAY_COUNT(podcast_id , episode_num, month, year, play_count) values (5001, 2, 2, 2023, 60);
insert into HISTORICAL_EPISODE_PLAY_COUNT(podcast_id , episode_num, month, year, play_count) values (5001, 2, 3 ,2023, 80);
