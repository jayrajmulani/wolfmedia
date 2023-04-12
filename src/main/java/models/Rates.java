package models;

import lombok.*;

import java.sql.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Rates {
    private long userId;
    private long podcastId;
    private double rating;
    private Date updatedAt;

}
