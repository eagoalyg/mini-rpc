package com.samuel.registry;

import com.samuel.config.Constant;
import com.samuel.config.RegistryConfig;
import com.samuel.model.ServiceMetaInfo;
import com.samuel.redis.RpcSubscriber;
import com.samuel.util.AddressUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class RedisRegistry implements Registry {
    private final Logger logger = LoggerFactory.getLogger(RedisRegistry.class);

    // 服务缓存
    public static final Map<String, Map<String, Long>> registryCache = new ConcurrentHashMap<>();
    private JedisPool jedisPool;

    private Logger log = LoggerFactory.getLogger(RedisRegistry.class);

    @Override
    public void shutdown() {
        Jedis resource = jedisPool.getResource();
        resource.publish(Constant.PROVIDER_KEY, Constant.UNREGISTER);
    }

    @Override
    public void init(RegistryConfig registryConfig) {
        JedisPool jedisPool = new JedisPool(registryConfig.getAddress(), registryConfig.getPort());
        this.jedisPool = jedisPool;
        Jedis jedis = jedisPool.getResource();
        new Thread(() -> {
            log.info("开启服务消息订阅");
            jedis.subscribe(new RpcSubscriber(jedisPool), Constant.PROVIDER_KEY);
        }).start();

        // 先拉取一遍服务信息
        Map<String, String> registryInfo = jedis.hgetAll(Constant.PROVIDER_KEY);
        Map<String, Map<String, Long>> cache = RedisRegistry.registryCache;
        cache.clear();

        registryInfo.forEach((key, value) -> {
            String[] split = key.split(":");
            Map<String, Long> registryAddr = cache.computeIfAbsent(split[2], k -> new HashMap<>());
            registryAddr.put(split[0] + ":" + split[1], Long.valueOf(value));
        });
    }

    @Override
    public List<ServiceMetaInfo> discoverService(String serviceName) {
        Map<String, Long> registry = registryCache.get(serviceName);

        if (registry == null || registry.isEmpty()) {
            return null;
        }

        return registry.entrySet().stream().filter((entry) ->  System.currentTimeMillis() >= entry.getValue()).map((key) -> {
            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
            serviceMetaInfo.setServiceName(serviceName);
            String[] split = key.getKey().split(":");
            serviceMetaInfo.setAddress(split[0]);
            serviceMetaInfo.setPort(Integer.valueOf(split[1]));
            return serviceMetaInfo;
        }).collect(Collectors.toList());
    }

    @Override
    public void register(ServiceMetaInfo serviceMetaInfo) {
        Jedis jedis = jedisPool.getResource();
        try {
            long isExit = jedis.hset(Constant.PROVIDER_KEY, AddressUtil.generateLocalAddress() + ":" + serviceMetaInfo.getServiceName(), System.currentTimeMillis() + "");
            jedis.publish(Constant.PROVIDER_KEY, Constant.REGISTER);

            ScheduledExecutorService schedule = Executors.newScheduledThreadPool(1);
            schedule.scheduleAtFixedRate(() -> {
                int errorCount = 0;
                while (true) {
                    try {
                        if (jedis.hset(Constant.PROVIDER_KEY, AddressUtil.generateLocalAddress() + ":" + serviceMetaInfo.getServiceName(), System.currentTimeMillis() + 60 * 1000 + "") == 1) {
                            jedis.publish(Constant.PROVIDER_KEY, Constant.REGISTER);
                            logger.info("------------ publish register info-------------");
                        };
                        logger.info("续期");
                    } catch (RuntimeException e) {
                        if (errorCount > 3) {
                            logger.error("注册服务重试三次失败");
                            throw new RuntimeException(e);
                        }
                        logger.error("注册失败重试");
                        errorCount++;
                        // retry until succeed !
                        continue;
                    } catch (UnknownHostException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                }

                if (true) {
                    clean(jedis);
                }
            }, 30L, 30L, TimeUnit.SECONDS);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    private void clean(Jedis jedis) {
        Map<String, String> registryInfo = jedis.hgetAll(Constant.PROVIDER_KEY);
        List<String> collect = registryInfo.entrySet().stream().filter(entry -> Long.valueOf(entry.getValue()) < System.currentTimeMillis())
                .map(entry -> entry.getKey()).collect(Collectors.toList());
        if (collect.isEmpty()) {
            return;
        }
        String[] s = new String[collect.size()];
        for (int i = 0; i < collect.size(); i++) {
            s[i] = collect.get(i);
        }
        try {
            jedis.publish(Constant.PROVIDER_KEY, Constant.UNREGISTER);
            jedis.hdel(Constant.PROVIDER_KEY, s);
        } catch (Exception e) {
            logger.error("删除服务异常");
            throw new RuntimeException(e);
        }
        logger.info("----清除过期服务  current time " + System.currentTimeMillis());
    }
}
