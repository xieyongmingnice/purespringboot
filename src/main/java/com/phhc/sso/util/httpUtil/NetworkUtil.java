package com.phhc.sso.util.httpUtil;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.BrowserType;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;

/**
 * 常用获取客户端信息的工具
 * @author wh
 * @date 2019/8/29
 * @since
 **/
public final class NetworkUtil {
	/**
	 * Logger for this class
	 */
	private static Logger logger = LoggerFactory.getLogger(NetworkUtil.class);
	
	/**
	 * ip 地址正则
	 */
	private static String regex = "(((2[0-4]d)|(25[0-5]))|(1d{2})|([1-9]d)|(d))[.](((2[0-4]d)|(25[0-5]))|(1d{2})|([1-9]d)|(d))[.]"
	            + "(((2[0-4]d)|(25[0-5]))|(1d{2})|([1-9]d)|(d))[.](((2[0-4]d)|(25[0-5]))|(1d{2})|([1-9]d)|(d))";
	private static Pattern p = Pattern.compile(regex);
	
	
	/****

	取所有IP段的私有IP段
	A类 私有地址 10.0.0.0---10.255.255.255 保留地址 127.0.0.0---127.255.255.255
	B类 私有地址 172.16.0.0-172.31.255.255
	C类 私有地址 192.168.0.0-192.168.255.255
	D类 地址不分网络地址和主机地址
	E类 地址不分网络地址和主机地址
	*/
	private static long aBegin = getIpNum("10.0.0.0");
	private static long aEnd = getIpNum("10.255.255.255");
	private static long bBegin = getIpNum("172.16.0.0");
	private static long bEnd = getIpNum("172.31.255.255");
	private static long cBegin = getIpNum("192.168.0.0");
	private static long cEnd = getIpNum("192.168.255.255");
	private static long saveBegin = getIpNum("127.0.0.0");
	private static long saveEnd = getIpNum("127.255.255.255");

	/**
	 * 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址;
	 * 
	 * @param request
	 * @return
	 * @throws IOException
	 */
	public final static String getIpAddress(HttpServletRequest request) throws IOException {
		// 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址
		String ip = request.getHeader("clientip");
		if (logger.isInfoEnabled()) {
			logger.info("getIpAddress(HttpServletRequest) - clientip - String ip=" + ip);
		}
		
		if (ip == null || ip.length() == 0  ||"unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("X-Forwarded-For");
			if (logger.isInfoEnabled()) {
				logger.info("getIpAddress(HttpServletRequest) - X-Forwarded-For - String ip=" + ip);
			}
		}
		 

		if (ip == null || ip.length() == 0  || "unknown".equalsIgnoreCase(ip)) {
			
				ip = request.getHeader("Proxy-Client-IP");
				if (logger.isInfoEnabled()) {
					logger.info("getIpAddress(HttpServletRequest) - Proxy-Client-IP - String ip=" + ip);
				}
			
			if (ip == null || ip.length() == 0  || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("WL-Proxy-Client-IP");
				if (logger.isInfoEnabled()) {
					logger.info("getIpAddress(HttpServletRequest) - WL-Proxy-Client-IP - String ip=" + ip);
				}
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("HTTP_CLIENT_IP");
				if (logger.isInfoEnabled()) {
					logger.info("getIpAddress(HttpServletRequest) - HTTP_CLIENT_IP - String ip=" + ip);
				}
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("HTTP_X_FORWARDED_FOR");
				if (logger.isInfoEnabled()) {
					logger.info("getIpAddress(HttpServletRequest) - HTTP_X_FORWARDED_FOR - String ip=" + ip);
				}
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip) ) {
				ip = request.getRemoteAddr();
				if (logger.isInfoEnabled()) {
					logger.info("getIpAddress(HttpServletRequest) - getRemoteAddr - String ip=" + ip);
				}
			}
		}	
			
		if (null != ip && ip.length() > 15) {
			String[] ips = ip.split(",");
			for (int index = 0; index < ips.length; index++) {
				String strIp = (String) ips[index];
				if (!("unknown".equalsIgnoreCase(strIp))) {
					ip = strIp;
					break;
				}
			}
		}
		if (ip.equals("0:0:0:0:0:0:0:1")) {
			ip = "127.0.0.1";
		}
		return ip;
	}
	
	/*
	public static String  getMACAddress(String ip){   
	    String str = "";   
	    String macAddress = "";   
	    try {   
	        Process p = Runtime.getRuntime().exec("nbtstat -A " + ip);   
	        InputStreamReader ir = new InputStreamReader(p.getInputStream());   
	        LineNumberReader input = new LineNumberReader(ir);   
	        for (int i = 1; i < 100; i++) {   
	            str = input.readLine();   
	            if (str != null) {   
	                if (str.indexOf("MAC Address") > 1) {   
	                    macAddress = str.substring(str.indexOf("MAC Address") + 14, str.length());   
	                    break;   
	                }   
	            }   
	        }   
	    } catch (IOException e) {   
	        e.printStackTrace(System.out);   
	    }   
	    return macAddress;   
	} */
	
	/**判断是否是私有IP地址
	 * @param ipAddress
	 * @return
	 */
	public static boolean isInnerIP(String ipAddress) {
	    boolean isInnerIp = false;
	    long ipNum = getIpNum(ipAddress);
	    isInnerIp = isInner(ipNum, aBegin, aEnd) || isInner(ipNum, bBegin, bEnd) || isInner(ipNum, cBegin, cEnd) || isInner(ipNum, saveBegin, saveEnd);
	    return isInnerIp;
	}

	private static long getIpNum(String ipAddress) {
	    String[] ip = ipAddress.split("\\.");
	    long a = Integer.parseInt(ip[0]);
	    long b = Integer.parseInt(ip[1]);
	    long c = Integer.parseInt(ip[2]);
	    long d = Integer.parseInt(ip[3]);

	    long ipNum = a * 256 * 256 * 256 + b * 256 * 256 + c * 256 + d;
	    return ipNum;
	}

	private static boolean isInner(long userIp, long begin, long end) {
	    return (userIp >= begin) && (userIp <= end);
	}


	/**
	 * 检验是否是合法的IP地址 
	 */
	public static boolean isIpAddress(String address) {
	    Matcher m = p.matcher(address);
	    return m.matches();
	}
	
	/**
	 * 操作系统，浏览器类型，设备类型(手机，电脑。。。。。)
	 * @param request
	 * @return
	 */
	public static String  getUserAgent(HttpServletRequest request){  
		UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
		OperatingSystem operatingSystem = userAgent.getOperatingSystem();
		
		return userAgent.toString()+"-"+operatingSystem.getDeviceType();
		
		
	}
}