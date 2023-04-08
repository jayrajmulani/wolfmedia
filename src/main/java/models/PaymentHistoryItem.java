package models;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;
import utils.PaymentUtils;

import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PaymentHistoryItem {
    private PaymentUtils.Stakeholder senderType;
    private Object sender;
    private PaymentUtils.Stakeholder receiverType;
    private Object receiver;
    private Timestamp timestamp;
    private double amount;
}
