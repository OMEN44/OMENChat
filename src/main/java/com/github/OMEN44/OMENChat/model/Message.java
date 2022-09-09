package com.github.OMEN44.OMENChat.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@ToString
public class Message {
    /*
    QUERIES and RESPONSES are very similar and a query must be followed with a response from the server, use these for
    getting data from the server.
    ACTIONS are for telling the other to do something and will send a success or fail response.
    UPDATES can be sent from either the client or server and should be used to carry information to the other, then the
    other with decide weather to treat it as a query or an action.

    CommandInterface:
        Q (query), R (response):
            0 - ping
            1 - login
            2 - online list
        A (action):
            0 - forward message to users
        U (update):
            0 -
    */

    //the first argument must be the command in form "R19" the rest of the arguments are command specific
    @Getter
    private String[] args;
    //date that the message was sent
    @Getter
    private final String timeSent;
    //who sent the message
    @Getter
    private final String senderId;
}
