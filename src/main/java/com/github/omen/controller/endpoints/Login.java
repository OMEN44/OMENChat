package com.github.omen.controller.endpoints;

import com.github.omen.controller.database.ChatsRepo;
import com.github.omen.controller.database.MembersRepo;
import com.github.omen.controller.database.MessagesRepo;
import com.github.omen.controller.database.UsersRepo;
import com.github.omen.controller.database.entities.User;
import com.github.omen.model.MessageTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Controller
public class Login {

    public final Map<Integer, String> userSessionMap = new HashMap<>();

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
        MessageTemplate message = null;
        String destination = null;
        User u;
        switch (m.getLabel()) {
            case "login" -> {
                u = ur.findUserByUserNameEquals(m.getArgAsString(0));
                if (u == null) {
                    // user does not exist
                    message = MessageTemplate.argsOnly("no-account");
                } else if (!Objects.equals(u.getPassword(), m.getArg(1))) {
                    // password does not match
                    message = MessageTemplate.argsOnly("incorrect");
                } else {
                    //logged in!
                    message = MessageTemplate.argsOnly(
                            "success", String.valueOf(u.getColourSchemeId()), String.valueOf(u.getId())
                    );
                    u.setLongitude((double) m.getArg(2));
                    u.setLatitude((double) m.getArg(3));
                    ur.save(u);
                }
                destination = "/login/" + m.getArg(6);
                if (u != null) {
                    if (userSessionMap.containsKey(u.getId()))
                        userSessionMap.replace(u.getId(), m.getArgAsString(6));
                    else
                        userSessionMap.put(u.getId(), m.getArgAsString(6));
                }
            }
            case "createAcc" -> {
                if (!ur.existsUserByUserNameEquals(m.getArgAsString(0))) {
                    ur.save(new User(
                            0,
                            m.getArgAsString(0),
                            m.getArgAsString(1),
                            0,
                            (double) m.getArg(3),
                            (double) m.getArg(2)
                    ));

                    destination = "/login/" + m.getArg(0);
                    message = MessageTemplate.argsOnly(
                            "success",
                            String.valueOf(0),
                            String.valueOf(ur.findUserByUserNameEquals(m.getArgAsString(0)).getId())
                    );

                    u = ur.findUserByUserNameEquals(m.getArgAsString(0));
                    if (userSessionMap.containsKey(u.getId()))
                        userSessionMap.replace(u.getId(), m.getArgAsString(5));
                    else
                        userSessionMap.put(u.getId(), m.getArgAsString(5));
                    break;
                } else {
                    destination = "/login/" + m.getArg(0);
                    message = MessageTemplate.argsOnly("cannot-create");
                }
            }
        }
        if (destination != null)
            messagingTemplate.convertAndSend(destination, message);
    }
}
