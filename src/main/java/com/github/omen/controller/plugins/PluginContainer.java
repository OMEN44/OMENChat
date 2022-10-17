package com.github.omen.controller.plugins;

import com.github.OMEN44.OmenChatPlugin;

import java.util.HashMap;
import java.util.Map;

public class PluginContainer {
    private final Map<String, OmenChatPlugin> PLUGIN_MAP = new HashMap<>();

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


    public Map<String, OmenChatPlugin> getPLUGIN_MAP() {
        return this.PLUGIN_MAP;
    }
}
