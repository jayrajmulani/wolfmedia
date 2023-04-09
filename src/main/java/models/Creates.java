package models;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Creates {
    private long songId;
    private long artistId;
    private Boolean isCollabarator;

    public Creates(long songId, long artistId, boolean isCollabarator) {
        this.songId = songId;
        this.artistId = artistId;
        this.isCollabarator = isCollabarator;
    }
}
