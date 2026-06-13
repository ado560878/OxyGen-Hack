package com.oxygenclient.module.combat;

import com.oxygenclient.module.Category;
import com.oxygenclient.module.Module;
import com.oxygenclient.module.settings.ModeSetting;
import com.oxygenclient.module.settings.NumberSetting;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;

public class KillAura extends Module {
    
    public NumberSetting range = new NumberSetting("Range", 4.5, 2.0, 6.0, 0.1);
    public NumberSetting cps = new NumberSetting("CPS", 10, 1, 20, 1);
    public ModeSetting priority = new ModeSetting("Priority", "Distance", "Distance", "Health", "Angle");
    
    private long lastAttack = 0;
    
    public KillAura() {
        super("KillAura", "Automatically attacks entities", Category.COMBAT);
        settings.add(range);
        settings.add(cps);
        settings.add(priority);
    }
    
    @Override
    public void onTick() {
        if (mc.player == null || mc.world == null) return;
        
        LivingEntity target = getNearestEntity();
        if (target == null) return;
        
        long now = System.currentTimeMillis();
        int delay = (int) (1000.0 / cps.getValue());
        
        if (now - lastAttack >= delay) {
            mc.interactionManager.attackEntity(mc.player, target);
            mc.player.swingHand(Hand.MAIN_HAND);
            lastAttack = now;
        }
    }
    
    private LivingEntity getNearestEntity() {
        double minDist = range.getValue();
        LivingEntity closest = null;
        
        for (LivingEntity entity : mc.world.getEntitiesByClass(LivingEntity.class, 
                mc.player.getBoundingBox().expand(range.getValue()), 
                e -> e != mc.player && e.isAlive() && !(e instanceof PlayerEntity && ((PlayerEntity)e).isCreative()))) {
            
            double dist = mc.player.distanceTo(entity);
            if (dist < minDist) {
                minDist = dist;
                closest = entity;
            }
        }
        return closest;
    }
    
    @Override
    public String getDisplayValue() {
        return "§7" + range.getValue();
    }
}
