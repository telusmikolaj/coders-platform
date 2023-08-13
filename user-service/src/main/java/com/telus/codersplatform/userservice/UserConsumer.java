package com.telus.codersplatform.userservice;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class UserConsumer {


    @RabbitListener(queues = "${rabbitmq.queue.registration}")
    public void consume(UserRegistrationRequest userRegistrationRequest) {
        log.info("Consumed {} from queue ", userRegistrationRequest);
        System.out.println(userRegistrationRequest);

    }
}
