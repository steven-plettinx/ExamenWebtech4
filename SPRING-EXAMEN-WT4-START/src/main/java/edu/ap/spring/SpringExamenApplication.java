package edu.ap.spring;


import edu.ap.spring.controller.InhaalExamenController;
import edu.ap.spring.redis.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@SpringBootApplication
public class SpringExamenApplication {

    private RedisService service;
    private String CHANNEL = "edu:ap:redis";
    private String KEY = "edu:ap:test";

    @Autowired
    public void setRedisService(RedisService service) {
        this.service = service;
    }

    @Bean
    RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory,
                                            MessageListenerAdapter listenerAdapter) {

        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(listenerAdapter, new ChannelTopic(CHANNEL));

        return container;
    }

    @Bean
    MessageListenerAdapter listenerAdapter(InhaalExamenController controller) {
        return new MessageListenerAdapter(controller, "getStudent");
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringExamenApplication.class, args);
    }
}
