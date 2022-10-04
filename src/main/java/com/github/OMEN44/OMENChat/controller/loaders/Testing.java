package com.github.OMEN44.OMENChat.controller.loaders;

import com.github.OMEN44.OmenChatPlugin;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

public class Testing {
    public static void onLoad() throws IOException {
        File loc = new File("commands");

        File[] flist = loc.listFiles(file -> file.getPath().toLowerCase().endsWith(".jar"));
        assert flist != null;
        URL[] urls = new URL[flist.length];
        for (int i = 0; i < flist.length; i++)
            urls[i] = flist[i].toURI().toURL();
        URLClassLoader ucl = new URLClassLoader(urls);

        for(URL url : ucl.getURLs()){
            if (Objects.equals(url.getFile(), "/C:/Users/huons/Documents/Programming/JavaPrograms/OMENChat/commands/TestCommand-with-properties.jar")) {
                Properties p = new Properties();
                p.load(ucl.getResourceAsStream(""));
                System.out.println(Arrays.toString(p.keySet().toArray()));
            }
            System.out.println("Found Plugin: " + url.getFile());
        }
        ServiceLoader<OmenChatPlugin> sl = ServiceLoader.load(OmenChatPlugin.class, ucl);

        for (OmenChatPlugin omenChatPlugin : sl) {
            System.out.println("Loading Plugin");
            omenChatPlugin.getCommandFactories().get(0).build().execute("1", "2", "3", new String[]{});
        }
    }
}
