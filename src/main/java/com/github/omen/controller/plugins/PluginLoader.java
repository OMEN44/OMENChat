package com.github.omen.controller.plugins;

import com.github.OMEN44.OmenChatPlugin;
import com.github.omen.App;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

public class PluginLoader {

    public static PluginContainer loadAll(File location) {
        List<OmenChatPlugin> pl = new ArrayList<>();
        File[] files = location.listFiles(file -> file.getPath().toLowerCase().endsWith(".jar"));
        if (files != null) {
            try {
                URLClassLoader[] ucls = new URLClassLoader[files.length];
                for (int i = 0; i < files.length; i++)
                    ucls[i] = new URLClassLoader(new URL[]{files[i].toURI().toURL()}, Thread.currentThread().getContextClassLoader());
                for (URLClassLoader ucl : ucls)
                    pl.add(ServiceLoader.load(OmenChatPlugin.class, ucl).iterator().next());
            } catch (IOException e) {
                return null;
            }
        }

        return enablePlugins(pl);
    }

    public static PluginContainer enablePlugins(List<OmenChatPlugin> pluginList) {
        PluginContainer pc = new PluginContainer();
        try {
            if (pluginList != null) {

                //sort plugins into plugin container
                List<String> l = new ArrayList<>();
                for (OmenChatPlugin o : pluginList) {
                    if (!pc.getPLUGIN_MAP().containsKey(o.getName()))
                        pc.setPlugin(o.getName(), o);
                    else l.add(o.getName());
                }
                if (l.size() != 0) {
                    System.out.println("The following plugins had duplicate id's and were skipped:");
                    for (String s : l) System.out.println(s);
                }

                //enable plugins
                System.out.println(pc.getPLUGIN_MAP().size() + " plugins found. Loading now...");
                for (String n : pc.getPLUGIN_MAP().keySet())
                    pc.getPLUGIN_MAP().get(n).onEnable();
            } else System.out.println("No plugins found. Nothing to load.");
        } catch (Exception e) {
            return null;
        }
        return pc;
    }

    public static String makeCommandTree() {
        StringBuilder sb = new StringBuilder("Commands");
        if (App.PLUGIN_CONTAINER != null) {
            List<String> plugins = App.PLUGIN_CONTAINER.getPLUGIN_MAP().keySet().stream().toList();
            int indexA = plugins.size();

            //add plugins
            for (int i = 0; i < indexA; i++) {
                if ((indexA - 1) == i) sb.append("\n└───");
                else sb.append("\n├───");
                sb.append(plugins.get(i));

                List<String> commands = new ArrayList<>();
                int index = 0;
                for (String name : App.PLUGIN_CONTAINER.getPlugin(plugins.get(i)).getCommandExecutors().keySet().stream().toList()) {
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
