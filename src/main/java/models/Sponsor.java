package models;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Sponsor {
    private long id;
    private String name;
    public Sponsor(String name) {
        this.name = name;
    }
}
