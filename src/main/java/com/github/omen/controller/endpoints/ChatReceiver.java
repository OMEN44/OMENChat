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

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.github.omen.Logger.*;

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
    @Autowired
    ChatSelector cs;

    /*
     * To send a message to this endpoint the following labels can be used:
     *  - sendMessage
     *
     * The arguments must be as follows:
     *  0: target (chat id)
     *  1: content
     *  2: message id
     *  3: date sent
     *  4: sender name (for display)
     *  5: session
     */

    @MessageMapping("/chat")
    public void onChatMessage(@Payload MessageTemplate m) {
        log("Received from /chat: \n" + m);
        MessageTemplate message = null;
        String[] destinations = null;

        if (Objects.equals(m.getGroup(), "core") && m.getLabel() != null) {
            switch (m.getLabel()) {
                case "sendMessage" -> {
                    //save to database
                    mr.save(new Message(
                            m.getSenderId() + String.valueOf(m.getTimeSent()),
                            m.getSenderId(),
                            m.getArgAsString(1),
                            m.getTimeSent(),
                            Integer.parseInt(m.getArgAsString(0))
                    ));
                    //set message and destination
                    List<String> sessionList = cs.listOfUsersByChat.get(Integer.valueOf((String) m.getArg(0)));
                    destinations = new String[sessionList.size()];
                    for (int i = 0; i < sessionList.size(); i++) {
                        destinations[i] = "/chat/" + sessionList.get(i);
                    }
                    message = MessageTemplate.currentDateGroupless(
                            "sendMessage",
                            m.getSenderId(),
                            m.getArg(0),
                            m.getArgAsString(1),
                            m.getSenderId() + String.valueOf(m.getTimeSent()),
                            m.getTimeSent(),
                            ur.findUserByIdEquals(m.getSenderId()).getUserName()
                    );
                }
                case "getMessages" -> {
                    //TODO make this limit at 50 messages
                    destinations = new String[]{"/chat/" + m.getSession()};
                    List<Message> mList = mr.findAllByRecipientIdEqualsOrderByDateDesc(Integer.parseInt(m.getArgAsString(0)));
                    Object[] args = new Object[mList.size()];
                    Object[] mes;
                    //convert list to array of arrays because cannot send message object
                    for (int i = mList.size() - 1; i >= 0; i--) {
                        mes = new Object[5];
                        mes[0] = mList.get(i).getRecipientId();
                        mes[1] = mList.get(i).getContent();
                        mes[2] = mList.get(i).getId();
                        mes[3] = mList.get(i).getDate();
                        //TODO maybe optimise by storing name in database?
                        mes[4] = ur.findUserByIdEquals(mList.get(i).getSenderId()).getUserName();
                        args[i] = mes;
                    }
                    message = MessageTemplate.sentBySystemGroupless("getMessages", args);
                }
                case "exit" -> {
                    List<String> list = cs.listOfUsersByChat.get(Integer.valueOf(m.getArgAsString(0)));
                    list.remove(m.getSession());
                    cs.listOfUsersByChat.replace(Integer.valueOf(m.getArgAsString(0)), list);
                }
            }
        }

        if (destinations != null) {
            log("Messaging: " + Arrays.toString(destinations) + "\nSending: " + message);
            for (String s : destinations)
                messagingTemplate.convertAndSend(s, message);
        }
    }
}
