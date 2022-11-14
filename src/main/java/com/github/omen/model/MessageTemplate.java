package com.github.omen.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.Date;

@AllArgsConstructor
@ToString
public class MessageTemplate {
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
    //the label is the command id for example: A12
    @Getter
    private String group;
    @Getter
    private String label;
    //the arguments are command specific
    @Getter
    private Object[] args;
    //date that the message was sent
    @Getter
    private final Date timeSent;
    //who sent the message
    @Getter
    private final int senderId;

    public static MessageTemplate currentDate(String group, String label, int senderId, Object... args) {
        return new MessageTemplate(group, label, args, new Date(), senderId);
    }

    public static MessageTemplate currentDateGroupless(String label, int senderId, Object... args) {
        return new MessageTemplate(null, label, args, new Date(), senderId);
    }

    public static MessageTemplate senderWithArgs(int senderId, Object... args) {
        return new MessageTemplate(null, null, args, new Date(), senderId);
    }

    public static MessageTemplate sentBySystem(String group, String label, Object... args) {
        return new MessageTemplate(group, label, args, new Date(), 0);
    }

    public static MessageTemplate sentBySystemGroupless(String label, Object... args) {
        return new MessageTemplate(null, label, args, new Date(), 0);
    }

    public static MessageTemplate argsOnly(Object... args) {
        return new MessageTemplate(null, null, args, new Date(), 0);
    }

    public Object getArg(int index) {
        return this.args[index];
    }

    public String getArgAsString(int index) {
        return (String) this.args[index];
    }

    public String getSession() {
        try {
            if (this.args.length > 0) return (String) this.args[this.args.length - 1]; else return null;
        } catch (ClassCastException e) {
            return null;
        }
    }
}
