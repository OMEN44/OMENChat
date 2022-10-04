package com.github.OMEN44.OMENChat;

import com.github.OMEN44.OMENChat.controller.loaders.PluginLoader;
import com.github.OMEN44.OMENChat.controller.loaders.Testing;
import com.github.OMEN44.command.Command;
import com.github.OMEN44.command.CommandFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

@SpringBootApplication
public class Application {

    public static void main(String[] args) throws IOException {
        //SpringApplication.run(Application.class, args);
        Testing.onLoad();

        /*PluginLoader pluginLoader = new PluginLoader(new File("commands"));
        pluginLoader.loadCommands();

        CommandFactory commandFactory = pluginLoader.getCommandFactory("test command");
        if (commandFactory == null) {
            System.err.println("No factories loaded!");
            return;
        }

        System.out.println("This is running from the plugin");
        final Command foo = commandFactory.build();
        foo.execute("label", "now", "console", new String[]{});*/

    }
}
