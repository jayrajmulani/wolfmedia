package models;

import lombok.*;

import java.util.Date;

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

    public Artist(long id) {
        this.id = id;
    }
    public Artist(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public enum ArtistStatus{

        RETIRED ("RETIRED"),
        ACTIVE ("ACTIVE");

        private String status;

        ArtistStatus(String status)
        {
            this.status = status;
        }

        public String getStatus()
        {
            return status;
        }
    }
    public Artist(String name, String country, ArtistStatus status) {
        this.name = name;
        this.country = country;
        this.status = status;
    }

}
