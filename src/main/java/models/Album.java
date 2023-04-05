package models;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Album {
    private long id;
    private String name;
    private Date release_date;
    private int edition;

    public Album(String name, Date release_date, int edition) {
        this.name = name;
        this.release_date = release_date;
        this.edition = edition;
    }
}