package com.wyf.util;

import io.netty.handler.codec.marshalling.DefaultMarshallerProvider;
import io.netty.handler.codec.marshalling.MarshallerProvider;
import io.netty.handler.codec.marshalling.MarshallingDecoder;
import io.netty.handler.codec.marshalling.MarshallingEncoder;


import io.netty.handler.codec.marshalling.DefaultUnmarshallerProvider;
import io.netty.handler.codec.marshalling.UnmarshallerProvider;

import org.jboss.marshalling.MarshallerFactory;
import org.jboss.marshalling.Marshalling;
import org.jboss.marshalling.MarshallingConfiguration;

/**
 * 把信息序列化成对象时使用
 *
 *@author: Weiyf
 *@Date: 2019-02-27 10:00
 */
public class MarshallingCodeUtil {

    /**
     * 创建 marshalling 解码器
     * @return
     */
    public static MarshallingDecoder buildDecoder() {

        // 先通过marshalling的工具类提供的方法实例化marshalling对象，参数serial是创建Java序列化工厂对象 serial
        MarshallerFactory  factory = Marshalling.getProvidedMarshallerFactory("serial");

        // 创建MarshallingConfiguration对象，设置版本为5
        final MarshallingConfiguration config = new MarshallingConfiguration();
        config.setVersion(5);

        // 根据 marshalling 的factory 和 config创建 provider；
        UnmarshallerProvider provider = new DefaultUnmarshallerProvider(factory, config);

        // 构建netty的MarshallingDecoder对象，两个参数分别是provide和单个信息序列后的最大长度
        MarshallingDecoder decoder = new MarshallingDecoder(provider, 1024*1204*1);

        return decoder;
    }


    /**
     * 创建 marshalling 编码器
     * @return
     */
    public static MarshallingEncoder buildEncoder() {

        // 先通过marshalling的工具类提供的方法实例化marshalling对象，参数serial是创建Java序列化工厂对象
        final MarshallerFactory  factory = Marshalling.getProvidedMarshallerFactory("serial");

        // 创建MarshallingConfiguration对象，设置版本为5
        final MarshallingConfiguration config = new MarshallingConfiguration();
        config.setVersion(5);

        // 根据 marshalling 的factory 和 config创建 provider；
        MarshallerProvider provider = new DefaultMarshallerProvider(factory, config);

        // 构建netty 的编码对象， 用于实现序列化接口的pojo对象序列化为二进制数组
        MarshallingEncoder encoder = new MarshallingEncoder(provider);

        return encoder;
    }

}
