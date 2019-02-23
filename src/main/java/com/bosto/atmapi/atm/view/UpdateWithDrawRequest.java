package com.bosto.atmapi.atm.view;
import lombok.Data;

@Data
public class UpdateWithDrawRequest {
    String atmExternalId;
    Long withDrawId;
    Action action;
    String comment;
    String qrCodeString;
    String otp;

    public enum Action {
        APPROVE,
        REJECT,
        SCANATM,
        AUTHPASS;
    }
}
