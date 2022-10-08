package com.github.omen.controller;

import com.github.OMEN44.OmenChatPlugin;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PluginManager {
    @Getter
    private final Map<String, OmenChatPlugin> PLUGIN_MAP = new HashMap<>();
    //private static final Logger LOGGER = LoggerFactory.getLogger(PluginManager.class);

    public void init(List<OmenChatPlugin> pluginList) {
        List<String> l = new ArrayList<>();
        for (OmenChatPlugin o : pluginList) {
            if (!PLUGIN_MAP.containsKey(o.getName()))
                setPlugin(o.getName(), o);
            else l.add(o.getName());
        }
        if (l.size() != 0) {
            System.out.println("The following plugins had duplicate id's and were skipped:");
            for (String s : l) System.out.println(s);
        }
    }

    public void setPlugin(String id, OmenChatPlugin omenChatPlugin) {
        if (id != null && omenChatPlugin != null) {
            if (!PLUGIN_MAP.containsKey(id)) {
                PLUGIN_MAP.put(id, omenChatPlugin);
                return;
            }
        }
        throw new IllegalArgumentException("Command with this id already exists");
    }

    public OmenChatPlugin getPlugin(String id) {
        if (id != null) {
            return PLUGIN_MAP.get(id);
        }
        throw new NullPointerException("Command id cannot be null");
    }


}
