/**
 * 
 */
package Redis.utils;

import java.util.Properties;

/**
 * Created on 2013-12-20
 * <p>Description: [Redis≈‰÷√≤Œ ˝]</p>
 * @author:wenghy
 * @version     1.0
 */
public class JRedisPoolConfig {
	static int EXPORE=Integer.parseInt(pageuntil.getValue("redis.properties","maxActive"));
	static int  MAX_ACTIVE=Integer.parseInt(pageuntil.getValue("redis.properties", "maxActive"));
	static int  MAX_IDLE=Integer.parseInt(pageuntil.getValue("redis.properties", "maxIdle"));
	static int MAX_WAIT=Integer.parseInt(pageuntil.getValue("redis.properties", "maxWait"));
	static int TIME_OUT=Integer.parseInt(pageuntil.getValue("redis.properties", "timeout"));
	static boolean  TEST_ON_BORROW=false;
	static boolean  TEST_ON_RETURN=false;
	static String  REDIS_PASSWORD=pageuntil.getValue("redis.properties", "redisPWD");
	static String  REDIS_IP=pageuntil.getValue("redis.properties", "redisIp");
	static int REDIS_PORT=Integer.parseInt(pageuntil.getValue("redis.properties","redisPort"));
}
