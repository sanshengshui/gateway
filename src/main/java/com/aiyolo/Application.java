package com.aiyolo;

import com.aiyolo.constant.QueueConsts;
import com.aiyolo.queue.Receiver;
import com.aiyolo.queue.Sender;
import com.aiyolo.service.storage.StorageProperties;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@SpringBootApplication
@EnableAsync
@EnableCaching
@EnableScheduling
@EnableConfigurationProperties(StorageProperties.class)
public class Application extends AsyncConfigurerSupport {

    public static final int concurrentConsumers = 2;
    public static final int maxConcurrentConsumers = 10;

//    @Bean
//    Queue inputQueue() {
//        return new Queue(QueueConsts.inputQueueName, true);
//    }
//
//    @Bean
//    Queue outputQueue() {
//        return new Queue(QueueConsts.outputQueueName, true);
//    }

    @Bean
    Receiver receiver() {
        return new Receiver();
    }

    @Bean
    Sender sender() {
        return new Sender();
    }

    @Bean
    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(QueueConsts.inputQueueName);
        container.setMessageListener(listenerAdapter);
        container.setConcurrentConsumers(concurrentConsumers);
        container.setMaxConcurrentConsumers(maxConcurrentConsumers);
        return container;
    }

    @Bean
    MessageListenerAdapter listenerAdapter(Receiver receiver) {
        return new MessageListenerAdapter(receiver, "receiveMessage");
    }

    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(8);
        executor.setMaxPoolSize(8);
        executor.setQueueCapacity(1000);
        executor.initialize();
        return executor;
    }

}
