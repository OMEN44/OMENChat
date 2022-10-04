package com.github.OMEN44.OMENChat.controller;

import java.util.HashMap;
import java.util.Map;

public class CommandManager {
    private final Map<String, String> commandMap = new HashMap<>();

    public Map<String, String> getCommandMap() {
        return commandMap;
    }

    public CommandManager setCommandExecutor(String commandId, String commandExecutor) {
        if (commandId != null && commandExecutor != null) {
            if (!commandMap.containsKey(commandId)) {
                commandMap.put(commandId, commandExecutor);
                return this;
            }
        }
        throw new IllegalArgumentException("Command with this id already exists");
    }

    public String getCommandExecutor(String commandId) {
        if (commandId != null) {
            return commandMap.get(commandId);
        }
        throw new NullPointerException("Command id cannot be null");
    }


}
