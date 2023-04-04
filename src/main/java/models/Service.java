package models;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Service {
    private long id;
    private String name;
    private double balance;

    public Service(String name, double balance) {
        this.name = name; this.balance = balance;
    }
}
