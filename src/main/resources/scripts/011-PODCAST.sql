DELETE FROM PODCAST WHERE 1=1;
ALTER TABLE PODCAST AUTO_INCREMENT = 5001;
insert into PODCAST (name, language, country, flat_fee) values ('Mind Over Matter: Exploring the Power of the Human Mind', 'English', 'United States', 10);