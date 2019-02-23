package com.bosto.atmapi.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by bosto on 2018/12/22
 **/
@Data
@AllArgsConstructor
public class Error {
	String code;
	String errorDetail;
}
