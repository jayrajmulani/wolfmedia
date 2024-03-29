package models;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class User {
    private long id;
    private String fName;
    private String lName;
    private Date regDate;
    private String phone;
    private String email;

    private Boolean premiumStatus;
    private double monthlyPremiumFees;

    public User(String fName, String lName, Date regDate, String phone, String email, Boolean premiumStatus, double monthlyPremiumFees) {
        this.fName = fName;
        this.lName = lName;
        this.regDate = regDate;
        this.phone = phone;
        this.email = email;
        this.premiumStatus = premiumStatus;
        this.monthlyPremiumFees = monthlyPremiumFees;
    }


    public User(long id, String fName, String lName, Boolean premiumStatus, double monthlyPremiumFees) {
        this.id = id;
        this.fName = fName;
        this.lName = lName;
        this.premiumStatus = premiumStatus;
        this.monthlyPremiumFees = monthlyPremiumFees;
    }
}
