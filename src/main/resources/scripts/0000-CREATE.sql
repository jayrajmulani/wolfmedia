create table if not exists SERVICE(
    id  int auto_increment PRIMARY KEY,
    name TEXT NOT NULL,
    balance double NOT NULL DEFAULT 0
);

create table if not exists USER(
    id int auto_increment PRIMARY KEY,
    f_name TEXT NOT NULL ,
    l_name TEXT NOT NULL ,
    reg_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP() NOT NULL,
    phone TEXT NOT NULL ,
    email TEXT NOT NULL ,
    premium_status BOOLEAN NOT NULL DEFAULT FALSE,
    monthly_premium_fees double NOT NULL default 0
);

create table if not exists RECORD_LABEL(
    id int auto_increment  PRIMARY KEY ,
    name TEXT NOT NULL
);
create table if not exists ARTIST(
    id int auto_increment PRIMARY KEY ,
    name TEXT NOT NULL ,
    country TEXT NOT NULL ,
    status enum('RETIRED', 'ACTIVE') NOT NULL
);
create table if not exists SONG(
    id int auto_increment PRIMARY KEY ,
    title TEXT NOT NULL ,
    release_country TEXT NOT NULL ,
    language TEXT NOT NULL ,
    duration double NOT NULL ,
    royalty_rate double NOT NULL ,
    release_date DATE NOT NULL ,
    royalty_paid boolean NOT NULL
);

create table if not exists ALBUM(
    id int auto_increment PRIMARY KEY ,
    name TEXT NOT NULL ,
    release_date DATE NOT NULL ,
    edition int NOT NULL
);

create table if not exists HOST(
    id int auto_increment PRIMARY KEY ,
    first_name TEXT NOT NULL ,
    last_name TEXT NOT NULL ,
    city TEXT NOT NULL ,
    email TEXT NOT NULL ,
    phone TEXT NOT NULL
);

create table if not exists PODCAST(
    id int auto_increment PRIMARY KEY,
    name TEXT NOT NULL,
    country TEXT NOT NULL,
    language TEXT NOT NULL,
    flat_fee double NOT NULL DEFAULT 0
);

create table if not exists EPISODE(
    podcast_id int  NOT NULL ,
    episode_num int NOT NULL ,
    title TEXT NOT NULL ,
    release_date DATE NOT NULL ,
    duration double NOT NULL ,
    adv_count int NOT NULL DEFAULT 0,
    bonus_rate double NOT NULL DEFAULT 0,
    episode_id double DEFAULT NULL,
    PRIMARY KEY (podcast_id, episode_num)
);

create table if not exists GENRE(
    id int auto_increment PRIMARY KEY ,
    name TEXT NOT NULL
);

create table if not exists SPONSOR(
    id int auto_increment PRIMARY KEY ,
    name TEXT NOT NULL
);

create table if not exists GUEST(
    id int auto_increment PRIMARY KEY ,
    name TEXT NOT NULL
);

create table if not exists EARNS(
    id int auto_increment PRIMARY KEY ,
    service_id int NOT NULL ,
    user_id int NOT NULL ,
    amount double NOT NULL DEFAULT 0,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP() NOT NULL
);

create table if not exists HOST_PAY(
    id int auto_increment PRIMARY KEY ,
    service_id int NOT NULL ,
    host_id int NOT NULL ,
    amount double NOT NULL DEFAULT 0,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP() NOT NULL
);

create table if not exists RL_PAY(
    id int auto_increment PRIMARY KEY ,
    service_id int NOT NULL ,
    record_label_id int NOT NULL ,
    amount double NOT NULL DEFAULT 0,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP() NOT NULL
);

create table if not exists COMPILES(
    artist_id int NOT NULL ,
    album_id int NOT NULL ,
    PRIMARY KEY (artist_id, album_id)
);

create table if not exists CREATES(
    artist_id int NOT NULL ,
    song_id int NOT NULL ,
    is_collaborator BOOLEAN NOT NULL DEFAULT FALSE ,
    PRIMARY KEY (artist_id, song_id)
);

