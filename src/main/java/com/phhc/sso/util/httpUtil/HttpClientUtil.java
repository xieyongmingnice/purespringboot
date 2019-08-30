package com.phhc.sso.util.httpUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.Args;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * HttpClient工具类
 * @author wh
 * @date 2019/8/29
 * @since
 **/
@Component
public class HttpClientUtil {

	private static final Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);

	private static final ObjectMapper objectMapper = new ObjectMapper();

	public static final ContentType TEXT_PLAIN_UTF8 = ContentType.create("text/plain", Consts.UTF_8);
	public static final ContentType TEXT_PLAIN_ASCII = ContentType.create("text/plain", Consts.ASCII);

	private static final ResponseHandler<String> defaultHandler = new ResponseHandler<String>() {

		@Override
		public String handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();
				BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()));

				String line;
				StringBuilder sb = new StringBuilder();
				while ((line = reader.readLine()) != null) {
					sb.append(line);
				}
				String result = sb.toString();
				return result;
			} else {
				HttpEntity r_entity = response.getEntity();
				String responseString = EntityUtils.toString(r_entity);
				logger.error("httpclient 响应状态码不是 200 {}", responseString);
				return StringUtils.EMPTY;
			}
		}
	};

	@Autowired
	@Qualifier("httpClient")
	private CloseableHttpClient httpClient;

	/**
	 * 执行请求核心方法
	 * 
	 * @param httpUriRequest
	 * @param rh
	 * @return
	 */
	public <T> T doRequest(HttpUriRequest httpUriRequest, ResponseHandler<T> rh) {
		Args.notNull(httpUriRequest, "httpUriRequest 请求");
		Args.notNull(rh, "rh 响应处理器");

		T result = null;
		try {
			result = httpClient.execute(httpUriRequest, rh);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			logger.error("请求出错 {}", e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("请求出错 {}", e.getMessage());
		}
		return result;
	}

	/**
	 * post请求
	 * 
	 * @param url
	 * @param entity
	 * @param rh
	 * @return
	 */
	public <T> T doPost(String url, HttpEntity entity, ResponseHandler<T> rh) {
		Args.notBlank(url, "url 请求提交地址");
		Args.notNull(rh, "rh 响应处理器");

		HttpPost post = new HttpPost(url);
		post.setEntity(entity);

		T doRequest = doRequest(post, rh);

		return doRequest;

	}

	/**
	 * 使用默认处理器
	 * 
	 * @param url
	 * @param e
	 * @param filebytes
	 * @return
	 */
	public <E> String doPostMultipartForm(String url, E e, Map<String, byte[]> filebytes) {
		return this.doPostMultipartForm(url, e, filebytes, defaultHandler);
	}

	public String doPostMultipartForm(String url, Map<String, byte[]> filebytes) {
		return this.doPostMultipartForm(url, null, filebytes);
	}

	/**
	 * 无附加参数 上传文件
	 * 
	 * @param url
	 * @param filebytes
	 * @param rh
	 * @return
	 */
	public <T> T doPostMultipartForm(String url, Map<String, byte[]> filebytes, ResponseHandler<T> rh) {
		return this.doPostMultipartForm(url, null, filebytes, rh);
	}

	/**
	 * MultipartForm utf8
	 * 
	 * @param url
	 *            请求地址
	 * @param e
	 *            中 不能包含 byte[],file,inputStream，等未实现 Serializable 的类
	 * @param rh
	 *            响应处理类
	 * @return
	 */
	public <T, E> T doPostMultipartForm(String url, E e, Map<String, byte[]> filebytes, ResponseHandler<T> rh) {
		Args.notBlank(url, "url 请求提交地址");
		Args.notNull(rh, "rh 响应处理器");

		MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create().setStrictMode()
				.setBoundary("tdprequestLinkfaceBoundary");

		if (null != e) {
			@SuppressWarnings("unchecked")
			Map<String, Object> map = objectMapper.convertValue(e, Map.class);
			// 添加键值对参数
			if (MapUtils.isNotEmpty(map))
				for (Map.Entry<String, Object> param : map.entrySet()) {
					multipartEntityBuilder.addTextBody(param.getKey(), String.valueOf(param.getValue()),
							TEXT_PLAIN_UTF8);
				}
		}
		// 添加文件参数
		if (MapUtils.isNotEmpty(filebytes)) {
			AtomicInteger atomicInteger = new AtomicInteger(0);
			for (Map.Entry<String, byte[]> fileByte : filebytes.entrySet()) {
				multipartEntityBuilder.addBinaryBody(fileByte.getKey(), fileByte.getValue(),
						ContentType.APPLICATION_OCTET_STREAM, "file" + atomicInteger.incrementAndGet());
			}
		}

		HttpEntity entity = multipartEntityBuilder.build();

		T doPost = doPost(url, entity, rh);

		return doPost;

	}

	/**
	 * 使用默认处理器提交form表单
	 * 
	 * @param url
	 * @param e
	 * @return
	 */
	public <E> String doPostUrlEncodedForm(String url, E e) {
		return this.doPostUrlEncodedForm(url, e, defaultHandler);
	}

	/**
	 * UrlEncodedForm urf8
	 * 
	 * @param url
	 * @param e
	 * @param rh
	 * @return
	 */
	public <T, E> T doPostUrlEncodedForm(String url, E e, ResponseHandler<T> rh) {
		Args.notBlank(url, "url 请求提交地址");
		Args.notNull(rh, "rh 响应处理器");

		ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
		if (null != e) {
			@SuppressWarnings("unchecked")
			Map<String, Object> params = objectMapper.convertValue(e, Map.class);

			if (CollectionUtils.isNotEmpty(pairs)) {
				for (Map.Entry<String, Object> param : params.entrySet()) {
					pairs.add(new BasicNameValuePair(param.getKey(), String.valueOf(param.getValue())));
				}
			}
		}
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(pairs, Consts.UTF_8);

		T doPost = doPost(url, entity, rh);

		return doPost;
	}

	/**
	 * 提交json 数据
	 * 
	 * @param url
	 * @param e
	 * @return
	 * @throws JsonProcessingException
	 * @throws UnsupportedEncodingException
	 */
	public <E> String doPostJSON(String url, E e) throws JsonProcessingException, UnsupportedEncodingException {
		Args.notBlank(url, "url 请求提交地址");

		return this.doPostJSON(url, e, defaultHandler, Boolean.FALSE);

	}

	/**
	 * 提交json 数据
	 * 
	 * @param url
	 * @param e
	 * @param isUrlEncoder
	 *            是否需要url编码
	 * @return
	 * @throws JsonProcessingException
	 * @throws UnsupportedEncodingException
	 */
	public <E> String doPostJSON(String url, E e, Boolean isUrlEncoder, Boolean isUrlDecoder)
			throws JsonProcessingException, UnsupportedEncodingException {
		Args.notBlank(url, "url 请求提交地址");

		String doPostJSON = this.doPostJSON(url, e, defaultHandler, isUrlEncoder);
		
		if(isUrlDecoder && !StringUtils.EMPTY.equals(doPostJSON)) {
			doPostJSON = URLDecoder.decode(doPostJSON, "UTF-8");
		}
		return doPostJSON;

	}

	/**
	 * 提交json 数据
	 * 
	 * @param url
	 * @param e
	 * @param rh
	 * @return
	 * @throws JsonProcessingException
	 * @throws UnsupportedEncodingException
	 */
	public <T, E> T doPostJSON(String url, E e, ResponseHandler<T> rh, Boolean isUrlEncoder)
			throws JsonProcessingException, UnsupportedEncodingException {
		Args.notBlank(url, "url 请求提交地址");
		Args.notNull(rh, "rh 响应处理器");

		String writeValueAsString = objectMapper.writeValueAsString(e);

		if (isUrlEncoder) {
			writeValueAsString = URLEncoder.encode(writeValueAsString, "UTF-8");
		}
		StringEntity entity = new StringEntity(writeValueAsString, ContentType.APPLICATION_JSON);

		T doPost = doPost(url, entity, rh);

		return doPost;
	}

	public  <E, T> T doJdHttpPost(String url, E e ,TypeReference<?> valueTypeRef) {

		try {
			String doPostJSON = this.doPostJSON(url, e, Boolean.TRUE, Boolean.TRUE);
			//判断返回结果不是空串
			if(!StringUtils.EMPTY.equals(doPostJSON)) {
				T result = objectMapper.readValue(doPostJSON, valueTypeRef);

				return result;
			}
		} catch (IOException ex) {

			// LOGGER.error("京东金融请求核心方法出错 url:{},参数:{},异常",url,e,ex.getMessage());
		}
				/*	text/plain;charset=UTF-8
				%7B%22code%22%3A%221000%22%2C%22data%22%3A%7B%22exist%22%3A%220%22%2C%22hei_score%22%3A0%2C%22hei_src%22%3A%22-1%22%7D%2C%22msg%22%3A%22%E8%B0%83%E7%94%A8%E6%88%90%E5%8A%9F%2C%E6%9C%89%E6%95%88%E6%95%B0%E6%8D%AE%22%7D
				{"code":"1000","data":{"exist":"0","hei_score":0,"hei_src":"-1"},"msg":"调用成功,有效数据"}*/


		return null;
	}

}
