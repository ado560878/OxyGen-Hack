package com.oxygenclient.module.combat;

import com.oxygenclient.OxygenClient;
import com.oxygenclient.module.Category;
import com.oxygenclient.module.Module;
import com.oxygenclient.module.settings.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Box;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class KillAura extends Module {
    private final NumberSetting range = new NumberSetting("Range", "Attack range", 4.0, 1.0, 6.0, 0.1);
    private final NumberSetting cps = new NumberSetting("CPS", "Clicks per second", 12, 1, 20, 1);
    private final NumberSetting switchDelay = new NumberSetting("Switch", "Target switch delay (ms)", 100, 0, 500, 50);
    private final BooleanSetting players = new BooleanSetting("Players", "Attack players", true);
    private final BooleanSetting mobs = new BooleanSetting("Mobs", "Attack hostile mobs", true);
    private final BooleanSetting autoBlock = new BooleanSetting("AutoBlock", "Auto shield block", false);
    
    private Entity currentTarget = null;
    private long lastAttack = 0;
    private long lastSwitch = 0;
    private final Random random = new Random();

    public KillAura() {
        super("KillAura", "Auto attack with combo system", Category.COMBAT);
        addSettings(range, cps, switchDelay, players, mobs, autoBlock);
    }

    @Override
    public void onTick() {
        if (mc.player == null || mc.world == null || mc.player.isDead()) return;
        
        long now = System.currentTimeMillis();
        int attackDelay = 1000 / (int) cps.getValue();
        
        // Hız sınırı
        if (now - lastAttack < attackDelay) return;
        
        double r = range.getValue();
        Box box = new Box(
            mc.player.getX() - r, mc.player.getY() - r, mc.player.getZ() - r,
            mc.player.getX() + r, mc.player.getY() + r, mc.player.getZ() + r
        );

        List<Entity> targets = mc.world.getOtherEntities(mc.player, box, e -> {
            if (!e.isAlive() || e == mc.player) return false;
            if (players.getValue() && e instanceof PlayerEntity) return true;
            if (mobs.getValue() && e instanceof HostileEntity) return true;
            return false;
        });

        if (targets.isEmpty()) {
            currentTarget = null;
            return;
        }

        // Hedef seçimi
        if (currentTarget == null || !currentTarget.isAlive() || 
            currentTarget.distanceTo(mc.player) > r + 1 ||
            (now - lastSwitch > switchDelay.getValue())) {
            
            currentTarget = targets.stream()
                .min(Comparator.comparingDouble(e -> e.distanceTo(mc.player)))
                .orElse(null);
            lastSwitch = now;
        }

        if (currentTarget == null) return;

        // Criticals modülü açık mı kontrol et
        Module criticals = OxygenClient.moduleManager.getModules().stream()
            .filter(m -> m.getName().equals("Criticals") && m.isEnabled())
            .findFirst().orElse(null);
        
        boolean doCrit = criticals != null && mc.player.isOnGround();

        // Kritik vuruş kombo: önce zıpla, sonra vur
        if (doCrit) {
            // 1.21 tarzı: havada kritik vuruş
            mc.player.jump();
            // Havada bekle
            if (mc.player.getY() > mc.player.prevY + 0.1) {
                attack(currentTarget);
            }
        } else {
            // Normal seri vuruş
            attack(currentTarget);
        }

        lastAttack = now;
        
        // Otomatik kalkan
        if (autoBlock.getValue() && mc.player.getOffHandStack().isEmpty()) {
            mc.options.useKey.setPressed(true);
        }
    }

    private void attack(Entity target) {
        // Hedefe dön
        double dx = target.getX() - mc.player.getX();
        double dy = target.getEyeY() - mc.player.getEyeY();
        double dz = target.getZ() - mc.player.getZ();
        double dist = Math.sqrt(dx * dx + dz * dz);
        
        float yaw = (float) (Math.atan2(dz, dx) * 180.0 / Math.PI) - 90.0f;
        float pitch = (float) (-Math.atan2(dy, dist) * 180.0 / Math.PI);
        
        // Hızlı dönüş
        mc.player.setYaw(yaw + (random.nextFloat() - 0.5f) * 2.0f);
        mc.player.setPitch(pitch + (random.nextFloat() - 0.5f) * 1.0f);
        
        // Vuruş
        mc.interactionManager.attackEntity(mc.player, target);
        mc.player.swingHand(Hand.MAIN_HAND);
        
        // Reset shield
        if (autoBlock.getValue()) {
            mc.options.useKey.setPressed(false);
        }
    }

    @Override
    public void onDisable() {
        currentTarget = null;
        mc.options.useKey.setPressed(false);
    }
}
