package models;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class PodcastEpListen {
    private long podcastId;
    private long userId;
    private long episodeNum;
}
