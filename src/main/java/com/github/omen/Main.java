package com.github.omen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Scanner;

import static com.github.omen.Logger.*;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        ConfigurableApplicationContext app = SpringApplication.run(Main.class, args);
        boolean running = true;
        while (running) {
            switch (new Scanner(System.in).nextLine().toLowerCase()) {
                case "quit" -> {
                    app.close();
                    running = false;
                    log("OmenChat has closed! Goodbye.");
                }
                case "stop" -> {
                    if (app.isActive()) {
                        app.close();
                        log("OmenChat has been stopped! use command 'start' to begin it again.");
                    }
                }
                case "start" -> {
                    if (!app.isActive()) {
                        app = SpringApplication.run(Main.class, args);
                        log("OmenChat has been started! use command 'stop' to stop it again.");
                    }
                }
                case "help" -> log("""
                        CommandInterface for OMENchat server
                        =======================
                        list   - Shows a list of all the currently connected users
                        quit   - Ends this instance of the server kicking all the connected users
                        =======================""");
                case "list" -> {

                }
            }
        }
    }
}
