
package me.teyvat;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CharacterListener implements Listener {
    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getView().getTitle().equals("Pilih Karakter")) {
            e.setCancelled(true);
            Player p = (Player) e.getWhoClicked();
            ItemStack clicked = e.getCurrentItem();
            if (clicked == null || !clicked.hasItemMeta()) return;

            String name = clicked.getItemMeta().getDisplayName();
            if (name.contains("Yoimiya")) {
                ItemStack crossbow = new ItemStack(Material.CROSSBOW);
                ItemMeta meta = crossbow.getItemMeta();
                meta.setDisplayName("§6Fox Crossbow");
                meta.setCustomModelData(1);
                crossbow.setItemMeta(meta);
                p.getInventory().addItem(crossbow);
                p.sendMessage("§6Kamu memilih Yoimiya!");
            } else if (name.contains("Ei")) {
                ItemStack spear = new ItemStack(Material.BLAZE_ROD);
                ItemMeta meta = spear.getItemMeta();
                meta.setDisplayName("§dElectro Spear");
                meta.setCustomModelData(2);
                spear.setItemMeta(meta);
                p.getInventory().addItem(spear);
                p.sendMessage("§dKamu memilih Ei!");
            }

            p.closeInventory();
        }
    }
}
