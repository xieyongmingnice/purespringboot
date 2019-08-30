package com.phhc.sso.util.httpUtil;

import java.util.Set;

import org.apache.http.Consts;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.config.RequestConfig.Builder;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.pool.PoolStats;
import org.apache.http.protocol.HttpContext;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HttpClientConfiguration {
	
	/**
	 * httpConnection 连接配置
	 * @return
	 */
	@Bean(name = "connectionConfig")
	public ConnectionConfig connectionConfig(){
		 ConnectionConfig connectionConfig = ConnectionConfig.custom()
				 .setBufferSize(8*1024)
				 .setFragmentSizeHint(8*1024)
	            .setCharset(Consts.UTF_8)
	            .build();
		 
		 return connectionConfig;
	}
	
	/**
	 * http连接管理器
	 * @param connectionConfig
	 * @return
	 */
	@Bean(name = "httpClientConnectionManager")
	public PoolingHttpClientConnectionManager poolingHttpClientConnectionManager(@Qualifier("connectionConfig")ConnectionConfig connectionConfig) {
		PoolingHttpClientConnectionManager pooling

				= new PoolingHttpClientConnectionManager();

		pooling.setMaxTotal(20);// 总的httpconnection 连接数
		pooling.setDefaultMaxPerRoute(20);//默认每个地址的连接数
		pooling.setValidateAfterInactivity(1000*10);/// 空闲永久连接检查间隔，可能会导致刚创建的连接 ，就过期
		  // 通过判断 entry的 更新时间默认 2000；
		pooling.setDefaultConnectionConfig(connectionConfig);
		return pooling;
	}

	@Bean(name = "requestConfigBuilder")
	public Builder requestConfigBuilder() {//设置 获取httpconnection的超时时间，tcp握手超时时间，通信超时时间
		Builder builder = RequestConfig.custom()
				.setConnectionRequestTimeout(1000 * 2)
				.setConnectTimeout(1000 * 2)
				.setSocketTimeout(1000 * 8)
				.setCookieSpec(CookieSpecs.IGNORE_COOKIES)
				.setAuthenticationEnabled(false);
		return builder;
	}

	/**
	 * 请求参数配置
	 * @param builder
	 * @return
	 */
	@Bean(name = "requestConfig")
	public RequestConfig requestConfig(@Qualifier("requestConfigBuilder") Builder builder) {
		RequestConfig requestConfig = builder.build();
		return requestConfig;
	}
	
	/**
	 * 连接保持策略
	 * @return
	 */
	@Bean(name = "keepAliveStrategy")
	public ConnectionKeepAliveStrategy connectionKeepAliveStrategy() {
		return new DefaultConnectionKeepAliveStrategy() {  
			  
		    @Override  
		    public long getKeepAliveDuration(  
		            HttpResponse response,  
		            HttpContext context) {  
		        long keepAlive = super.getKeepAliveDuration(response, context);  
		        if (keepAlive == -1) {  
		            keepAlive = 60000;  
		        }  
		        return keepAlive;  
		    }  
		  
		};
	}
	
	 

	@Bean(name = "httpClientBuilder")
	public HttpClientBuilder httpClientBuilder(
			@Qualifier("httpClientConnectionManager") HttpClientConnectionManager pooling,
			@Qualifier("requestConfig") RequestConfig requestConfig,
			@Qualifier("keepAliveStrategy") ConnectionKeepAliveStrategy connectionKeepAliveStrategy) {
		
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		
		httpClientBuilder.setConnectionManager(pooling)
							.setDefaultRequestConfig(requestConfig)
							.setKeepAliveStrategy(connectionKeepAliveStrategy)
							.setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36")
							.disableAuthCaching()
							.disableCookieManagement()
							.disableConnectionState();

		
		return httpClientBuilder;
	}

	@Bean(name = "httpClient")
	public CloseableHttpClient closeableHttpClient(@Qualifier("httpClientBuilder") HttpClientBuilder builder) {
		CloseableHttpClient client = builder.build();
		return client;
	}

	/*@Bean(initMethod = "start", destroyMethod = "shutdown")
	public IdleConnectionEvictor idleConnectionEvictor(
			@Qualifier("linkfaceHttpClientConnectionManager") PoolingHttpClientConnectionManager pooling) {
		IdleConnectionEvictor connectionEvictor = new IdleConnectionEvictor(pooling);
		return connectionEvictor;
	}*/
	
	/*private HttpRoutePlanner routePlanner = new HttpRoutePlanner() {
		
		@Override
		public HttpRoute determineRoute(HttpHost target, HttpRequest request, HttpContext context) throws HttpException {
			
			return new HttpRoute(target, null, new HttpHost("127.0.0.1",8888), "https".equals(target.getSchemeName()));
		}
	};*/

	/**
	 * 关闭空闲连接
	 * 
	 * @author tian
	 *
	 *//*
	class IdleConnectionEvictor extends Thread {
		private Logger logger = Logger.getLogger(IdleConnectionEvictor.class);
		private final PoolingHttpClientConnectionManager connMgr;

		private volatile boolean shutdown;

		public IdleConnectionEvictor(PoolingHttpClientConnectionManager connMgr) {
			this.connMgr = connMgr;
			super.setName("LinkFaceEvictor");
			super.setDaemon(true);
		}

		@Override
		public void run() {
			try {
				while (!shutdown) {
					synchronized (this) {
						// 每隔10秒执行一个，关闭失效的http连接
						formatStats();
						wait(1000*10);
						// 关闭失效的连接
						connMgr.closeExpiredConnections();
					}
				}
			} catch (InterruptedException ex) {
				// 结束
			}
		}

		public void shutdown() {
			shutdown = true;
			synchronized (this) {
				notifyAll();
			}
		}
		
		
		private void formatStats() {
	        final PoolStats totals = this.connMgr.getTotalStats();
	        if(totals.getAvailable() > 0 || totals.getLeased() > 0){
		        final StringBuilder buf = new StringBuilder();
		        buf.append("[total kept alive: ").append(totals.getAvailable()).append("; ");
		        buf.append("total allocated: ").append(totals.getLeased() + totals.getAvailable());
		        buf.append(" of ").append(totals.getMax()).append("]\r\n");
		        
		        final Set<HttpRoute> routes = this.connMgr.getRoutes();
		        for (HttpRoute httpRoute : routes) {
		        	final PoolStats stats = this.connMgr.getStats(httpRoute);
		        	buf.append("route(").append(httpRoute.toString()).append(")allocated: ").append(stats.getLeased() + stats.getAvailable());
		 	        buf.append(" of ").append(stats.getMax()).append("; \r\n");
				}
		        logger.debug(buf.toString());
	        }
	    }
	}*/
}
