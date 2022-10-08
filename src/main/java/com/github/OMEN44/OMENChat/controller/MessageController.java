package com.github.OMEN44.OMENChat.controller;

import com.github.OMEN44.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import static com.github.OMEN44.OMENChat.Main.PLUGIN_MGR;

@Controller
public class MessageController {

    @MessageMapping("/command")
    @SendTo("/topic/public")
    public Message executeCommand(@Payload Message m) {
        if (m.getLabel() != null && m.getGroup() != null && PLUGIN_MGR != null) {
            Message mes = PLUGIN_MGR.getPLUGIN_MAP().get(m.getGroup()).getCommandExecutor(m.getLabel())
                    .execute(m.getLabel(), m.getSenderId(), m.getTimeSent(), m.getArgs());
            System.out.println(mes);
            return mes;
        }
        return m;
    }
}
