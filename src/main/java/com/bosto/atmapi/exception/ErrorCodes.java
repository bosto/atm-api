package com.bosto.atmapi.exception;

import java.util.Map;

import static com.bosto.atmapi.common.Maps.*;


public class ErrorCodes {

    public static final Map LOGON_FAIL = map("logon","fail");
    public static final Map VALIDATE_TOKEN_FAIL = map("validate","fail");
    public static final Map WD_NOT_FOUND= map("withDraw","not found");
    public static final String ERROR_MESSAGE_KEY = "errorDetail";


    public static final String USER_NAME_DUPLICATE = "001";
}
