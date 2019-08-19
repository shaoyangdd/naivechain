package org.naivechain.block.common.factory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 服务工厂类
 *
 * @author kangshaofei
 * @date 2019-08-19
 */
public class ServiceFactory {

    private static final Logger logger = LoggerFactory.getLogger(ServiceFactory.class);

    private static Map<Class, Object> serviceMap = new ConcurrentHashMap<Class, Object>();

    /**
     * 获取service对象
     *
     * @param clazz service class对象
     * @param <T>   T
     * @return T
     */
    @SuppressWarnings("unchecked")
    public static <T> T getService(Class<T> clazz) {
        T service = (T) serviceMap.get(clazz);
        if (service != null) {
            return service;
        } else {
            try {
                synchronized (ServiceFactory.class) {
                    service = (T) serviceMap.get(clazz);
                    if (service == null) {
                        service = clazz.newInstance();
                        serviceMap.put(clazz, service);
                        return service;
                    } else {
                        //多线程的时候，有一个线程已经创建了一个对象，直接返回
                        return service;
                    }
                }
            } catch (InstantiationException e) {
                logger.error("获取service失败:{}", e);
                throw new RuntimeException("获取service失败");
            } catch (IllegalAccessException e) {
                logger.error("获取service失败:{}", e);
                throw new RuntimeException("获取service失败");
            }
        }
    }
}
