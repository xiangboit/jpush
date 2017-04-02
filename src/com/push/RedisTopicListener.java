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
 * <p>Description: [���������Ҫ���ܽ���]</p>
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

     * ����ģʽ���رն���ʱ����

     * channel keyֵ

     * subscribedChannels ��������

     */
		@Override
    public void onUnsubscribe(String channel, int subscribedChannels) {
			logger.info("���õķ���Ϊ{},����Ϊ{},\n����Ϊ[{}],[{}]--->>��{}����{}��",
					new Object[]{"onUnsubscribe","����ģʽ���رն���ʱ����","String channel","int subscribedChannels",channel,subscribedChannels});
		
    }

     /*

     * ����ģʽ����������ʱ����

     * channel keyֵ

     * subscribedChannels ��������

     */
    @Override
    public void onSubscribe(String channel, int subscribedChannels) {
    	logger.info("���õķ���Ϊ{},����Ϊ{},\n����Ϊ[{}],[{}]--->>��{}����{}��",
				new Object[]{"onSubscribe","����ģʽ����������ʱ����","String channel","int subscribedChannels",channel,subscribedChannels});
//    	Jedis jedis=JedisUtils.getJedis();
//		UUID uuid=UUID.randomUUID();
//		jedis.sadd(channel, uuid.toString());
//		JedisUtils.returnJedis(jedis);
//		logger.info("����UUID,keyֵΪ{},valueΪ{}",channel,uuid.toString());
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
					map.put("subMsg", "������");
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
			       logger.info("ɾ����һ��asyncContext");
			       break;
			   }
		   }
	
    }
    }

     /*

     * ����ģʽ���յ�ƥ��keyֵ����Ϣʱ����

     * channel keyֵ

     * message �յ�����Ϣֵ

     */
    @Override
    public void onMessage(String channel, String message) {
    	logger.info("���õķ���Ϊ{},����Ϊ{},\n����Ϊ[{}],[{}]--->>��{}����{}��",
				new Object[]{"onMessage","����ģʽ���յ�ƥ��keyֵ����Ϣʱ����","String channel","String message",channel,message});
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

     * ����ģʽ���ر��������Ͷ���ʱ����

     * pattern key��������ʽ

     * subscribedChannels ��������

     */

    @Override
	public void onPUnsubscribe(String pattern, int subscribedChannels) {
     	logger.info("���õķ���Ϊ{},����Ϊ{},\n����Ϊ[{}],[{}]--->>��{}����{}��",
				new Object[]{"onPUnsubscribe","����ģʽ���ر��������Ͷ���ʱ����",
     			"String pattern"," int subscribedChannels",pattern, subscribedChannels});
	}

     /*

     * ����ģʽ�������������Ͷ���ʱ����

     * pattern key��������ʽ

     * subscribedChannels ��������

     */

	@Override
	public void onPSubscribe(String pattern, int subscribedChannels) {

		logger.info("���õķ���Ϊ{},����Ϊ{},\n����Ϊ[{}],[{}]--->>��{}����{}��",
				new Object[]{"onPSubscribe","����ģʽ�������������Ͷ���ʱ����",
     			"String pattern"," int subscribedChannels",pattern, subscribedChannels});

	}
     /*

     * ����ģʽ���յ�ƥ��keyֵ����Ϣʱ����

     * pattern���ĵ�key������ʽ

     * channelƥ���ϸ�����keyֵ

     * message�յ�����Ϣֵ

     */
	
		@Override
		public void onPMessage(String pattern, String channel, String message) {
			logger.info("���õķ���Ϊ{},����Ϊ{},\n����Ϊ[{}],[{}][{}]--->>��{}����{}����{}��",
					new Object[]{"onPMessage","����ģʽ���յ�ƥ��keyֵ����Ϣʱ����",
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
//	    ���Ҫ���͵�channel 
	    String channelName = null;
	    boolean hasUUID=false;
		try {
			channelName = message.getString("key");
			
		} catch (JSONException e1) {

			e1.printStackTrace();
		}
		 Jedis jedis=JedisUtils.getJedis();
//	while (!asyncContexts.isEmpty()) {
		logger.info("���õķ���Ϊ{},����Ϊ{},\n����Ϊ[{}]--->>��{}��",
				new Object[]{"subMsg2Web","����",
     			"JSONObject message",message});
		
		   for( AsyncContext asyncContext : asyncContexts){
			   HttpServletRequest request = (HttpServletRequest) asyncContext.getRequest();
			   String reqUUID=request.getParameter("uuid");
			   if(reqUUID!=null){
			    	//�жϸ�UUID�Ƿ����channel��
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
					       logger.info("ɾ����һ��asyncContext");
					} catch (IOException e) {
					    e.printStackTrace();
					}

			        }
		   }

	   
	  
	
    }
    public static void main(String[] args) {
    	logger.info("���õķ���Ϊ{},����Ϊ{},����Ϊ��{}��,��{}��",new Object[]{"onUnsubscribe","����ģʽ���رն���ʱ����","String channel","int subscribedChannels"});
	}
}