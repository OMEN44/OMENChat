package com.github.OMEN44.OMENChat.controller.loaders;

import com.github.OMEN44.OmenChatPlugin;
import com.github.OMEN44.command.CommandFactory;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.util.Objects.requireNonNull;

public class PluginLoader {
    private final Map<String, CommandFactory> commandFactoryMap = new HashMap<>();
    private final File pluginsDir;
    private final AtomicBoolean loading = new AtomicBoolean();

    public PluginLoader(final File pluginsDir) {
        this.pluginsDir = pluginsDir;
    }

    public void loadCommands() {
        if (!pluginsDir.exists() || !pluginsDir.isDirectory()) {
            System.err.println("Skipping Plugin Loading. Plugin dir not found: " + pluginsDir);
            return;
        }

        if (loading.compareAndSet(false, true)) {
            final File[] files = requireNonNull(pluginsDir.listFiles());
            for (File pluginDir : files) {
                if (pluginDir.isDirectory()) {
                    loadCommand(pluginDir);
                }
            }
        }
    }

    private void loadCommand(final File pluginDir) {
        System.out.println("Loading plugin: " + pluginDir);
        final URLClassLoader pluginClassLoader = createPluginClassLoader(pluginDir);
        final ClassLoader currentClassLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(pluginClassLoader);
            // broken
            for (OmenChatPlugin omenChatPlugin : ServiceLoader.load(OmenChatPlugin.class, pluginClassLoader)) {
                installPlugin(omenChatPlugin);
            }
        } finally {
            Thread.currentThread().setContextClassLoader(currentClassLoader);
        }
    }


    private void installPlugin(final OmenChatPlugin plugin) {
        System.out.println("Installing plugin: " + plugin.getClass().getName());
        for (CommandFactory c : plugin.getCommandFactories()) {
            commandFactoryMap.put(c.name(), c);
        }
    }

    private URLClassLoader createPluginClassLoader(File dir) {
        final URL[] urls = Arrays.stream(Optional.of(dir.listFiles()).orElse(new File[]{}))
                .sorted()
                .map(File::toURI)
                .map(this::toUrl)
                .toArray(URL[]::new);

        System.out.println("cpcl: " + Arrays.toString(urls));

        return new PluginClassLoader(urls, getClass().getClassLoader());
    }

    private URL toUrl(final URI uri) {
        try {
            return uri.toURL();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public CommandFactory getCommandFactory(String name) {
        return commandFactoryMap.get(name);
    }
}
