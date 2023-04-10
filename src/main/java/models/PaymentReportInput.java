package models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import utils.PaymentUtils;

import java.sql.Date;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class PaymentReportInput {
    private Date startDate;
    private Date endDate;
    private long id;
    private PaymentUtils.Stakeholder stakeholder;
}
