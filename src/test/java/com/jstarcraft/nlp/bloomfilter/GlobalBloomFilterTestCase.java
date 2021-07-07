package com.jstarcraft.nlp.bloomfilter;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.redisson.Redisson;
import org.redisson.api.RKeys;
import org.redisson.client.codec.Codec;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;

import redis.embedded.RedisServer;

public class GlobalBloomFilterTestCase extends BloomFilterTestCase {

    private static RedisServer redis;

    private static Redisson redisson;

    @BeforeAll
    public static void beforeClass() {
        redis = RedisServer.builder().port(6379).setting("maxmemory 1024M").build();
        redis.start();
        // 注意此处的编解码器
        Codec codec = new JsonJacksonCodec();
        Config configuration = new Config();
        configuration.setCodec(codec);
        configuration.useSingleServer().setAddress("redis://127.0.0.1:6379");
        redisson = (Redisson) Redisson.create(configuration);
    }

    @AfterAll
    public static void afterClass() {
        redisson.shutdown();
        redis.stop();
    }

    @BeforeEach
    public void beforeTest() {
        RKeys keys = redisson.getKeys();
        keys.flushdb();
    }

    @AfterEach
    public void afterTest() {
        RKeys keys = redisson.getKeys();
        keys.flushdb();
    }

    @Override
    protected BloomFilter getBloomFilter(int elments, float probability) {
        BloomFilter bloomFilter = new GlobalBloomFilter(redisson, "bloom", elments, probability);
        return bloomFilter;
    }

}
