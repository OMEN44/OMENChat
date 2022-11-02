package com.github.omen.controller.endpoints;

import com.github.omen.controller.database.ChatsRepo;
import com.github.omen.controller.database.MembersRepo;
import com.github.omen.controller.database.MessagesRepo;
import com.github.omen.controller.database.UsersRepo;
import com.github.omen.controller.database.entities.Member;
import com.github.omen.controller.database.entities.User;
import com.github.omen.model.MessageTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Controller
public class Login {
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

    @MessageMapping("/login")
    public void onLoginRequest(@Payload MessageTemplate m) {
        switch (m.getLabel()) {
            case "login":
                MessageTemplate message;

                User u = ur.findUserByUserNameEquals((String) m.getArgs()[0]);
                if (u == null) {
                    // user does not exist
                    message = MessageTemplate.argsOnly("no-account");
                } else if (!Objects.equals(u.getPassword(), m.getArgs()[1])) {
                    // password does not match
                    message = MessageTemplate.argsOnly("incorrect");
                } else {
                    //logged in!
                    message = MessageTemplate.argsOnly(
                            "success", String.valueOf(u.getColourSchemeId()), String.valueOf(u.getId())
                    );
                    u.setLongitude((double) m.getArgs()[2]);
                    u.setLatitude((double) m.getArgs()[3]);
                    ur.save(u);
                }

                messagingTemplate.convertAndSend(
                        "/login/" + m.getArgs()[6],
                        message
                );
                break;
            case "createAcc":
                if (!ur.existsUserByUserNameEquals((String) m.getArgs()[0])) {
                    ur.save(new User(
                            0,
                            (String) m.getArgs()[0],
                            (String) m.getArgs()[1],
                            0,
                            (double) m.getArgs()[3],
                            (double) m.getArgs()[2]
                    ));
                    messagingTemplate.convertAndSend(
                            "/login/" + m.getArgs()[4],
                            MessageTemplate.argsOnly(
                                    "success",
                                    String.valueOf(0),
                                    String.valueOf(ur.findUserByUserNameEquals((String) m.getArgs()[0]).getId())
                            )
                    );
                } else {
                    messagingTemplate.convertAndSend(
                            "/login/" + m.getArgs()[4],
                            MessageTemplate.argsOnly("cannot-create")
                    );
                }
                break;
            case "getChats":
                List<Member> chats = memr.findMembersByUserIdEquals(m.getSenderId());
                System.out.println(Arrays.toString(chats.toArray()));
                messagingTemplate.convertAndSend(
                        "/chat-selector/" + m.getArgs()[0],
                        MessageTemplate.argsOnly(chats.toArray())
                );
                break;
        }
    }
}
