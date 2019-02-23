package com.bosto.atmapi.exception;

import lombok.Data;

/**
 * Created by bosto on 2018/12/22
 **/
@Data
public class BaseException extends RuntimeException {
	String code;
	String errorDetail;

	public BaseException() {}

	public BaseException(String code, String errorDetail) {
		this.code = code;
		this.errorDetail = errorDetail;
	}

	public static Error toError(BaseException baseException) {
		return new Error(baseException.getCode(), baseException.getErrorDetail());
	}
}
