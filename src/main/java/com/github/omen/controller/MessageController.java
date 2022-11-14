package com.github.omen.controller;

import com.github.omen.controller.database.ChatsRepo;
import com.github.omen.controller.database.MembersRepo;
import com.github.omen.controller.database.MessagesRepo;
import com.github.omen.controller.database.UsersRepo;
import com.github.omen.controller.endpoints.Login;
import com.github.omen.model.MessageTemplate;
import org.apache.catalina.session.StandardSessionFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class MessageController {

    /*
    * for reference
    * This annotation: @MessageMapping("\<end point name>") get triggered when the client sends a message
    * to: "\app\<end point name>"
    *
    * The value of the @SendTo annotation is what the client subscribes to
    *
    * omen-chat/[login, chat, system]/
    *
    */

    @Autowired
    public SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/command")
    @SendTo("/client-command")
    public MessageTemplate onCommand(@Payload MessageTemplate m, SimpMessageHeaderAccessor headerAccessor) {
        return m;
    }

    @MessageMapping("/system")
    @SendTo("/client-system")
    public MessageTemplate onSystemMessage(@Payload MessageTemplate m, SimpMessageHeaderAccessor headerAccessor) {
        if (m.getLabel() != null && m.getGroup() != null) {
            headerAccessor.getSessionAttributes().forEach((s, o) -> {
                StandardSessionFacade st = (StandardSessionFacade) o;
                System.out.println(s + " | " + st.getId());
            });
/*
            assert PLUGIN_CONTAINER != null;
            return PLUGIN_CONTAINER.getPlugin(m.getGroup()).getCommandExecutor(m.getLabel())
                    .execute(m.getLabel(), m.getSenderId(), m.getTimeSent(), m.getArgs());*/
        }
        return m;
    }
}
