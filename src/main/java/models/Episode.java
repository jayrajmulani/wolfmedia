package models;

import lombok.*;

import java.sql.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Episode {
    private Podcast podcast;
    private long episodeNum;
    private String title;
    private Date releaseDate;
    private double duration;
    private int advCount;
    private double bonusRate;
    private String episodeId;

    public Episode(Podcast podcast, long episodeNum, String title) {
        this.podcast = podcast;
        this.episodeNum = episodeNum;
        this.title = title;
    }
}
