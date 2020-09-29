package com.cimctht.servicestation.common.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MathsUtils {

	/**
	 * 将BigDecimal转换为百分百
	 * @return
	 */
	public static String convertBigDecimal2HundredPercent(BigDecimal big) {
		String result = big.multiply(new BigDecimal(100)).setScale(0, BigDecimal.ROUND_DOWN).toString()+"%";
		return result;
	}
	
	/**
	 * 将BigDecimal转换为百分百
	 * @return
	 */
	public static String convertBigDecimal2HundredPercent(BigDecimal big,Integer decimal) {
		String result = big.multiply(new BigDecimal(100)).setScale(decimal, BigDecimal.ROUND_DOWN).toString()+"%";
		return result;
	}


	/**
	 * 将Integer转换为BigDecimal
	 * @return
	 */
	public static BigDecimal convertInteger2BigDecimal(Integer decimal) {
		BigDecimal b = new BigDecimal(decimal);
		return b;
	}

	/**
	 * 将Long转换为BigDecimal
	 * @return
	 */
	public static BigDecimal convertLong2BigDecimal(Long decimal) {
		BigDecimal b = new BigDecimal(decimal);
		return b;
	}
}
