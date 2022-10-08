package com.github.omen;

import com.github.omen.controller.PluginLoader;
import com.github.omen.controller.PluginManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;

@SpringBootApplication
public class Main {

    public static final PluginManager PLUGIN_MGR = PluginLoader.enablePlugins(
            PluginLoader.loadPlugins(new File("commands"))
    );

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
        System.out.println(PluginLoader.makeCommandTree());
        assert PLUGIN_MGR != null;
    }
}
