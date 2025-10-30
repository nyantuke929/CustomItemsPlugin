package com.example.customitems.listeners;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.example.customitems.api.VersionedCustomItemAPI;

/**
 * カスタムアイテムの右クリックイベントを処理
 */
public class ItemInteractListener implements Listener {
    
    private final VersionedCustomItemAPI api;
    
    public ItemInteractListener(VersionedCustomItemAPI api) {
        this.api = api;
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR && 
            event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        
        if (item == null || !api.isCustomItem(item)) {
            return;
        }
        
        String itemId = api.getCustomItemId(item);
        if (itemId == null) {
            return;
        }
        
        event.setCancelled(true);
        
        switch (itemId) {
            case "flame_sword" -> handleFlameSword(player);
            case "thunder_hammer" -> handleThunderHammer(player);
            case "healing_potion" -> handleHealingPotion(player, item);
            case "magic_wand" -> handleMagicWand(player);
            case "teleport_stone" -> handleTeleportStone(player, event.getAction() == Action.RIGHT_CLICK_AIR && player.isSneaking());
            default -> player.sendMessage("§eこのアイテムには特殊効果がありません");
        }
    }
    
    private void handleFlameSword(Player player) {
        // 火の玉を発射
        Fireball fireball = player.launchProjectile(Fireball.class);
        fireball.setYield(2.0F);
        
        player.getWorld().spawnParticle(Particle.FLAME, player.getLocation(), 20, 0.5, 0.5, 0.5, 0.1);
        player.playSound(player.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 1.0F, 1.0F);
        player.sendMessage("§c火の玉を発射した！");
    }
    
    private void handleThunderHammer(Player player) {
        // プレイヤーの視線方向に雷を召喚
        Location target = player.getTargetBlock(null, 50).getLocation();
        player.getWorld().strikeLightning(target);
        
        player.getWorld().spawnParticle(Particle.ELECTRIC_SPARK, player.getLocation(), 30, 0.5, 0.5, 0.5, 0.2);
        player.playSound(player.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1.0F, 1.0F);
        player.sendMessage("§b雷を召喚した！");
    }
    
    private void handleHealingPotion(Player player, ItemStack item) {
        // 体力全回復
        player.setHealth(player.getMaxHealth());
        player.setFoodLevel(20);
        player.setSaturation(20.0F);
        
        // バフ効果
        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 200, 1));
        player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 600, 1));
        
        player.getWorld().spawnParticle(Particle.HEART, player.getLocation().add(0, 1, 0), 10, 0.5, 0.5, 0.5, 0.1);
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.5F);
        player.sendMessage("§a体力が全回復した！");
        
        // アイテムを1つ消費
        if (item.getAmount() > 1) {
            item.setAmount(item.getAmount() - 1);
        } else {
            player.getInventory().setItemInMainHand(null);
        }
    }
    
    private void handleMagicWand(Player player) {
        // ランダムな魔法効果
        int random = (int) (Math.random() * 4);
        
        switch (random) {
            case 0 -> {
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 400, 2));
                player.sendMessage("§5スピードアップの魔法を唱えた！");
                player.playSound(player.getLocation(), Sound.ENTITY_WITCH_CELEBRATE, 1.0F, 1.5F);
            }
            case 1 -> {
                player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP_BOOST, 400, 2));
                player.sendMessage("§5ジャンプ強化の魔法を唱えた！");
                player.playSound(player.getLocation(), Sound.ENTITY_WITCH_CELEBRATE, 1.0F, 1.2F);
            }
            case 2 -> {
                player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 600, 0));
                player.sendMessage("§5暗視の魔法を唱えた！");
                player.playSound(player.getLocation(), Sound.ENTITY_WITCH_CELEBRATE, 1.0F, 1.0F);
            }
            case 3 -> {
                player.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, 600, 0));
                player.sendMessage("§5水中呼吸の魔法を唱えた！");
                player.playSound(player.getLocation(), Sound.ENTITY_WITCH_CELEBRATE, 1.0F, 0.8F);
            }
        }
        
        player.getWorld().spawnParticle(Particle.WITCH, player.getLocation().add(0, 1, 0), 20, 0.5, 0.5, 0.5, 0.1);
    }
    
    private void handleTeleportStone(Player player, boolean isSneaking) {
        if (isSneaking) {
            // Shift+右クリック: テレポート実行
            // TODO: 保存した座標にテレポート（実装例）
            player.sendMessage("§dテレポート機能は未実装です");
            player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0F, 1.0F);
        } else {
            // 右クリック: 現在地を保存
            Location loc = player.getLocation();
            player.sendMessage("§d現在地を保存しました: " + 
                String.format("X:%.1f Y:%.1f Z:%.1f", loc.getX(), loc.getY(), loc.getZ()));
            player.playSound(player.getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 1.0F, 1.5F);
            player.getWorld().spawnParticle(Particle.PORTAL, player.getLocation(), 30, 0.5, 0.5, 0.5, 0.5);
        }
    }
}