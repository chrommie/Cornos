package me.constantindev.ccl.features.command.impl;

import me.constantindev.ccl.etc.config.MConf;
import me.constantindev.ccl.etc.config.MConfToggleable;
import me.constantindev.ccl.etc.helper.STL;
import me.constantindev.ccl.features.command.Command;
import me.constantindev.ccl.features.module.Module;
import me.constantindev.ccl.features.module.ModuleRegistry;
import net.minecraft.util.Formatting;
import org.apache.commons.lang3.ArrayUtils;

public class Config extends Command {
    public Config() {
        super("Config", "Changes configuration of modules", new String[]{"config", "conf"});
    }

    @Override
    public void onExecute(String[] args) {
        if (args.length == 0) {
            STL.notifyUser(Formatting.RED + "Please provide a module's name, a config key and a value to set.");
        } else if (args.length == 1) {
            Module m = ModuleRegistry.search(args[0]);
            if (m == null) {
                STL.notifyUser(Formatting.RED + "Module " + args[0] + " does not exist.");
                return;
            }
            STL.notifyUser("Configuration of " + m.name);
            m.mconf.config.forEach(configKey -> STL.notifyUser("  " + configKey.key + ": " + configKey.value));
        } else if (args.length == 2) {
            Module m = ModuleRegistry.search(args[0]);
            if (m == null) {
                STL.notifyUser(Formatting.RED + "Module " + args[0] + " does not exist.");
                return;
            }
            MConf.ConfigKey cfk = m.mconf.getByName(args[1]);
            if (cfk == null) {
                STL.notifyUser(Formatting.RED + "Config key not found.");
                return;
            }
            STL.notifyUser(cfk.key + ": " + cfk.value);
        } else {
            Module m = ModuleRegistry.search(args[0]);
            if (m == null) {
                STL.notifyUser(Formatting.RED + "Module " + args[0] + " does not exist.");
                return;
            }
            MConf.ConfigKey cfk = m.mconf.getOrDefault(args[1], new MConf.ConfigKey(args[1], "0"));
            if (cfk instanceof MConfToggleable) {
                boolean newState;
                if (args[2].equalsIgnoreCase("on")) newState = true;
                else if (args[2].equalsIgnoreCase("off")) newState = false;
                else if (args[2].equalsIgnoreCase("toggle")) {
                    ((MConfToggleable) cfk).toggle();
                    STL.notifyUser("Toggled " + cfk.key + " to " + (cfk.value));
                    return;
                } else {
                    STL.notifyUser("Syntax: }config <module> <key> <<on/off/toggle>/value>");
                    return;
                }
                cfk.setValue(newState ? "on" : "off");
                STL.notifyUser("Set " + cfk.key + " to " + cfk.value);


            } else {
                cfk.setValue(String.join(" ", ArrayUtils.subarray(args, 2, args.length)));
                STL.notifyUser("Set property " + cfk.key + " to " + cfk.value);
            }

        }
    }
}
