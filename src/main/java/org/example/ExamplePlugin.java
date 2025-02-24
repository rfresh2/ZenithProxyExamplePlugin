package org.example;

import com.zenith.api.Plugin;
import com.zenith.api.PluginAPI;
import com.zenith.api.ZenithProxyPlugin;
import org.example.command.ExampleCommand;
import org.example.module.ExampleModule;
import org.slf4j.Logger;

// to indicate any MC version: @Plugin(mcVersions = "*")
@Plugin(
    id = "example-plugin",
    version = "${version}",
    description = "ZenithProxy Example Plugin",
    url = "https://github.com/rfresh2/ZenithProxyExamplePlugin",
    authors = {"rfresh2"},
    mcVersions = {"1.21.0", "1.21.4"}
)
public class ExamplePlugin implements ZenithProxyPlugin {
    // public static for easy access from modules and commands
    // or alternatively, you could pass these around in constructors
    public static ExampleConfig PLUGIN_CONFIG;
    public static Logger LOG;

    @Override
    public void onLoad(PluginAPI pluginAPI) {
        LOG = pluginAPI.getLogger(this);
        LOG.info("Example Plugin loading...");
        // initialize any configurations before modules or commands might need to read them
        PLUGIN_CONFIG = pluginAPI.registerConfig("example-plugin", ExampleConfig.class);
        pluginAPI.registerModule(new ExampleModule());
        pluginAPI.registerCommand(new ExampleCommand());
        LOG.info("Example Plugin loaded!");
    }
}
