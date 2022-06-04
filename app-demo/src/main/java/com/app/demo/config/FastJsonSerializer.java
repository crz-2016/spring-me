package com.app.demo.config;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.nio.charset.Charset;
 
/**
 * 说明:自定义redis序列化方式
 *
 * @author WangBin
 * @version V1.0
 * @since 2018.03.22
 */
public class FastJsonSerializer<T> implements RedisSerializer<T> {
    
    public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
 
    private Class<T> clazz;
 
    public FastJsonSerializer(Class<T> clazz) {
        super();
        this.clazz = clazz;
    }
    
    @Override
    public byte[] serialize(T t) throws SerializationException {
        if (t == null) {
            return new byte[0];
        }
        return null;
//        return JSON.toJSONString(t, SerializerFeature.IgnoreErrorGetter).getBytes(DEFAULT_CHARSET);
    }
 
    @Override
    public T deserialize(byte[] bytes) throws SerializationException {
        if (bytes == null || bytes.length <= 0) {
            return null;
        }
        String str = new String(bytes, DEFAULT_CHARSET);
        return null;
//        return JSON.parseObject(str, clazz);
    }
}