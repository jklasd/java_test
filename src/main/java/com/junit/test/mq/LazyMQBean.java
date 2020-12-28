package com.junit.test.mq;

import org.springframework.amqp.core.AmqpAdmin;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class LazyMQBean {
	public final static String packageName = "org.springframework.amqp";
	public final static String rabbitPackage = "org.springframework.amqp.rabbit";
	
	protected static AmqpAdmin admin;
	private static LazyMQBean factory;
	private static LazyMQBean getFactory(String packageName) {
		if(factory != null) {
			return factory;
		}
		switch (packageName) {
		case rabbitPackage:
			factory = new LazyRabbitMQBean(); 
			return factory;
		default:
			break;
		}
		return null;
	}
	
	@SuppressWarnings("rawtypes")
	public static Object buildBean(Class classBean) throws InstantiationException, IllegalAccessException {
		if(classBean.getPackage().getName().contains(packageName)) {
			if(classBean.getPackage().getName().contains(rabbitPackage)) {
				return getFactory(rabbitPackage).buildBeanProcess(classBean);
			}else {
				//
				log.warn("{}=>MQ 还未配置",classBean.getPackage().getName());
			}
			if (factory != null) {
				return factory.buildBeanProcess(classBean);
			}
		}
		log.warn("构建{}失败",classBean);
		return null;
	}
	@SuppressWarnings("rawtypes")
	public abstract Object buildBeanProcess(Class classBean) throws InstantiationException, IllegalAccessException;
}
