package com.github.OMEN44.OMENChat.controller;

import com.github.OMEN44.command.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.jar.Attributes;

public class CommandLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketEventListener.class);

    public CommandLoader() {
        try {
            File f = new File("commands");
            //create commands directory
            if (!f.exists()) {
                if (f.mkdir()) {
                    LOGGER.info("Generated commands directory");
                } else {
                    throw new IOException();
                }
            }

        } catch (IOException e) {
            LOGGER.error("Unable to create commands directory!");
        }
    }

    public void load() {
        //get all the jar files in the directory
        String[] jars = new File("commands").list();
        if (jars != null && jars.length > 0) {
            for (String jar : jars) {
                System.out.println(jar);

                File jarFile = new File("commands/" + jar);
                String className = "com.github.OMEN44.CommandImpl";
                try
                {
                    URL fileURL = jarFile.toURI().toURL();
                    String jarURL = "jar:" + fileURL + "!/";
                    URL urls [] = { new URL(jarURL) };
                    URLClassLoader ucl = new URLClassLoader(urls);
                    Command m = (Command) Class.forName(className, true,   ucl).newInstance();
                    m.execute("Q1", "now", "SERVER", new String[]{"ARG1", "arg2", "hehehe"});
                }
                catch (MalformedURLException ex)
                {
                    ex.printStackTrace();
                }
                catch (InstantiationException ex)
                {
                    ex.printStackTrace();
                }
                catch (IllegalAccessException ex)
                {
                    ex.printStackTrace();
                }
                catch (ClassNotFoundException ex)
                {
                    ex.printStackTrace();
                }



            }
        }
    }

    public String getMainClassName(String url) throws IOException {
        URL u = new URL("jar", "", url + "!/");
        System.out.println(u);
        JarURLConnection uc = (JarURLConnection) u.openConnection();
        Attributes attr = uc.getMainAttributes();
        return attr != null
                ? attr.getValue(Attributes.Name.MAIN_CLASS)
                : null;
    }
}
