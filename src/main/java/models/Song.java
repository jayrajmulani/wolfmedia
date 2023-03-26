package models;

import lombok.*;

import java.sql.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Song {
    long id;
    String title;
    String releaseCountry;
    String language;
    float duration;
    float royaltyRate;
    Date releaseDate;
    boolean royaltyPaid;
    public Song(String title, String releaseCountry, String language, float duration, float royaltyRate, Date releaseDate, boolean royaltyPaid) {
        this.title = title;
        this.releaseCountry = releaseCountry;
        this.language = language;
        this.duration = duration;
        this.royaltyRate = royaltyRate;
        this.releaseDate = releaseDate;
        this.royaltyPaid = royaltyPaid;
    }
    @Override
    public String toString() {
        return "Song{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", releaseCountry='" + releaseCountry + '\'' +
                ", language='" + language + '\'' +
                ", duration=" + duration +
                ", royaltyRate=" + royaltyRate +
                ", releaseDate=" + releaseDate +
                ", royaltyPaid=" + royaltyPaid +
                '}';
    }
}
