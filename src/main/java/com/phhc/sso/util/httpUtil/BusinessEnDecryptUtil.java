package com.phhc.sso.util.httpUtil;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class BusinessEnDecryptUtil {

	private static final ObjectMapper mapper = new ObjectMapper();
	
	/**
	 * 填充字符串
	 */
	private static final char PADDING_CHAR = '#';
	
	/**
	 * 填充长度
	 */
	private static final int PADDING_LENGHT = 30;
	
	private static final String PASSWORD = "zhongan";//秘钥
	
	private static final byte[] SALT =  "tdpplatf".getBytes();//盐值
	

	public static ObjectMapper getObjectMapper() {
		return mapper;
	}

	public static <T> T convertJsonStringToPojo(String jsonString, Class<T> clazz)
			throws JsonParseException, JsonMappingException, IOException {
		T pojo = getObjectMapper().readValue(jsonString, clazz);
		return pojo;
	}

	public static <T, E> T convertBeanToBean(E e, Class<T> clazz) throws IllegalArgumentException {
		T pojo = getObjectMapper().convertValue(e, clazz);
		return pojo;
	}

	/**
	 *  ： 将customerId 转换为 thirdUserNo
	 * 
	 * @param customerId
	 * @return
	 */
	public static String convertCustomerIdToThirdUserNo(Long customerId) {
		String customerIdString = customerId.toString();
		if(customerIdString.length() < 30) {
			int len = (PADDING_LENGHT - customerId.toString().length());
			String repeat = StringUtils.repeat(PADDING_CHAR, len);
			customerIdString = customerIdString + repeat;
		}
		String thirdUserNo = EnDecryptUtil.encrypt(customerIdString, PASSWORD, SALT);
		
		return thirdUserNo;
	}

	/**
	 * ：将 thirdUserNo 转换为 customerId
	 * @param thirdUserNo
	 * @return
	 */
	public static Long convertThirdUserNoToCustomerId(String thirdUserNo) {
		String customerIdString = EnDecryptUtil.decrypt(thirdUserNo, PASSWORD, SALT);
		int pad_index = customerIdString.indexOf(PADDING_CHAR);
		if(pad_index > 0) {
			customerIdString = customerIdString.substring(0, pad_index);
		}
		Long customerId = Long.parseLong(customerIdString);
		return customerId;         
	}
	
	/**
	 *  ：将 orderId 转换为 partnerOrderNo
	 * @param orderId
	 * @return
	 */
	public static String convertOrderIdToPartnerOrderNo(Long orderId) {
		String partnerOrderNo = convertCustomerIdToThirdUserNo(orderId); 
		return partnerOrderNo;
	}
	
	/**
	 * 众安 ：将 partnerOrderNo 转换为 orderId
	 * @param partnerOrderNo
	 * @return
	 */
	public static Long convertPartnerOrderNoToOrderId(String partnerOrderNo) {
		Long orderId = convertThirdUserNoToCustomerId(partnerOrderNo); 
		return orderId;
	}
	
	/**
	 *  ：将 BusinessId 转换为 mshSellerID
	 * @param businessId
	 * @return
	 */
	public static String convertBusinessIdToMshSellerID(Long businessId) {
		String mshSellerID = convertCustomerIdToThirdUserNo(businessId); 
		return mshSellerID;
	}
	
	/**
	 *  ：将 partnerOrderNo 转换为 orderId
	 * @param mshSellerID
	 * @return
	 */
	public static Long convertMshSellerIDToBusinessId(String mshSellerID) {
		Long businessId = convertThirdUserNoToCustomerId(mshSellerID); 
		return businessId;
	}
	
	/*public static void main(String[] args) {
		
		Long customerId = 1L;
		         
		String thirdUserNo = convertCustomerIdToThirdUserNo(customerId);
		System.out.println(thirdUserNo);
		System.out.println(thirdUserNo.length());
		
		Long customerId2 = convertThirdUserNoToCustomerId(thirdUserNo);
		
		System.out.println(customerId2);
		
		System.out.println(customerId.compareTo(customerId2));
	
	}*/
}
