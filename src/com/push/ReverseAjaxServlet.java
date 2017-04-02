package com.push;

import java.io.IOException;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import Redis.utils.JedisUtils;

public final class ReverseAjaxServlet extends HttpServlet {
    private static Logger logger = LoggerFactory
	    .getLogger(ReverseAjaxServlet.class);
    private static final long serialVersionUID = 1L;

    private final RedisTopicListener rtl = new RedisTopicListener();
    JedisPoolConfig poolConfig = new JedisPoolConfig();
    JedisPool jedisPool = new JedisPool(poolConfig, "localhost", 6379, 0);
    final Jedis subscriberJedis = JedisUtils.getJedis();

    @Override
    public void init() throws ServletException {
	generator.start();
	System.out.println("servlet ��ʼ�����������̡�����");
    }

    @Override
    public void destroy() {

	System.out.println("���١�����");
    }

    private final Thread generator = new Thread("Event generator") {
	@Override
	public void run() {
	    try {
		logger.info("Subscribing to \"commonChannel\". This thread will be blocked.");
		// �����߶�������
		subscriberJedis.subscribe(rtl, "initChannel");
		logger.info("Subscription ended.");
	    } catch (Exception e) {
		logger.error("Subscribing failed.", e);
	    }
	}
    };

    /**
     * 
     * Created on 2014-3-20
     * <p>
     * Discription:[���ĺ����ͷ���]
     * </p>
     * 
     * @author:wenghy
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest request,
	    HttpServletResponse response) throws ServletException, IOException {
	String strWd = request.getParameter("wd");
//	byte[] jiema= strWd.getBytes("ISO-8859-1") ; //����  
//	 strWd = new String(jiema,"UTF-8");
	AsyncContext asyncContext = request.startAsync();
	asyncContext.setTimeout(0);
	rtl.addAsyncContexts(asyncContext);
	logger.info("-----------------" + strWd);
	if (strWd != null && generator.isAlive() && !strWd.trim().equals("")) {
	    logger.info("into subMethod!!");
	    while (!rtl.isSubscribed()) {
		try {
		    Thread.sleep(200);
		    logger.info("sleep....");
		} catch (InterruptedException e) {
		    e.printStackTrace();
		}
	    }
	    rtl.subscribe(strWd);
	    logger.info("�õ��Ķ��Ĺ���Ϊ{}", strWd);
	}
    }

}
