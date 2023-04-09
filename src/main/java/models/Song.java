package models;

import lombok.*;

import java.sql.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Song {
    long id;
    String title;
    String releaseCountry;
    String language;
    double duration;
    double royaltyRate;
    Date releaseDate;
    boolean royaltyPaid;
    List<Genre> genres;
    public Song(String title, String releaseCountry, String language, double duration, double royaltyRate, Date releaseDate, boolean royaltyPaid, List<Genre> genres) {
        this.title = title;
        this.releaseCountry = releaseCountry;
        this.language = language;
        this.duration = duration;
        this.royaltyRate = royaltyRate;
        this.releaseDate = releaseDate;
        this.royaltyPaid = royaltyPaid;
        this.genres = genres;
    }
    public Song(long id, String title){
        this.title = title;
        this.id = id;
    }
}
