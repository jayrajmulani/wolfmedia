package models;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Genre {
   private long id;
   private String name;
   public Genre(String name) {
       this.name = name;
   }
}
