package models;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class SongAlbum {
    private long songId;
    private long albumId;
    private long trackNum;
}
