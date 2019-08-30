package com.phhc.sso.util.httpUtil;

import java.io.Serializable;
/**
 * http请求返回父类
 * @author wh
 * @date 2019/8/29
 * @since
 **/
public class BaseResponse implements Serializable {

	private static final long serialVersionUID = 1L;
	
	 public static BaseResponse SUCCESSFUL() {
	        return new BaseResponse("0","处理成功");
	    }

	    public static BaseResponse FAILED() {
	        return new BaseResponse("1","处理失败");
	    }

	    

    
    public BaseResponse(String code,String message){
        this.code = code;
        this.message = message;
    }

    public BaseResponse() {
	}
	

	/**
     * 结果码
     */
    private String code;

    /**
     * 消息内容
     */
    private String message;


	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setMessage(String message) {
		this.message = message;
	}
    
    
}
