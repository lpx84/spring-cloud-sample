package com.xiang.cloud.cache.redis.serializer;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.io.ByteArrayOutputStream;

/**
 * @author 李鹏翔(lipengxiang1)
 * @date 2019/6/26
 **/
public class KryoObjectRedisSerializer implements RedisSerializer<Object> {

    private static final byte[] EMPTY_BYTE_ARRAY = new byte[0];

    private static ThreadLocal<Kryo> KRYO_THREAD_LOCAL = ThreadLocal.withInitial(() -> {
        Kryo kryo = new Kryo();
        kryo.setReferences(false);
        kryo.register(Object.class);
        return kryo;
    });

    public KryoObjectRedisSerializer() {
        super();
    }

    @Override
    public byte[] serialize(Object obj) throws SerializationException {
        if (obj == null) {
            return EMPTY_BYTE_ARRAY;
        }
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        Output output = new Output(byteOut);
        KRYO_THREAD_LOCAL.get().writeClassAndObject(output, obj);
        output.close();
        return byteOut.toByteArray();
    }

    @Override
    public Object deserialize(byte[] bytes) throws SerializationException {
        if (bytes == null || bytes.length <= 0) {
            return null;
        }
        Input input = new Input(bytes);
        try {
            return KRYO_THREAD_LOCAL.get().readClassAndObject(input);
        } finally {
            input.close();
        }
    }

}
