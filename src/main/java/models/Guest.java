package models;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Guest {
    private long id;
    private String name;

    public Guest(String name) {
        this.name = name;
    }
}
