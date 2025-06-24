package me.teyvat;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;

public class TeyvatCharacter extends JavaPlugin implements Listener {

    private final NamespacedKey markKey = new NamespacedKey(this, "marked");
    private final Map<UUID, Long> domainCooldown = new HashMap<>();

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        getLogger().info("Yoimiya Plugin Enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("Yoimiya Plugin Disabled.");
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        if (e.getHand() != EquipmentSlot.HAND) return;
        Player player = e.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getType() != Material.CROSSBOW) return;
        if (!item.hasItemMeta() || !item.getItemMeta().getDisplayName().equals("§6Fox Crossbow")) return;

        if (player.isSneaking()) {
            activateUltimate(player);
        } else {
            shootBlazingMark(player);
        }
    }

    private void shootBlazingMark(Player player) {
        Arrow arrow = player.launchProjectile(Arrow.class);
        arrow.setCritical(true);
        arrow.setShooter(player);

        arrow.setMetadata("yoimiya_arrow", new org.bukkit.metadata.FixedMetadataValue(this, true));

        new BukkitRunnable() {
            @Override
            public void run() {
                if (!arrow.isValid() || arrow.isOnGround()) cancel();
                arrow.getNearbyEntities(1.5, 1.5, 1.5).forEach(entity -> {
                    if (entity instanceof LivingEntity && !entity.equals(player)) {
                        LivingEntity target = (LivingEntity) entity;
                        if (target.getPersistentDataContainer().has(markKey, PersistentDataType.INTEGER)) {
                            target.getWorld().createExplosion(target.getLocation(), 1.5F, false, false);
                            target.getPersistentDataContainer().remove(markKey);
                        } else {
                            target.getPersistentDataContainer().set(markKey, PersistentDataType.INTEGER, 1);
                            target.getWorld().spawnParticle(org.bukkit.Particle.FLAME, target.getLocation().add(0,1,0), 10, 0.3, 0.5, 0.3, 0);
                        }
                        arrow.remove();
                        cancel();
                    }
                });
            }
        }.runTaskTimer(this, 1, 1);
    }

    private void activateUltimate(Player player) {
        long now = System.currentTimeMillis();
        UUID uuid = player.getUniqueId();
        if (domainCooldown.containsKey(uuid) && now - domainCooldown.get(uuid) < 20000) {
            player.sendMessage("§cUltimate masih cooldown!");
            return;
        }
        domainCooldown.put(uuid, now);

        player.sendMessage("§6Yoimiya Ultimate: Ryuukin's Domain!");

        new BukkitRunnable() {
            int ticks = 0;
            @Override
            public void run() {
                if (ticks >= 240) {
                    cancel();
                    return;
                }
                for (int x = -2; x <= 2; x++) {
                    for (int z = -2; z <= 2; z++) {
                        Vector pos = player.getLocation().toVector().add(new Vector(x, 0, z));
                        player.getWorld().spawnParticle(org.bukkit.Particle.CRIT, pos.toLocation(player.getWorld()).add(0.5, 1, 0.5), 2, 0.1, 0.2, 0.1, 0);
                        player.getWorld().getNearbyEntities(pos.toLocation(player.getWorld()), 0.5, 1.5, 0.5).forEach(ent -> {
                            if (ent instanceof LivingEntity && !ent.equals(player)) {
                                ((LivingEntity) ent).damage(1.0, player);
                            }
                        });
                    }
                }
                ticks += 10;
            }
        }.runTaskTimer(this, 0, 10);
    }
}
