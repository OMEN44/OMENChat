package com.github.omen.controller.endpoints;

import com.github.omen.controller.database.ChatsRepo;
import com.github.omen.controller.database.MembersRepo;
import com.github.omen.controller.database.MessagesRepo;
import com.github.omen.controller.database.UsersRepo;
import com.github.omen.controller.database.entities.Chat;
import com.github.omen.model.MessageTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.Objects;

@Controller
public class ChatSelector {

    @Autowired
    public SimpMessagingTemplate messagingTemplate;
    @Autowired
    UsersRepo ur;
    @Autowired
    ChatsRepo cr;
    @Autowired
    MessagesRepo mr;
    @Autowired
    MembersRepo memr;

    @MessageMapping("/selector")
    public void onLoginRequest(@Payload MessageTemplate m) {
        if (Objects.equals(m.getGroup(), "core") && m.getLabel() != null) {
            switch (m.getLabel()) {
                case "CreateChat":
                    cr.save(new Chat(
                            0,
                            (String) m.getArgs()[0],
                            m.getSenderId(),
                            m.getTimeSent()));
                    break;
                case "search":

                    break;
                case "getChats":

                    break;
            }
        }
    }
}
