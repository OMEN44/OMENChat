package com.github.omen.controller;

import com.github.OMEN44.Message;
import org.apache.catalina.session.StandardSessionFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import static com.github.omen.App.PLUGIN_CONTAINER;

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

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageController.class);

    @MessageMapping("/chat")
    @SendTo("/client-chat")
    public Message onChatMessage(@Payload Message m, SimpMessageHeaderAccessor headerAccessor) {
        return m;
    }

    @MessageMapping("/login")
    @SendTo("/login")
    public Message onLoginRequest(@Payload Message m, SimpMessageHeaderAccessor headerAccessor) {
        assert PLUGIN_CONTAINER != null;
        return PLUGIN_CONTAINER.getPlugin("core").getCommandExecutor("login")
                .execute(m.getLabel(), m.getSenderId(), m.getTimeSent(), m.getArgs());
    }

    @MessageMapping("/command")
    @SendTo("/client-command")
    public Message onCommand(@Payload Message m, SimpMessageHeaderAccessor headerAccessor) {
        if (m.getLabel() != null && m.getGroup() != null) {
            assert PLUGIN_CONTAINER != null;
            return PLUGIN_CONTAINER.getPlugin(m.getGroup()).getCommandExecutor(m.getLabel())
                    .execute(m.getLabel(), m.getSenderId(), m.getTimeSent(), m.getArgs());
        }
        return m;
    }

    @MessageMapping("/system")
    @SendTo("/client-system")
    public Message onSystemMessage(@Payload Message m, SimpMessageHeaderAccessor headerAccessor) {
        if (m.getLabel() != null && m.getGroup() != null) {
            headerAccessor.getSessionAttributes().forEach((s, o) -> {
                StandardSessionFacade st = (StandardSessionFacade) o;
                System.out.println(s + " | " + st.getId());
            });

            assert PLUGIN_CONTAINER != null;
            return PLUGIN_CONTAINER.getPlugin(m.getGroup()).getCommandExecutor(m.getLabel())
                    .execute(m.getLabel(), m.getSenderId(), m.getTimeSent(), m.getArgs());
        }
        return m;
    }
}
