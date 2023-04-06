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
public class Podcast {
    long id;
    String name;
    String language;
    String country;
    double flatFee;
    int rating;
    long subscribers;
    List<Host> hosts;
    List<Sponsor> sponsors;
    List<Genre> genres;
//    List<Episode> episodes;
    public Podcast(String name, String language, String country, double flatFee, List<Host> hosts) {
        this.name = name;
        this.language = language;
        this.country = country;
        this.flatFee = flatFee;
        this.hosts = hosts;
    }

    public Podcast(long id, String name, String language, String country, double flatFee) {
        this.id = id;
        this.name = name;
        this.language = language;
        this.country = country;
        this.flatFee = flatFee;
    }
}
