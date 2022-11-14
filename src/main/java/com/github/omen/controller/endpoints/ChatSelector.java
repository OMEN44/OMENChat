package com.github.omen.controller.endpoints;

import com.github.omen.controller.database.ChatsRepo;
import com.github.omen.controller.database.MembersRepo;
import com.github.omen.controller.database.MessagesRepo;
import com.github.omen.controller.database.UsersRepo;
import com.github.omen.controller.database.entities.Chat;
import com.github.omen.controller.database.entities.Member;
import com.github.omen.model.MessageTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.*;

import static com.github.omen.Logger.log;

@Controller
public class ChatSelector {

    public Map<Integer, List<String>> listOfUsersByChat = new HashMap<>();

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
        log("Received from /selector: \n" + m);
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
                                m.getArgAsString(1),
                                m.getArgAsString(2),
                                m.getSenderId(),
                                m.getTimeSent()
                        ));
                        memr.save(new Member(
                                0,
                                m.getSenderId(),
                                cr.findChatByOwnerIdEqualsAndChatNameEquals(m.getSenderId(), m.getArgAsString(1))
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
                    String name = cr.findChatByChatIdEquals(Integer.parseInt(m.getArgAsString(0))).getChatName();
                    if (listOfUsersByChat.containsKey(Integer.valueOf((String) m.getArg(0)))) {
                        List<String> sessionList = listOfUsersByChat.get(Integer.valueOf((String) m.getArg(0)));
                        sessionList.add(m.getSession());
                        listOfUsersByChat.replace(
                                Integer.valueOf((String) m.getArg(0)),
                                sessionList
                        );
                    } else {
                        List<String> sessionList = new ArrayList<>();
                        sessionList.add(m.getSession());
                        listOfUsersByChat.put(Integer.valueOf((String) m.getArg(0)), sessionList);
                    }
                    destination = "/chat-selector/" + m.getSession();
                    message = MessageTemplate.sentBySystemGroupless("joinChat", m.getArg(0), name);
                }
                case "leaveChat" -> {
                    //check if user is owner of chat
                    // if true -> cannot delete
                    // else -> delete member where user equals id
                    //send chats
                }
                case "deleteChat" -> {
                    //if chat exists
                    // if true -> delete chat and all members where chat id equals id
                    // else -> send user error
                    //send chats
                }
                case "invite" -> {
                    if (memr.existsMemberByUserIdEqualsAndChatIdEquals(m.getSenderId(), Integer.parseInt(m.getArgAsString(0))))
                        memr.save(new Member(
                                0,
                                ur.findUserByUserNameEquals(m.getArgAsString(1)).getId(),
                                Integer.parseInt(m.getArgAsString(0))
                        ));

                }
            }
        }

        if (destination != null) {
            log("Messaging: " + destination + "\nSending: " + message);
            messagingTemplate.convertAndSend(destination, message);
        }
    }

    private void sendChats(@Payload MessageTemplate m) {
        //Remove with the addition of foreign keys

        String destination;
        MessageTemplate message;
        if (cr.count() != 0) {
            List<Chat> chatList = new ArrayList<>();
            for (Member mem : memr.findMembersByUserIdEquals(m.getSenderId()))
                chatList.add(cr.findChatByChatIdEquals(mem.getChatId()));

            destination = "/chat-selector/" + m.getArgAsString(3);
            message = MessageTemplate.sentBySystemGroupless("showChats", chatList.toArray());

            log("Messaging: " + destination + "\nSending: " + message);
            messagingTemplate.convertAndSend(destination, message);
        }
    }
}
