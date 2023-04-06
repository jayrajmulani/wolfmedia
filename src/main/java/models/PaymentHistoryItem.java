package models;

import utils.PaymentUtils;

import java.sql.Timestamp;

public class PaymentHistoryItem {
    private PaymentUtils.Stakeholder senderType;
    private Object sender;
    private PaymentUtils.Stakeholder receiverType;
    private Object receiver;
    private Timestamp timestamp;
    private double amount;
}
