package ru.devforu.bookdelivery.rabitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

/**
 * @author Miklyaev Konstantin <a href="https://devforu.ru/">DevForU</a>
 */

public class RabbitMqListener {
    Logger logger = LoggerFactory.getLogger(RabbitMqListener.class);
    
    @RabbitListener()
    public void requestsQueue(String message) {
        logger.info("Received message: {}", message);
    } 
}
