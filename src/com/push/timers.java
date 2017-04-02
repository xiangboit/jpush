package com.push;
import java.util.*;

import redis.clients.jedis.Jedis;

import Redis.utils.JRedisPoolConfig;
public class timers extends  TimerTask{
	private Jedis jedis;
	@Override
	public void run() {
		jedis = new Jedis("127.0.0.1");
		String [] ss={"sad","ppp","asd"};
		String [] asd={"sad","ppp","asd"};
		for(int i=0;i<ss.length;i++){		
			for (int j = 0; j < asd.length; j++) {
				jedis.publish(ss[i], asd[j]);
				System.out.println(ss[i]+"=======>"+asd[j]);
			}
		}
	}
	
}
