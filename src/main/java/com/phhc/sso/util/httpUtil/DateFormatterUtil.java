package com.phhc.sso.util.httpUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * 请填写类描述
 * @author wh
 * @date 2019/8/29
 * @since
 **/
public class DateFormatterUtil {

	private static SimpleDateFormat StyleOne = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	
	
	/**
	 * 格式化日期为字符串 ，返回格式 yyyy:MM:dd hh:mm:ss
	 * @param date
	 * @return
	 */
	public static String styleOneFormatDate(Date date) {
		if(null == date) {
			return "";
		}
		return StyleOne.format(date);
	}
}
