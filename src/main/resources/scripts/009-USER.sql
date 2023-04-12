DELETE FROM USER WHERE 1=1;
ALTER TABLE USER AUTO_INCREMENT = 8001;
insert into USER (f_name, l_name, phone, email, reg_date, premium_status, monthly_premium_fees) values ('Alex', 'A', '480-504-9960', 'alex@wolf.com', '2020-03-18 21:39:22', 1, 10);
insert into USER (f_name, l_name, phone, email, reg_date, premium_status, monthly_premium_fees) values ('John', 'J', '379-508-3422', 'john@wolf.com', '2020-01-26 17:35:42', 1, 10);


-- Dummy users for subscribers
insert into USER (f_name, l_name, phone, email, reg_date) values ('Eduard', 'Barkas', '480-504-9960', 'ebarkas0@zdnet.com', '2020-03-18 21:39:22');
insert into USER (f_name, l_name, phone, email, reg_date) values ('Britni', 'Dreinan', '379-508-3422', 'bdreinan1@goo.ne.jp', '2020-01-26 17:35:42');
insert into USER (f_name, l_name, phone, email, reg_date) values ('Bartholomeo', 'Tull', '797-447-6138', 'btull2@sourceforge.net', '2022-02-01 23:37:19');
insert into USER (f_name, l_name, phone, email, reg_date) values ('Ashbey', 'Watson', '571-739-7023', 'awatson3@digg.com', '2020-08-02 01:43:46');
insert into USER (f_name, l_name, phone, email, reg_date) values ('Leodora', 'Baseke', '736-906-7328', 'lbaseke4@sphinn.com', '2022-09-25 08:58:31');
insert into USER (f_name, l_name, phone, email, reg_date) values ('Aldwin', 'Zanioletti', '558-112-6864', 'azanioletti5@oakley.com', '2020-07-22 01:33:47');
insert into USER (f_name, l_name, phone, email, reg_date) values ('Miltie', 'Petticrew', '752-987-9527', 'mpetticrew6@yelp.com', '2020-06-29 02:54:29');
insert into USER (f_name, l_name, phone, email, reg_date) values ('Carmen', 'Hanrahan', '538-106-9129', 'chanrahan7@wunderground.com', '2022-02-20 05:18:27');