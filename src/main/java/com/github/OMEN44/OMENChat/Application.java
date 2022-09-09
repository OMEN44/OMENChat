package com.github.OMEN44.OMENChat;

import com.github.OMEN44.OMENChat.controller.Command;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Map;

@SpringBootApplication
public class Application {

    private Map<String, Command> commandMap;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        new Application();
    }

    public Application() {
        setCommandExecutor("Q0", n);
    }

    public void setCommandExecutor(String commandId, Command commandExecutor) {
        if (commandId != null && commandExecutor != null) {
            if (!commandMap.containsKey(commandId)) {
                commandMap.put(commandId, commandExecutor);
                return;
            }
        }
        throw new IllegalArgumentException("Command with this id already exists");
    }

    public Command getCommandExecutor(String commandId) {
        if (commandId != null) {
            return commandMap.get(commandId);
        }
        throw new NullPointerException("Command id cannot be null");
    }
}
