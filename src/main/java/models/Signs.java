package models;

import lombok.*;

import java.sql.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Signs {
    private long artistId;
    private long recordLabelId;
    private Date updatedAt;
}
