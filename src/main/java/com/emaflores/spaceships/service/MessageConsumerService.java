package com.emaflores.spaceships.service;

import com.emaflores.spaceships.config.RabbitMQConfig;
import com.emaflores.spaceships.entity.MessageLog;
import com.emaflores.spaceships.repository.MessageLogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageConsumerService {

    private static final Logger logger = LoggerFactory.getLogger(MessageConsumerService.class);

    @Autowired
    private MessageLogRepository messageLogRepository;

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void receiveMessage(String message) {
        logger.info("Received Message: {}", message);

        MessageLog messageLog = new MessageLog();
        messageLog.setMessage(message);
        messageLogRepository.save(messageLog);
    }
}