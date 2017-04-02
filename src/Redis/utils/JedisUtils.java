/**
 * JedisUtils
 */
package Redis.utils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Created on 2013-12-18
 * <p>Description: [���������Ҫ���ܽ���]</p>
 */
/**
 * Created on 2013-12-18
 * <p>
 * Description: [���������Ҫ���ܽ���]
 * </p>
 * 
 * @author:wenghy
 * @version 1.0
 */
public class JedisUtils {
	 //private Logger log = Logger.getLogger(this.getClass());  
	 /**�������ʱ�� */
	 private final int expire =JRedisPoolConfig.EXPORE;
	 /** ����Key�ķ��� */
	// public Keys KEYS;
	 /** �Դ洢�ṹΪString���͵Ĳ��� */
	 //public Strings STRINGS;
	 /** �Դ洢�ṹΪList���͵Ĳ��� */
	// public Lists LISTS;
	 /** �Դ洢�ṹΪSet���͵Ĳ��� */
	// public Sets SETS;
	 /** �Դ洢�ṹΪHashMap���͵Ĳ��� */
	// public Hash HASH;
	 /** �Դ洢�ṹΪSet(�����)���͵Ĳ��� */
	// public SortSet SORTSET;
	 private static JedisPool jedisPool = null;  
		 
	 private JedisUtils() {   
		
	 } 
    static {  
           JedisPoolConfig config = new JedisPoolConfig();
           config.setMaxActive(JRedisPoolConfig.MAX_ACTIVE);   
           config.setMaxIdle(JRedisPoolConfig.MAX_IDLE);
           config.setMaxWait(JRedisPoolConfig.MAX_WAIT); 
           config.setTestOnBorrow(JRedisPoolConfig.TEST_ON_BORROW);
           config.setTestOnReturn(JRedisPoolConfig.TEST_ON_RETURN); 
           //redis������������룺
           jedisPool = new JedisPool(
        		config, 
        		JRedisPoolConfig.REDIS_IP, 
           		JRedisPoolConfig.REDIS_PORT,
           		JRedisPoolConfig.TIME_OUT);    
           //redisδ�������룺
          // jedisPool = new JedisPool(config, JRedisPoolConfig.REDIS_IP, 
           	//	JRedisPoolConfig.REDIS_PORT); 
	   }
    
	public JedisPool getPool() {  
		return jedisPool; 
	}
	
    /**
     * ��jedis���ӳ��л�ȡ��ȡjedis����  
     * @return
     */
    public static Jedis getJedis() {  
   	 return jedisPool.getResource(); 
	}
    
    
    private static final JedisUtils JedisUtils = new JedisUtils();
	 

   /**
    * ��ȡJedisUtilsʵ��
    * @return
    */
   public static JedisUtils getInstance() {
		return JedisUtils; 
	}

   /**
    * ����jedis
    * @param jedis
    */
   public static void returnJedis(Jedis jedis) {
		jedisPool.returnResource(jedis);
	} 

   
   /**
	 * ���ù���ʱ��
	 * @author ruan 2013-4-11
	 * @param key
	 * @param seconds
	 */
	public void expire(String key, int seconds) {
		if (seconds <= 0) { 
			return;
		}
		Jedis jedis = getJedis();
		jedis.expire(key, seconds);
		returnJedis(jedis);
	}

	/**
	 * ����Ĭ�Ϲ���ʱ��
	 * @author ruan 2013-4-11
	 * @param key
	 */
	public void expire(String key) {
		expire(key, expire);
	}

}
