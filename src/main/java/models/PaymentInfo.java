package models;

import lombok.*;
import utils.PaymentUtils;

import java.sql.Timestamp;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PaymentInfo {
    private long senderId;
    private long receiverId;
    private double amount;
    private Timestamp timestamp;
    private PaymentUtils.Stakeholder senderType;
    private PaymentUtils.Stakeholder receiverType;
}