create table if not exists SONG_ALBUM(
    song_id int NOT NULL ,
    album_id int NOT NULL ,
    track_num int NOT NULL ,
    primary key (song_id, album_id, track_num)
);

create table if not exists SONG_GENRE(
    song_id int NOT NULL ,
    genre_id int NOT NULL ,
    PRIMARY KEY (song_id, genre_id)
);

create table if not exists OWNS(
    record_label_id int NOT NULL ,
    song_id int NOT NULL ,
    PRIMARY KEY (record_label_id, song_id)
);

create table if not exists SONG_LISTEN(
    id int auto_increment PRIMARY KEY ,
    song_id int NOT NULL ,
    user_id int NOT NULL ,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP() NOT NULL
);

create table if not exists ARTIST_PAY(
    id int auto_increment PRIMARY KEY ,
    artist_id int NOT NULL ,
    record_label_id int NOT NULL ,
    amount double NOT NULL DEFAULT 0,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP() NOT NULL
);

create table if not exists SIGNS(
    id int auto_increment PRIMARY KEY ,
    artist_id int NOT NULL ,
    record_label_id int NOT NULL ,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP() NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

create table if not exists ARTIST_SUBSCRIPTION(
   id int auto_increment PRIMARY KEY ,
   artist_id int NOT NULL,
   user_id int NOT NULL ,
   created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP() NOT NULL,
   updated_at TIMESTAMP NOT NULL,
   subscription_status BOOLEAN DEFAULT FALSE
);
create table if not exists PODCAST_SUBSCRIPTION(
   id int auto_increment PRIMARY KEY ,
   podcast_id int NOT NULL,
   user_id int NOT NULL ,
   created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP() NOT NULL,
   updated_at TIMESTAMP NOT NULL,
   subscription_status BOOLEAN DEFAULT FALSE
);

create table if not exists PODCAST_HOST(
    host_id int NOT NULL ,
    podcast_id int NOT NULL ,
    PRIMARY KEY (host_id, podcast_id)
);

create table if not exists PODCAST_EP_LISTEN(
    id int auto_increment PRIMARY KEY ,
    user_id int NOT NULL ,
    podcast_id int NOT NULL ,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP() NOT NULL ,
    episode_num int NOT NULL
);

create table if not exists PODCAST_SPONSOR(
    podcast_id int NOT NULL ,
    sponsor_id int NOT NULL ,
    PRIMARY KEY (podcast_id, sponsor_id)
);

create table if not exists EPISODE_GUEST(
    podcast_id int NOT NULL ,
    episode_num int NOT NULL ,
    guest_id int NOT NULL ,
    PRIMARY KEY (podcast_id, guest_id, episode_num)
);

create table if not exists PODCAST_GENRE(
    podcast_id int NOT NULL ,
    genre_id int NOT NULL ,
    PRIMARY KEY (podcast_id, genre_id)
);

create table if not exists PRIMARY_GENRE(
    artist_id int NOT NULL ,
    genre_id int NOT NULL ,
    PRIMARY KEY (artist_id, genre_id)
);

create table if not exists ARTIST_IS(
    artist_id int NOT NULL ,
    artist_type_id int NOT NULL ,
    PRIMARY KEY (artist_id, artist_type_id)
);

create table if not exists ARTIST_TYPE(
    id int auto_increment PRIMARY KEY ,
    name TEXT NOT NULL
);

create table if not exists RATES(
    user_id int NOT NULL ,
    podcast_id int NOT NULL ,
    updated_at TIMESTAMP NOT NULL ,
    rating double NOT NULL default 0 ,
    PRIMARY KEY (user_id, podcast_id)
);
create table if not exists HISTORICAL_SONG_PLAY_COUNT(
     song_id int NOT NULL,
     month int NOT NULL,
     year int NOT NULL,
     play_count int NOT NULL
);
create table if not exists HISTORICAL_EPISODE_PLAY_COUNT(
    podcast_id int NOT NULL,
    episode_num int NOT NULL,
    month int NOT NULL,
    year int NOT NULL,
    play_count int NOT NULL
);