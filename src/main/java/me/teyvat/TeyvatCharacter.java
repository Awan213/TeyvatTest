
package me.teyvat;

import org.bukkit.plugin.java.JavaPlugin;

public class TeyvatCharacter extends JavaPlugin {
    @Override
    public void onEnable() {
        getCommand("character").setExecutor(new CharacterCommand());
        getServer().getPluginManager().registerEvents(new CharacterListener(), this);
    }
}
