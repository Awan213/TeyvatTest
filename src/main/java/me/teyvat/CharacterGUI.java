
package me.teyvat;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CharacterGUI {
    public static void open(Player player) {
        Inventory gui = Bukkit.createInventory(null, 27, "Pilih Karakter");

        ItemStack yoimiya = new ItemStack(Material.CROSSBOW);
        ItemMeta meta = yoimiya.getItemMeta();
        meta.setDisplayName("§6Yoimiya");
        yoimiya.setItemMeta(meta);
        gui.setItem(11, yoimiya);

        ItemStack ei = new ItemStack(Material.BLAZE_ROD);
        meta = ei.getItemMeta();
        meta.setDisplayName("§dEi");
        ei.setItemMeta(meta);
        gui.setItem(15, ei);

        player.openInventory(gui);
    }
}
