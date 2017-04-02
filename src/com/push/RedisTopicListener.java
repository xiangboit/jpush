package com.push;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import Redis.utils.JedisUtils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

/**
 * Created on 2014-3-15
 * <p>Description: [描述该类概要功能介绍]</p>
 */
public class RedisTopicListener  extends JedisPubSub {
	private static Logger logger = LoggerFactory
			.getLogger(RedisTopicListener.class);
	private Queue<AsyncContext> asyncContexts = new ConcurrentLinkedQueue<AsyncContext>();

	public void addAsyncContexts(AsyncContext asyncContext){
	    asyncContexts.add(asyncContext);
	}

	public RedisTopicListener(){
	    
	}
	

 
	   /*

     * 常规模式：关闭订阅时触发

     * channel key值

     * subscribedChannels 订阅数量

     */
		@Override
    public void onUnsubscribe(String channel, int subscribedChannels) {
			logger.info("调用的方法为{},功能为{},\n参数为[{}],[{}]--->>【{}】【{}】",
					new Object[]{"onUnsubscribe","常规模式：关闭订阅时触发","String channel","int subscribedChannels",channel,subscribedChannels});
		
    }

     /*

     * 常规模式：启动订阅时触发

     * channel key值

     * subscribedChannels 订阅数量

     */
    @Override
    public void onSubscribe(String channel, int subscribedChannels) {
    	logger.info("调用的方法为{},功能为{},\n参数为[{}],[{}]--->>【{}】【{}】",
				new Object[]{"onSubscribe","常规模式：启动订阅时触发","String channel","int subscribedChannels",channel,subscribedChannels});
//    	Jedis jedis=JedisUtils.getJedis();
//		UUID uuid=UUID.randomUUID();
//		jedis.sadd(channel, uuid.toString());
//		JedisUtils.returnJedis(jedis);
//		logger.info("存入UUID,key值为{},value为{}",channel,uuid.toString());
		if(!channel.equals("initChannel")){
		   for( AsyncContext asyncContext : asyncContexts){
			   HttpServletRequest request = (HttpServletRequest) asyncContext.getRequest();
			   if(request.getParameter("wd")!=null&&request.getParameter("wd").equals(channel)){
			       String uuid=null;
			       if(request.getParameter("uuid")!=null){
				   uuid=request.getParameter("uuid");
			       }else{
				   uuid= UUID.randomUUID().toString();
			       }
			    	Jedis jedis=JedisUtils.getJedis();
			    	jedis.sadd(channel, uuid);
				JedisUtils.returnJedis(jedis);
			       HttpServletResponse peer = (HttpServletResponse) asyncContext.getResponse();
				    Map<String, String> map = new HashMap<String, String>();
					map.put("subMsg", "订阅了");
					map.put("result", "true");
					map.put("uuid", uuid);
				  
				    try {
					peer.getWriter().print( new JSONArray().put(map).toString());
					String s=new JSONArray().put(map).toString();
					System.out.println(s);
				    } catch (IOException e) {
			
					e.printStackTrace();
				    }
				    peer.setStatus(HttpServletResponse.SC_OK);
				    peer.setContentType("application/json");
				    asyncContext.complete();
				    System.out.println("==============111asyncContexts.size()"+asyncContexts.size());
			       asyncContexts.remove(asyncContext);
			       System.out.println("==============222asyncContexts.size()"+asyncContexts.size());
			       logger.info("删除了一个asyncContext");
			       break;
			   }
		   }
	
    }
    }

     /*

     * 常规模式：收到匹配key值的消息时触发

     * channel key值

     * message 收到的消息值

     */
    @Override
    public void onMessage(String channel, String message) {
    	logger.info("调用的方法为{},功能为{},\n参数为[{}],[{}]--->>【{}】【{}】",
				new Object[]{"onMessage","常规模式：收到匹配key值的消息时触发","String channel","String message",channel,message});
		JSONObject msg=new JSONObject();
		try {
			msg.put("key", channel);
			msg.put("value", message);
		
		} catch (JSONException e) {
			e.printStackTrace();
		}
		subMsg2Web(msg);

    }

     /*

     * 正则模式：关闭正则类型订阅时触发

     * pattern key的正则表达式

     * subscribedChannels 订阅数量

     */

    @Override
	public void onPUnsubscribe(String pattern, int subscribedChannels) {
     	logger.info("调用的方法为{},功能为{},\n参数为[{}],[{}]--->>【{}】【{}】",
				new Object[]{"onPUnsubscribe","正则模式：关闭正则类型订阅时触发",
     			"String pattern"," int subscribedChannels",pattern, subscribedChannels});
	}

     /*

     * 正则模式：启动正则类型订阅时触发

     * pattern key的正则表达式

     * subscribedChannels 订阅数量

     */

	@Override
	public void onPSubscribe(String pattern, int subscribedChannels) {

		logger.info("调用的方法为{},功能为{},\n参数为[{}],[{}]--->>【{}】【{}】",
				new Object[]{"onPSubscribe","正则模式：启动正则类型订阅时触发",
     			"String pattern"," int subscribedChannels",pattern, subscribedChannels});

	}
     /*

     * 正则模式：收到匹配key值的消息时触发

     * pattern订阅的key正则表达式

     * channel匹配上该正则key值

     * message收到的消息值

     */
	
		@Override
		public void onPMessage(String pattern, String channel, String message) {
			logger.info("调用的方法为{},功能为{},\n参数为[{}],[{}][{}]--->>【{}】【{}】【{}】",
					new Object[]{"onPMessage","正则模式：收到匹配key值的消息时触发",
	     			"String pattern"," int channel"," String message",pattern, channel,message});
			System.out.println(channel);
			System.out.println(asyncContexts);
			JSONObject msg=new JSONObject();
			try {
				msg.put("key", channel);
				msg.put("value", message);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			subMsg2Web(msg);
		
		}

    public void subMsg2Web(JSONObject message) {
//	    获得要推送的channel 
	    String channelName = null;
	    boolean hasUUID=false;
		try {
			channelName = message.getString("key");
			
		} catch (JSONException e1) {

			e1.printStackTrace();
		}
		 Jedis jedis=JedisUtils.getJedis();
//	while (!asyncContexts.isEmpty()) {
		logger.info("调用的方法为{},功能为{},\n参数为[{}]--->>【{}】",
				new Object[]{"subMsg2Web","推送",
     			"JSONObject message",message});
		
		   for( AsyncContext asyncContext : asyncContexts){
			   HttpServletRequest request = (HttpServletRequest) asyncContext.getRequest();
			   String reqUUID=request.getParameter("uuid");
			   if(reqUUID!=null){
			    	//判断该UUID是否存在channel中
			    	hasUUID=jedis.sismember(channelName,reqUUID);
			    	JedisUtils.returnJedis(jedis);
			    }
			      if(hasUUID){
					try {
					    HttpServletResponse peer = (HttpServletResponse) asyncContext
						    .getResponse();
					    peer.getWriter().write(
						    new JSONArray().put(message).toString());
					    peer.setStatus(HttpServletResponse.SC_OK);
					    peer.setContentType("application/json");
					    asyncContext.complete();
					    System.out.println(asyncContexts.size());
					    asyncContexts.remove(asyncContext);
					       logger.info("删除了一个asyncContext");
					} catch (IOException e) {
					    e.printStackTrace();
					}

			        }
		   }

	   
	  
	
    }
    public static void main(String[] args) {
    	logger.info("调用的方法为{},功能为{},参数为【{}】,【{}】",new Object[]{"onUnsubscribe","常规模式：关闭订阅时触发","String channel","int subscribedChannels"});
	}
}