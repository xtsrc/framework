package com.xt.framework.db.redis.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author tao.xiong
 * @Description redis 工具
 * @Date 2022/5/9 11:12
 */
@Slf4j
public class RedisUtil {
    @Resource
    private static RedisTemplate<String, Object> redisTemplate = SpringBeanUtil.getBean("redisTemplate");

    public static boolean expire(String key, long time) {
        try {
            if (time > 0L) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }

            return true;
        } catch (Exception var4) {
            log.error("RedisUtils expire(String key,long time) failure." + var4.getMessage());
            return false;
        }
    }

    public static Long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    public static boolean hasKey(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception var2) {
            log.error("RedisUtils hasKey(String key) failure." + var2.getMessage());
            return false;
        }
    }

    public static void del(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(key[0]);
            } else {
                redisTemplate.delete(Arrays.asList(key));
            }
        }

    }

    public static Object get(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    public static boolean set(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception var3) {
            log.error("RedisUtils set(String key,Object value) failure." + var3.getMessage());
            return false;
        }
    }

    public static boolean set(String key, Object value, long time) {
        try {
            if (time > 0L) {
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            } else {
                set(key, value);
            }

            return true;
        } catch (Exception var5) {
            log.error("RedisUtils set(String key,Object value,long time) failure." + var5.getMessage());
            return false;
        }
    }

    public static long incr(String key, long delta) {
        if (delta < 0L) {
            throw new RuntimeException("递增因子必须大于0");
        } else {
            return redisTemplate.opsForValue().increment(key, delta);
        }
    }

    public static long decr(String key, long delta) {
        if (delta < 0L) {
            throw new RuntimeException("递减因子必须大于0");
        } else {
            return redisTemplate.opsForValue().increment(key, -delta);
        }
    }

    public static Object hget(String key, String item) {
        return redisTemplate.opsForHash().get(key, item);
    }

    public static Map<Object, Object> hmget(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    public static boolean hmset(String key, Map<String, Object> map) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            return true;
        } catch (Exception var3) {
            log.error("RedisUtils hmset(String key, Map<String,Object> map) failure." + var3.getMessage());
            return false;
        }
    }

    public static boolean hmset(String key, Map<String, Object> map, long time) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            if (time > 0L) {
                expire(key, time);
            }

            return true;
        } catch (Exception var5) {
            log.error("RedisUtils hmset(String key, Map<String,Object> map, long time) failure." + var5.getMessage());
            return false;
        }
    }

    public static boolean hset(String key, String item, Object value) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            return true;
        } catch (Exception var4) {
            log.error("RedisUtils hset(String key,String item,Object value) failure." + var4.getMessage());
            return false;
        }
    }

    public static boolean hset(String key, String item, Object value, long time) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            if (time > 0L) {
                expire(key, time);
            }

            return true;
        } catch (Exception var6) {
            log.error("RedisUtils hset(String key,String item,Object value,long time) failure." + var6.getMessage());
            return false;
        }
    }

    public static void hdel(String key, Object... item) {
        redisTemplate.opsForHash().delete(key, item);
    }

    public static boolean hHasKey(String key, String item) {
        return redisTemplate.opsForHash().hasKey(key, item);
    }

    public static double hincr(String key, String item, double by) {
        return redisTemplate.opsForHash().increment(key, item, by);
    }

    public static double hdecr(String key, String item, double by) {
        return redisTemplate.opsForHash().increment(key, item, -by);
    }

    public static Set<Object> sGet(String key) {
        try {
            return redisTemplate.opsForSet().members(key);
        } catch (Exception var2) {
            log.error("RedisUtils sGet(String key) failure." + var2.getMessage());
            return null;
        }
    }

    public static boolean sHasKey(String key, Object value) {
        try {
            return redisTemplate.opsForSet().isMember(key, value);
        } catch (Exception var3) {
            log.error("RedisUtils sHasKey(String key,Object value) failure." + var3.getMessage());
            return false;
        }
    }

    public static long sSet(String key, Object... values) {
        try {
            return redisTemplate.opsForSet().add(key, values);
        } catch (Exception var3) {
            log.error("RedisUtils sSet(String key, Object...values) failure." + var3.getMessage());
            return 0L;
        }
    }

    public static long sSetAndTime(String key, long time, Object... values) {
        try {
            Long count = redisTemplate.opsForSet().add(key, values);
            if (time > 0L) {
                expire(key, time);
            }

            return count;
        } catch (Exception var5) {
            log.error("RedisUtils sSetAndTime(String key,long time,Object...values) failure." + var5.getMessage());
            return 0L;
        }
    }

    public static long sGetSetSize(String key) {
        try {
            return redisTemplate.opsForSet().size(key);
        } catch (Exception var2) {
            log.error("RedisUtils sGetSetSize(String key) failure." + var2.getMessage());
            return 0L;
        }
    }

    public static long setRemove(String key, Object... values) {
        try {
            Long count = redisTemplate.opsForSet().remove(key, values);
            return count;
        } catch (Exception var3) {
            log.error("RedisUtils setRemove(String key, Object ...values) failure." + var3.getMessage());
            return 0L;
        }
    }

    public static List<Object> lGet(String key, long start, long end) {
        try {
            return redisTemplate.opsForList().range(key, start, end);
        } catch (Exception var6) {
            log.error("RedisUtils lGet(String key, long start, long end) failure." + var6.getMessage());
            return null;
        }
    }

    public static long lGetListSize(String key) {
        try {
            return redisTemplate.opsForList().size(key);
        } catch (Exception var2) {
            log.error("RedisUtils lGetListSize(String key) failure." + var2.getMessage());
            return 0L;
        }
    }

    public static Object lGetIndex(String key, long index) {
        try {
            return redisTemplate.opsForList().index(key, index);
        } catch (Exception var4) {
            log.error("RedisUtils lGetIndex(String key,long index) failure." + var4.getMessage());
            return null;
        }
    }

    public static boolean lSet(String key, Object value) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            return true;
        } catch (Exception var3) {
            log.error("RedisUtils lSet(String key, Object value) failure." + var3.getMessage());
            return false;
        }
    }

    public static boolean lSet(String key, Object value, long time) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            if (time > 0L) {
                expire(key, time);
            }

            return true;
        } catch (Exception var5) {
            log.error("RedisUtils lSet(String key, Object value, long time) failure." + var5.getMessage());
            return false;
        }
    }

    public static boolean lSet(String key, List<Object> value) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            return true;
        } catch (Exception var3) {
            log.error("RedisUtils lSet(String key, List<Object> value) failure." + var3.getMessage());
            return false;
        }
    }

    public static boolean lSet(String key, List<Object> value, long time) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            if (time > 0L) {
                expire(key, time);
            }

            return true;
        } catch (Exception var5) {
            log.error("RedisUtils lSet(String key, List<Object> value, long time) failure." + var5.getMessage());
            return false;
        }
    }

    public static boolean lUpdateIndex(String key, long index, Object value) {
        try {
            redisTemplate.opsForList().set(key, index, value);
            return true;
        } catch (Exception var5) {
            log.error("RedisUtils lUpdateIndex(String key, long index,Object value) failure." + var5.getMessage());
            return false;
        }
    }

    public static long lRemove(String key, long count, Object value) {
        try {
            Long remove = redisTemplate.opsForList().remove(key, count, value);
            return remove;
        } catch (Exception var5) {
            log.error("RedisUtils lRemove(String key,long count,Object value) failure." + var5.getMessage());
            return 0L;
        }
    }
}
