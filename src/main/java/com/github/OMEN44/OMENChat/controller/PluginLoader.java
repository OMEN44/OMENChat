package com.github.OMEN44.OMENChat.controller;

import com.github.OMEN44.OmenChatPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

import static com.github.OMEN44.OMENChat.Main.PLUGIN_MGR;

public class PluginLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(PluginLoader.class);

    public static List<OmenChatPlugin> loadPlugins(File location) {
        List<OmenChatPlugin> pl = new ArrayList<>();
        try {
            File[] files = location.listFiles(file -> file.getPath().toLowerCase().endsWith(".jar"));
            assert files != null;
            URLClassLoader[] ucls = new URLClassLoader[files.length];
            for (int i = 0; i < files.length; i++)
                ucls[i] = new URLClassLoader(new URL[]{files[i].toURI().toURL()});
            for (URLClassLoader ucl : ucls)
                pl.add(ServiceLoader.load(OmenChatPlugin.class, ucl).iterator().next());
        } catch (IOException e) {
            return null;
        }
        return pl;
    }

    public static PluginManager enablePlugins(List<OmenChatPlugin> pluginList) {
        PluginManager pm = new PluginManager();
        try {
            if (pluginList != null) {
                pm.init(pluginList);
                LOGGER.info(pm.getPLUGIN_MAP().size() + " plugins found. Loading now...");
                for (String n : pm.getPLUGIN_MAP().keySet())
                    pm.getPLUGIN_MAP().get(n).onEnable();
            } else LOGGER.info("No plugins found. Nothing to load.");
        } catch (Exception e) {
            return null;
        }
        return pm;
    }

    public static String makeCommandTree() {
        StringBuilder sb = new StringBuilder("Commands");
        if (PLUGIN_MGR != null) {
            List<String> plugins = PLUGIN_MGR.getPLUGIN_MAP().keySet().stream().toList();
            int indexA = plugins.size();

            //add plugins
            for (int i = 0; i < indexA; i++) {
                if ((indexA - 1) == i) sb.append("\n└───");
                else sb.append("\n├───");
                sb.append(plugins.get(i));

                List<String> commands = new ArrayList<>();
                int index = 0;
                for (String name : PLUGIN_MGR.getPlugin(plugins.get(i)).getCommandExecutors().keySet().stream().toList()) {
                    if (name.startsWith(plugins.get(i))) {
                        commands.add(name);
                        index += 1;
                    }
                }

                //add commands
                for (int j = 0; j < index; j++) {
                    if ((indexA - 1) != i) {
                        if ((index - 1) == j) sb.append("\n│   └───");
                        else sb.append("\n│   ├───");
                    } else {
                        if ((index - 1) == j) sb.append("\n    └───");
                        else sb.append("\n    ├───");
                    }
                    sb.append(commands.get(j).substring(plugins.get(i).length()));
                }
            }
        }
        return sb.toString();
    }
}
