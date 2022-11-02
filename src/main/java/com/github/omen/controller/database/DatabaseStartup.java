package com.github.omen.controller.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import static com.github.omen.Logger.log;

@Component
public class DatabaseStartup implements CommandLineRunner {

    @Autowired
    UsersRepo ur;
    @Autowired
    ChatsRepo cr;
    @Autowired
    MessagesRepo mr;

    @Override
    public void run(String... args) throws Exception {
        log("Number of users: " + ur.count());
        log("Number of chats: " + cr.count());
    }
}
