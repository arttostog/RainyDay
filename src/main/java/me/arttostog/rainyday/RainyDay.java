package me.arttostog.rainyday;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class RainyDay extends JavaPlugin implements Listener {

    private static boolean EnableThunder = false;
    private static final List<World> worlds = new ArrayList<>();

    @Override
    public void onEnable() {
        FileConfiguration config = this.getConfig();
        config.addDefault("EnableThunder", false);
        config.addDefault("Worlds", new ArrayList<>(Collections.singletonList("RainyWorld")));
        config.options().copyDefaults(true);
        saveConfig();

        EnableThunder = config.getBoolean("EnableThunder");
        for (String world: config.getStringList("Worlds")) {
            World w = Bukkit.getWorld(world);
            if (w != null) {
                worlds.add(w);
                this.ChangeWeather(w);
            }
        }

        Bukkit.getServer().getPluginManager().registerEvents(this, this);
        this.getLogger().info("§aRainyDay successfully enabled!");
    }

    @EventHandler
    public void OnWeatherChange(WeatherChangeEvent event) {
        World world = event.getWorld();
        if (worlds.contains(world)) this.ChangeWeather(world);
    }

    private void ChangeWeather(World world) {
        this.getServer().getScheduler().scheduleSyncDelayedTask(this, () -> {
            if (world.isClearWeather()) {
                world.setStorm(true);
                world.setThundering(EnableThunder);
            }
        }, 10L);
    }
}