package org.monkeyofavon.pumpkindiver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class App extends JavaPlugin {
    private int ticks = 4;
    private static final int maxTicks = 9;

    @Override
    public void onEnable() {
        if (this.getConfig() == null) {
            this.saveDefaultConfig();
            this.getConfig().set("ticks", this.ticks);
            this.saveConfig();
        }
        this.ticks = this.getConfig().getInt("ticks");
        
        this.getServer().getPluginManager().registerEvents(new EntityAirChangeEventListener(this), this);

        this.getCommand("pumpkindiver").setTabCompleter(
            new TabCompleter() {
                @Override
                public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
                    if (sender instanceof Player && args.length <= 1) {
                        String[] argArray = {"0","1","2","3","4","5","6","7","8","9"};
                        List<String> argList = new ArrayList<>(Arrays.asList(argArray));
                        return argList;
                    }
                    return new ArrayList<String>();
                } 
            }
        );
    }

    @Override
    public void onDisable() {
        this.saveConfig();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("pumpkindiver") && cmd instanceof PluginCommand) {            
            switch (args.length) {
                case 0:
                    if (this.ticks > 0)
                        sender.sendMessage("PumpkinDiver in enabled and set to " + this.ticks + " ticks.");
                    else
                        sender.sendMessage("PumpkinDiver in disabled (set to " + this.ticks + " ticks).");
                    return true;
                
                case 1:
                    int newTicks;
                    try {
                        newTicks = Integer.parseInt(args[0]);
                    }
                    catch (NumberFormatException e) {
                        sender.sendMessage("Invalid integer " + args[0]);
                        break;
                    }
                    setTicks(sender, newTicks);
                    return true;
              }
        } 
	    return false; 
    }

    public void setTicks(CommandSender sender, int newTicks) {
        this.ticks = Math.max(0, Math.min(maxTicks, newTicks));
        this.getConfig().set("ticks", this.ticks);
        this.saveConfig();
        
        if (newTicks != this.ticks)
            sender.sendMessage("Argument " + newTicks + " out of range (0-9).");

        if (this.ticks > 0)
            sender.sendMessage("Set to " + this.ticks + " ticks.");
        else
            sender.sendMessage("Disabled (set to " + this.ticks + " ticks).");
    }

    public int getTicks() {
        return this.ticks;
    }
}