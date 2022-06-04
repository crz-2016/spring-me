//package com.app.demo.config;
//
//import org.springframework.data.redis.serializer.RedisSerializer;
//import org.springframework.data.redis.serializer.SerializationException;
//
//import java.nio.charset.Charset;
//
///**
// * 描述：<br>
// * 版权：Copyright (c) 2011-2019<br>
// * 公司：北京活力天汇<br>
// * 作者：曹孝欢<br>
// * 版本：1.0<br>
// * 创建日期：2022/4/30<br>
// */
//public class CustomSerilizer<T> implements RedisSerializer<T> {
//    public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
//    private Class<T> clazz;
//
//    @Override
//    public byte[] serialize(T t) throws SerializationException {
//        if (t == null){
//        return new byte[0];}
//        return JSON.toJSONString(t, SerializerFeature.WriteClassName).getBytes(DEFAULT_CHARSET);
//    }
//
//    @Override
//    public T deserialize(byte[] bytes) throws SerializationException {
//        if(bytes == null || bytes.length < 0){
//            return null;
//        }
//        String string = new String(bytes,DEFAULT_CHARSET);
//        return JSON.parseObject(string,clazz);
//    }
//}
