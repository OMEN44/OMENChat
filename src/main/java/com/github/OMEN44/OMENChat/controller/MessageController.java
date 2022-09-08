package com.github.OMEN44.OMENChat.controller;

import com.github.OMEN44.OMENChat.model.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class MessageController {

    @MessageMapping("/ping")
    @SendTo("/topic/public")
    public Message ping(@Payload Message m) {
        String s = m.getTimeSent();
        return Message.builder().type('R').command(0).timeSent("now").args(new String[]{s}).senderId("0").build();
    }

    @MessageMapping("/command")
    @SendTo("/topic/public")
    public Message executeCommand(@Payload Message m) {
        System.out.println("user: " + m.getArgs()[0] + ", password: " + m.getArgs()[1]);
        return m;
    }


}
