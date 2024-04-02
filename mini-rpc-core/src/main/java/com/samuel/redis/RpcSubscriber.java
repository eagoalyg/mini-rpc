package com.samuel.redis;

import com.samuel.config.Constant;
import com.samuel.registry.RedisRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;

import java.util.HashMap;
import java.util.Map;

public class RpcSubscriber extends JedisPubSub {

    private Logger log = LoggerFactory.getLogger(RpcSubscriber.class);

    public RpcSubscriber(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    private JedisPool jedisPool;

   @Override
   public void onMessage(String channel, String message) {
       log.info("订阅消息：" + message);
       switch (message) {
           case Constant.REGISTER:
           case Constant.UNREGISTER:
               refreshRegistryCache();
               break;
       }
   }

   private void refreshRegistryCache() {
       Jedis jedis = jedisPool.getResource();
       Map<String, String> registryInfo = jedis.hgetAll(Constant.PROVIDER_KEY);
       Map<String, Map<String, Long>> cache = RedisRegistry.registryCache;
       cache.clear();

       registryInfo.forEach((key, value) -> {
           String[] split = key.split(":");
           Map<String, Long> registryAddr = cache.computeIfAbsent(split[2], k -> new HashMap<>());
           registryAddr.put(split[0] + ":" + split[1], Long.valueOf(value));
       });
   }
}
