package com.example.customitems.listeners;

import com.example.customitems.api.NMSCustomItemAPI;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ItemInteractListener implements Listener {
    
    private final NMSCustomItemAPI itemAPI;
    
    public ItemInteractListener(NMSCustomItemAPI itemAPI) {
        this.itemAPI = itemAPI;
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        
        if (item == null || !itemAPI.isCustomItem(item)) {
            return;
        }
        
        String itemId = itemAPI.getCustomItemId(item);
        if (itemId == null) {
            return;
        }
        
        // アイテムIDごとの処理
        switch (itemId) {
            case "flame_sword":
                handleFlameSword(player, event);
                break;
            case "ice_sword":
                handleIceSword(player, event);
                break;
            case "magic_wand":
                handleMagicWand(player, event);
                break;
            case "super_heal_potion":
                handleSuperHealPotion(player, event);
                break;
            case "magic_book":
                handleMagicBook(player, event);
                break;
        }
    }
    
    private void handleFlameSword(Player player, PlayerInteractEvent event) {
        if (!event.getAction().isRightClick()) return;
        
        player.getWorld().spawnParticle(Particle.FLAME, player.getLocation(), 30, 0.5, 0.5, 0.5, 0.1);
        player.playSound(player.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 1.0f, 1.0f);
        player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 200, 0));
        player.sendMessage("§c炎の力が目覚めた！");
        
        event.setCancelled(true);
    }
    
    private void handleIceSword(Player player, PlayerInteractEvent event) {
        if (!event.getAction().isRightClick()) return;
        
        player.getWorld().spawnParticle(Particle.SNOWFLAKE, player.getLocation(), 30, 0.5, 0.5, 0.5, 0.1);
        player.playSound(player.getLocation(), Sound.BLOCK_GLASS_BREAK, 1.0f, 1.5f);
        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 200, 1));
        player.sendMessage("§b氷の力が目覚めた！");
        
        event.setCancelled(true);
    }
    
    private void handleMagicWand(Player player, PlayerInteractEvent event) {
        if (!event.getAction().isRightClick()) return;
        
        player.getWorld().spawnParticle(Particle.WITCH, player.getLocation(), 20, 0.3, 0.3, 0.3, 0.05);
        player.playSound(player.getLocation(), Sound.ENTITY_EVOKER_CAST_SPELL, 1.0f, 1.0f);
        player.sendMessage("§d魔法のエネルギーを感じる...");
        
        event.setCancelled(true);
    }
    
    private void handleSuperHealPotion(Player player, PlayerInteractEvent event) {
        if (!event.getAction().isRightClick()) return;
        
        player.setHealth(player.getMaxHealth());
        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 200, 2));
        player.getWorld().spawnParticle(Particle.HEART, player.getLocation().add(0, 1, 0), 10, 0.5, 0.5, 0.5);
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_BURP, 1.0f, 1.0f);
        player.sendMessage("§a体力が全回復した！");
        
        // アイテムを1つ減らす
        ItemStack item = event.getItem();
        if (item.getAmount() > 1) {
            item.setAmount(item.getAmount() - 1);
        } else {
            player.getInventory().setItemInMainHand(null);
        }
        
        event.setCancelled(true);
    }
    
    private void handleMagicBook(Player player, PlayerInteractEvent event) {
        if (!event.getAction().isRightClick()) return;
        
        player.sendMessage("§5=== 魔法の書 ===");
        player.sendMessage("§7この本には古代の魔法が記されている...");
        player.sendMessage("§7しかし、その内容は今はまだ解読できない。");
        player.playSound(player.getLocation(), Sound.ITEM_BOOK_PAGE_TURN, 1.0f, 1.0f);
        
        event.setCancelled(true);
    }
}