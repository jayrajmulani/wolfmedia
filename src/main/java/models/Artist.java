package models;

import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Artist {
    private long id;
    private String name;
    private String country;
    private ArtistStatus status;
    private List<ArtistType> types;
    private Genre primaryGenre;
    private RecordLabel recordLabel;

    public Artist(long id) {
        this.id = id;
    }

    public Artist(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Artist(String name, String country, ArtistStatus status, List<ArtistType> types, Genre primaryGenre, RecordLabel recordLabel) {
        this.name = name;
        this.country = country;
        this.status = status;
        this.types = types;
        this.primaryGenre = primaryGenre;
        this.recordLabel = recordLabel;
    }

    public enum ArtistStatus {

        RETIRED("RETIRED"),
        ACTIVE("ACTIVE");

        private String status;

        ArtistStatus(String status) {
            this.status = status;
        }

        public String getStatus() {
            return status;
        }
    }

    public Artist(String name, String country, ArtistStatus status) {
        this.name = name;
        this.country = country;
        this.status = status;
    }

    public Artist(long id, String name, String country, ArtistStatus status) {
        this.id = id;
        this.name = name;
        this.country = country;
        this.status = status;
    }
}
