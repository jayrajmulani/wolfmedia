package models;

import lombok.*;

import java.sql.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class RecordLabel {
    long id;
    String name;
    public RecordLabel(String name) {
        this.name = name;
    }
}
