package org.monkeyofavon.pumpkindiver;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityAirChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

public class EntityAirChangeEventListener implements Listener {
    private final App plugin;

    EntityAirChangeEventListener(App plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityAirChange(EntityAirChangeEvent event) {
        if (this.plugin.getTicks() > 0 && event != null && event.getEntity() instanceof Player)
        {
            Player player = (Player)event.getEntity();
            ItemStack helmet = player.getInventory().getHelmet();
            
            if (helmet != null && helmet.getType() == Material.CARVED_PUMPKIN) {
                int remaining = player.getRemainingAir();
                int change = event.getAmount() - remaining;

                if (change < 0 && remaining >= 0 && skipTicks(player)) {
                    event.setAmount(remaining);
                }
                else if (remaining < 0) {
                    player.getInventory().setHelmet(null);
                    player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_BREATH, 1.0f, 1.7f);
                }

            }
        }
        
    }

    boolean skipTicks(Player player) {
        boolean skip = true;
        
        if (!player.hasMetadata("skipTicks"))
            player.setMetadata("skipTicks", new FixedMetadataValue(this.plugin, 0));
        else {
            int ticks = player.getMetadata("skipTicks").get(0).asInt() + 1;
            if (ticks > this.plugin.getTicks()) {
                skip = false;
                ticks = 0;
            }
            player.setMetadata("skipTicks", new FixedMetadataValue(this.plugin, ticks));
        }
        return skip;
    }

    static boolean randomBoolean(double probability) {
        return Math.random() < probability;
    }
}