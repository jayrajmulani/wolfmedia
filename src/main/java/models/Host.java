package models;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Host {
    long id;
    String firstName;
    String lastName;
    String city;
    String email;
    String phone;
    List<Podcast> podcasts;

    public Host(long id){
        this.id = id;
    }
    public Host(long id, String firstName, String lastName){
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }
    public Host(String firstName, String lastName, String city, String email, String phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.city = city;
        this.email = email;
        this.phone = phone;
//        this.podcasts = podcasts;
    }
}
