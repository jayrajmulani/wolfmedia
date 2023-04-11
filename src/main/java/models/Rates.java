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
    private long user_id;
    private long podcast_id;
    private double rating;
    private Date updated_at;

}
