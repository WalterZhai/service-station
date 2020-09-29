package com.cimctht.servicestation.common.utils;

import java.util.ArrayList;
import java.util.List;

public class CollectionsUtils {
	
	public static List<String> convertArrays2ListByString(String[] arr){
		List<String> list = new ArrayList<String>();
		for(String s : arr) {
			list.add(s);
		}
		return list;
	}
}
