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
    private long podcastId;
    private long episodeNum;
    private String title;
    private Date releaseDate;
    private double duration;
    private int advCount;
    private double bonusRate;
}
