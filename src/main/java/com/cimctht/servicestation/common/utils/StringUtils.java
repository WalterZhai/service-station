package com.cimctht.servicestation.common.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class StringUtils {

	/**
	 * 将string 按分隔符分隔返回list
	 * @return
	 */
	public static List<String> transString2ListString(String str,String reg) {
		List<String> list = new ArrayList<String>();
		String[] arr = str.split(reg);
		for(String s : arr) {
			list.add(s);
		}
		return list;
	}
	
	
	public static boolean isEmpty(String str) {
		if("".equals(str)) {
			return true;
		}else if(null==str) {
			return true;
		}
		return false;
	}
	
	/**
	 * 将list<String>修改成?,?,?的形式
	 * @return
	 */
	public static String list2String(List<String> list) {
		String s = "";
		for(String l : list) {
			if(s.equals("")) {
				s = l;
			}else {
				s = s + "," + l;
			}
		}
		return s;
	}
	
	/**
	 * 将list<String>改成in后面的形式
	 * @return
	 */
	public static String list2InString(List<String> list) {
		String s = "";
		for(String l : list) {
			if(s.equals("")) {
				s = "'"+l+"'";
			}else {
				s = s + ",'" + l + "'";
			}
		}
		return s;
	}
	
	
	/**
	 * 将String[]改成in后面的形式
	 * @return
	 */
	public static String arr2InString(String[] arr) {
		String s = "";
		for(String l : arr) {
			if(s.equals("")) {
				s = "'"+l+"'";
			}else {
				s = s + ",'" + l + "'";
			}
		}
		return s;
	}
	
	/**
	 * 生成GID
	 * @return
	 */
	public static String generteUUID() {
		 return UUID.randomUUID().toString().replace("-", "").toLowerCase();
	}
	
	/**
	 * 左补位
	 * @param s
	 * @param len
	 * @param c
	 * @return
	 */
	public static String padLeft(String s,Integer len, String c) {
		if(s.length()>=len) {
			return s;
		}
		Integer ct = len - s.length();
		for(int i = 1; i<=ct ; i++) {
			s = c + s;
		}
		return s;
	}
	
	/**
	 * 右补位
	 * @param s
	 * @param len
	 * @param c
	 * @return
	 */
	public static String padRight(String s,Integer len, String c) {
		if(s.length()>=len) {
			return s;
		}
		Integer ct = len - s.length();
		for(int i = 1; i<=ct ; i++) {
			s = s + c;
		}
		return s;
	}
	
}
