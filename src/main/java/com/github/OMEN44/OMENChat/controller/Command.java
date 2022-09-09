package com.github.OMEN44.OMENChat.controller;

public interface Command {
    void onCommand(String label, String sender, String[] args);
}
