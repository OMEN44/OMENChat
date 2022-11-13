package com.github.omen.controller.endpoints;

import com.github.omen.controller.database.ChatsRepo;
import com.github.omen.controller.database.MembersRepo;
import com.github.omen.controller.database.MessagesRepo;
import com.github.omen.controller.database.UsersRepo;
import com.github.omen.controller.database.entities.Message;
import com.github.omen.model.MessageTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.Objects;

@Controller
public class ChatReceiver {
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

    /*
     * To send a message to this endpoint the following labels can be used:
     *  - sendMessage
     *
     * The arguments must be as follows:
     *  0: target (chat id)
     *  1: content
     *  2: message id
     *  3: session
     */

    @MessageMapping("/selector")
    public void onSelectorMessage(@Payload MessageTemplate m) {
        MessageTemplate message = null;
        String destination = null;

        if (Objects.equals(m.getGroup(), "core") && m.getLabel() != null && m.getArgs().length == 4) {
            switch (m.getLabel()) {
                case "sendMessage" -> {
                    //save to database
                    mr.save(new Message(
                            0,
                            m.getSenderId(),
                            m.getArgAsString(1),
                            m.getTimeSent(),
                            Integer.parseInt(m.getArgAsString(0))
                    ));
                    //set message and destination
                    destination = "/chat/" + m.getSession();
                    message = MessageTemplate.senderWithArgs(
                            m.getSenderId(),
                            m.getArgAsString(0),
                            m.getArgAsString(1),
                            m.getSenderId() + String.valueOf(m.getTimeSent())
                    );
                }
            }
        }

        if (destination != null) {
            System.out.println("Messaging: " + destination + "\nWith: " + message);
            messagingTemplate.convertAndSend(destination, message);
        }
    }
}
