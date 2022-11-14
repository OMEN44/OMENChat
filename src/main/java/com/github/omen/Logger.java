package com.github.omen;

public class Logger {

    public static final String RESET = "\u001B[0m";
    public static final String GREEN_BACK = "\u001B[42m";
    public static final String RED_BACK = "\u001B[41m";
    public static final String YELLOW_BACK = "\u001B[43m";
    public static final String BLACK = "\u001B[30m";

    public static void log(String message) {
        if (message.contains("\n")) System.out.println(GREEN_BACK + BLACK + "[OCS-INFO]:" + RESET + "\n" + message);
        else System.out.println(GREEN_BACK + BLACK + "[OCS-INFO]" + RESET + " " + message);
    }

    public static void warn(String message) {
        if (message.contains("\n")) System.out.println(YELLOW_BACK + BLACK + "[OCS-WARN]:" + RESET + "\n" + message);
        else System.out.println(YELLOW_BACK + BLACK + "[OCS-WARN]" + RESET + " " + message);
    }

    public static void error(String message) {
        if (message.contains("\n")) System.out.println(RED_BACK + BLACK + "[OCS-ERROR]:" + RESET + "\n" + message);
        else System.out.println(RED_BACK + BLACK + "[OCS-ERROR]" + RESET + " " + message);
    }
}
