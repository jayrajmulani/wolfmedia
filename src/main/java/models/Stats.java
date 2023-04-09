package models;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Stats {
    private Object obj;
    private int month;
    private int year;
    private long count;

    public Stats(Object obj, int year, long count) {
        this.obj = obj;
        this.year = year;
        this.count = count;
    }
}

