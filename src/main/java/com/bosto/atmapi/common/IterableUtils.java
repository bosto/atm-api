package com.bosto.atmapi.common;

import java.util.ArrayList;
import java.util.List;

public class IterableUtils {
	public static List iterableTolist(Iterable iterable) {
		List list = new ArrayList();
		iterable.forEach(it -> list.add(it));
		return list;
	}
}
