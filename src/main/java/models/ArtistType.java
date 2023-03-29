package models;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class ArtistType {
    private long id;
    private String name;
    public ArtistType(String name) {
        this.name = name;
    }
}
