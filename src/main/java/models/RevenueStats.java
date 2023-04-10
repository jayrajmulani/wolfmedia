package models;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class RevenueStats {
    private Object obj;
    private int month;
    private int year;
    private double amount;

    public RevenueStats(Object obj, int year, double amount) {
        this.obj = obj;
        this.year = year;
        this.amount = amount;
    }
}

