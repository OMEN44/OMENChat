package com.github.omen.controller;

import com.github.OMEN44.Message;
import org.apache.catalina.session.StandardSessionFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
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

    @Autowired
    public SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat")
    @SendTo("/chat")
    public Message onChatMessage(@Payload Message m, SimpMessageHeaderAccessor headerAccessor) {
        return new Message()
                .setSenderId(m.getSenderId())
                .setTimeSent(m.getTimeSent())
                .setArgs(m.getSenderId() + m.getTimeSent(), m.getArgs()[0]);
    }

    @MessageMapping("/login")
    public void onLoginRequest(@Payload Message m, @Header("simpSessionId") String sessionId) {
        assert PLUGIN_CONTAINER != null;
        Message message = PLUGIN_CONTAINER.getPlugin("core").getCommandExecutor("login")
                .execute(m.getLabel(), m.getSenderId(), m.getTimeSent(), m.getArgs());
        System.out.println("/login/" + message.getArgs()[2]);
        messagingTemplate.convertAndSend(
                "/login/" + message.getArgs()[2],
                message
        );
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

    private MessageHeaders createHeaders(String sessionId) {
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
        headerAccessor.setSessionId(sessionId);
        headerAccessor.setLeaveMutable(true);
        return headerAccessor.getMessageHeaders();
    }
}
