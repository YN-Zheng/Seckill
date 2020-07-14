package cn.yongnian.seckill.redis;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * TODO
 */
@Service
public class RedisService {

    @Autowired
    JedisPool jedisPool;


    /**
     * 获取对象
     *
     * @param prefix
     * @param key
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T get(KeyPrefix prefix, String key, Class<T> clazz) {
        Jedis resource = null;
        try {
            resource = jedisPool.getResource();
            //生成真正的key
            String realKey = prefix.getPrefix() + key;

            String valueString = resource.get(realKey);
            T value = StringToBean(valueString, clazz);
            return value;
        } finally {
            returnToPool(resource);
        }

    }


    /**
     * 设置对象
     * <p>
     * Example: ( in redis-cli )
     * 127.0.0.1:6379> get UserKey:tka084ced1f04746ba8429b2f613a71fcb
     * "{\"avatar\":\"\",\"gmtCreate\":1589214160000,\"gmtModified\":1593770663577,\"id\":18672394517,\"loginCount\":1,\"nickname\":\"\xe5\xb0\x8f\xe7\xb1\xb3\",\"password\":\"b7797cce01b4b131b433b6acf4add449\",\"salt\":\"1a2b3c4d\"}"
     *
     * @param prefix tk
     * @param key    UUID
     * @param value  User
     * @param <T>
     * @return
     */
    public <T> boolean set(KeyPrefix prefix, String key, T value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String stringValue = beanToString(value);
            if (stringValue == null || stringValue.length() == 0) {
                return false;
            }
            // 生成真正的key
            // (BasePrefix)prefix.getPrefix = Classname+":"+prefix
            // "UserKey:tk"+uuid
            String realKey = prefix.getPrefix() + key;
            int expireSeconds = prefix.getExpireSeconds();
            if (expireSeconds <= 0) {
                jedis.set(realKey, stringValue);
            } else {
                jedis.setex(realKey, expireSeconds, stringValue);
            }
            return true;
        } finally {
            returnToPool(jedis);
        }
    }


    /**
     * 判断key是否存在
     *
     * @param prefix
     * @param key
     * @param <T>
     * @return
     */
    public <T> boolean exist(KeyPrefix prefix, String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            // 生成真正的key
            String realKey = prefix.getPrefix() + key;
            return jedis.exists(realKey);
        } finally {
            returnToPool(jedis);
        }
    }


    /**
     * 删除
     *
     * @param prefix
     * @param key
     * @param <T>
     * @return
     */
    public <T> boolean delete(KeyPrefix prefix, String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            // 生成真正的key
            String realKey = prefix.getPrefix() + key;
            Long del = jedis.del(realKey);
            return del > 0;
        } finally {
            returnToPool(jedis);
        }
    }

    /**
     * 增加
     *
     * @param prefix
     * @param key
     * @param <T>
     * @return
     */
    public <T> Long incr(KeyPrefix prefix, String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            // 生成真正的key
            String realKey = prefix.getPrefix() + key;
            return jedis.incr(realKey);
        } finally {
            returnToPool(jedis);
        }
    }

    /**
     * 减少
     *
     * @param prefix
     * @param key
     * @param <T>
     * @return
     */
    public <T> Long decr(KeyPrefix prefix, String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            // 生成真正的key
            String realKey = prefix.getPrefix() + key;
            return jedis.decr(realKey);
        } finally {
            returnToPool(jedis);
        }
    }

    public static <T> String beanToString(T value) {
        if (value == null) {
            return null;
        }
        Class<?> clazz = value.getClass();

        if (Integer.class.equals(clazz)) {
            return "" + value;
        } else if (Long.class.equals(clazz)) {
            return "" + value;
        } else if (String.class.equals(clazz)) {
            return (String) value;
        } else {
            return JSON.toJSONString(value);
        }
    }

    public static <T> T StringToBean(String str, Class<T> clazz) {
        if (str == null) {
            return null;
        }
        if (Integer.class.equals(clazz)) {
            return (T) Integer.valueOf(str);
        } else if (Long.class.equals(clazz)) {
            return (T) Long.valueOf(str);
        } else if (String.class.equals(clazz)) {
            return (T) str;
        } else {
            return JSON.toJavaObject(JSON.parseObject(str), clazz);
        }


    }

    private void returnToPool(Jedis resource) {
        if (resource != null) {
            resource.close();
        }
    }


}
