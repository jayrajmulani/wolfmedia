package models;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class SongListen {
    private long songId;
    private long artistId;
    private Boolean isCollabarator;

    public SongListen(long songId, long artistId, boolean isCollabarator) {
        this.songId = songId;
        this.artistId = artistId;
        this.isCollabarator = isCollabarator;
    }
}
