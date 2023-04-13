package models;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class SongAlbum {
    private Song song;
    private Album album;
    private long trackNum;

    public SongAlbum(Song song) {
        this.song = song;
    }
}


