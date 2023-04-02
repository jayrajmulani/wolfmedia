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
public class Host {
    long id;
    String first_name;
    String last_name;
    String city;
    String email;
    String phone;
//    TODO Link Podcast when ready, to be applied with UPDATE not CREATE
//    List<Podcast> podcasts;
    public Host(String first_name, String last_name, String city, String email, String phone) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.city = city;
        this.email = email;
        this.phone = phone;
//        this.podcasts = podcasts;
    }
}
