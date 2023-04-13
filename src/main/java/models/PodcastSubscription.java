package models;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class PodcastSubscription {
    private long podcastId;
    private long userId;
    private Timestamp updated_at;
    private long subscriptionStatus;
}
