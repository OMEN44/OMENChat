package com.github.omen.controller;

import com.github.omen.model.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class MessageController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageController.class);

    @MessageMapping("/command")
    @SendTo("/topic/public")
    public Message executeCommand(@Payload Message m) {
        if (m.getLabel() != null && m.getGroup() != null) {
            switch (m.getGroup() + m.getLabel()) {
                case "corelogin" -> {
                    LOGGER.info(m.getArgs()[0] + " logged in at " + m.getArgs()[2]);
                    return Message.builder()
                            .group("core")
                            .label("login")
                            .args(new String[]{"success", m.getArgs()[0]})
                            .build();
                }

            }
        }
        return m;
    }
}
