package com.github.omen.controller.endpoints;

import com.github.omen.controller.database.ChatsRepo;
import com.github.omen.controller.database.MembersRepo;
import com.github.omen.controller.database.MessagesRepo;
import com.github.omen.controller.database.UsersRepo;
import com.github.omen.controller.database.entities.Chat;
import com.github.omen.controller.database.entities.Member;
import com.github.omen.controller.database.entities.Message;
import com.github.omen.controller.database.entities.User;
import com.github.omen.model.MessageTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;
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

    /*
     * To send a message to this endpoint the following labels can be used:
     *  - getChats
     *  - createChat
     *  - search
     *  - joinChat
     *  - leaveChat
     *  - deleteChat
     *
     * The arguments must be as follows:
     *  0: chat id
     *  1: input bar
     *  2: description
     *  3: session
     *
     * With joinChat being an exception to this format
     */

    @MessageMapping("/selector")
    public void onSelectorMessage(@Payload MessageTemplate m) {
        MessageTemplate message = null;
        String destination = null;

        if (Objects.equals(m.getGroup(), "core") && m.getLabel() != null && m.getArgs().length == 4) {
            switch (m.getLabel()) {
                case "getChats" -> sendChats(m);
                case "createChat" -> {
                    //if chat with that name and owner doesn't exist:
                    if (!cr.existsChatByOwnerIdEqualsAndChatNameEquals(m.getSenderId(), m.getArgAsString(0))) {
                        cr.save(new Chat(
                                0,
                                m.getArgAsString(0),
                                m.getArgAsString(1),
                                m.getSenderId(),
                                m.getTimeSent()
                        ));
                        memr.save(new Member(
                                0,
                                m.getSenderId(),
                                cr.findChatByOwnerIdEqualsAndChatNameEquals(m.getSenderId(), m.getArgAsString(0))
                                        .getChatId()
                        ));

                        //Remove with the addition of foreign keys
                        sendChats(m);
                    } else {
                        destination = "/chat-selector/" + m.getSession();
                        message = MessageTemplate.sentBySystemGroupless("chatExists", m.getArgAsString(0));
                    }
                }
                case "search" -> {

                }
                case "joinChat" -> {
                    List<Message> messages = mr.findTopByRecipientIdEqualsOrderByDateDesc(50/*, Integer.parseInt(m.getArgAsString(0))*/);
                    String name = cr.findChatByChatIdEquals(Integer.parseInt(m.getArgAsString(0))).getChatName();
                    messagingTemplate.convertAndSend(
                            "/chat-selector/" + m.getSession(),
                            MessageTemplate.sentBySystemGroupless("joinChat", m.getArg(0), name, messages.toArray())
                    );
                }
                case "leaveChat" -> {
                    //check if user is owner of chat
                    // if true -> cannot delete
                    // else -> delete member where user equals id
                    //send chats
                }
                case "deleteChat" -> {

                }
            }
        }

        if (destination != null) {
            System.out.println("Messaging: " + destination + "\nWith: " + message);
            messagingTemplate.convertAndSend(destination, message);
        }
    }

    private void sendChats(@Payload MessageTemplate m) {
        //Remove with the addition of foreign keys
        if (cr.count() != 0) {
            List<Chat> chatList = new ArrayList<>();
            for (Member mem : memr.findMembersByUserIdEquals(m.getSenderId()))
                chatList.add(cr.findChatByChatIdEquals(mem.getChatId()));

            messagingTemplate.convertAndSend(
                    "/chat-selector/" + m.getSession(),
                    MessageTemplate.sentBySystemGroupless(
                            "showChats",
                            chatList.toArray()
                    )
            );
        }
    }
}
