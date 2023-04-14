package models;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class EpisodeGuest {
    long podcast_id;
    long episode_num;
    long guest_id;
}
