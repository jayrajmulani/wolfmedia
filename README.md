# Wolf Media

This is a description of the main project for the course Database Management Concepts and Systems. The details are likely to change over time, so please visit the online version again. 

You are to design and build a database system for WolfMedia, a media streaming service. This is an audio and media streaming service, like Spotify or Apple Music. The database system will be used by the administrators and management of the streaming service and should maintain information on at least the following:

- Song: song title, artist(s), duration, genre(s), album, play count, release date, release country, language, royalty rate, collaborators (guest artist), royalty_paid(status of whether the current royalty has been paid out)
- Artist: name, collaboration(s), status (active/retired), type (Band/musician/composer), country, primary genre, monthly listeners, album(s), record label
- Record label: record label name, contracted artist(s),
- Album: album name, artist(s), song(s), track number(s), release year, Edition (special, limited, collector’s edition)
- Podcast: podcast host, podcast name, language, country, episode count, genre(s), rating, sponsor(s), total subscribers
- Podcast host: first name, last name, phone, email, city, podcast(s)
- Podcast episode: episode title, duration, release date, listening count, special guest(s), advertisement count
- User: first name, last name, phone, email, registration date, status of subscription, monthly subscription fee

By talking to the managers, we have elicited for you the following information for the media streaming service. (Note that in working on this project, you might discover that not every bit of the information has to be explicitly captured in the database. Part of the modeling effort is to decide what to keep and what to discard. In doing your project, you will need to make additional assumptions as well as identify the potential inconsistencies and resolve them. Any reasonable assumptions are fine, but they must be documented in your reports. You can consult with the TA or instructor if you have questions about the assumptions.) 

The media streaming service features songs and podcasts by various artists, hosts, and record labels. Each song has at least one artist which can be a band, solo musician and/or composer. Each song is owned by the record label of its main artist. Podcasts have at least one host with podcast episodes that sometimes feature special guests.
The media streaming service generates monthly payments based on royalties collected. Royalties are generated based on a royalty rate for each song times the total play count. The royalties are paid out to the record label who keeps 30% and distributes the remainder evenly among the respective artist(s). Both active, retired and guest artists receive royalties.
Podcast hosts are paid a single flat fee per released episode and an additional bonus based on the total advertisements presented within the episode. Special guest(s) are volunteers and do not receive payments.
All payment information for artist(s), record label(s) and podcast host(s) are recorded and maintained by the media streaming service. All relevant information for the songs, artists, albums, podcasts, host(s), and episodes should be kept in the database system. Monthly listeners and total subscribers include unique active listener accounts which subscribe to the artist or podcast. Subscribers make monthly payments to the media streaming service.
Management collects and analyzes data on various aspects of the streamed items and associated parties including but not limited to song play count, podcast subscribers, ratings, and all payments made. Generated reports include monthly, yearly, and total payment summaries for each artist, record label and podcast host. Monthly, yearly, and total reports for podcast/song subscribers, play count, and rating (if applicable).
Tasks and Operations

The following are the four major kinds of tasks that need to be performed using your database. Each task potentially consists of a number of operations; an "operation" is something that corresponds to a separate action. For example, Information Processing is considered to be one task, which involves separate operations such as entering and updating basic information about songs, and podcasts.  

Each student should write about the same number of application programs, but it is up to each team to decide how to allocate the overall effort. 

1. Information Processing: Enter/update/delete basic information about songs, artists, podcast hosts, and podcast episodes. Assign songs and artists to albums. Assign artists to record labels. Assign podcast episodes and podcast hosts to podcasts.
2. Maintaining metadata and records:  Enter/update play count for songs.  Enter/update the count of monthly listeners for artists. Enter/update the total count of subscribers and ratings for podcasts. Enter/Update the listening count for podcast episodes. Find songs and podcast episodes given artist, album, and/or podcast.
3. Maintaining payments: Make royalty payments for a given song. Monthly royalties are generated based on a royalty rate for each song times the total play count per month. 30% of the collected royalties are paid to the record label and the remainder is distributed evenly among all participating artists. Make payment to podcast hosts. Podcast hosts are paid a single flat fee per released episode and an additional bonus based on total advertisements per podcast episode. Receive payment from subscribers.
4. Reports: Generate all the following reports: Monthly play count per song/album/artist. Calculate total payments made out to host/artist/record labels per a given time period. Total revenue of the streaming service per month, per year. Report all songs/podcast episodes given an artist, album, and/or podcast.
