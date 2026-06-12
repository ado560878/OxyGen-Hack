package com.oxygenclient.module.combat;

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

public class KillAura extends Module {
    private final NumberSetting range = new NumberSetting("Range", "Attack range", 4.0, 1.0, 6.0, 0.1);
    private final NumberSetting cps = new NumberSetting("CPS", "Clicks per second", 10, 1, 20, 1);
    private final BooleanSetting players = new BooleanSetting("Players", "Attack players", true);
    private final BooleanSetting mobs = new BooleanSetting("Mobs", "Attack mobs", true);
    private long delay = 0;

    public KillAura() {
        super("KillAura", "Auto attack entities", Category.COMBAT);
        addSettings(range, cps, players, mobs);
    }

    @Override
    public void onTick() {
        if (mc.player == null || mc.world == null) return;
        if (System.currentTimeMillis() < delay) return;

        double r = range.getValue();
        Box box = new Box(mc.player.getX()-r, mc.player.getY()-r, mc.player.getZ()-r,
                          mc.player.getX()+r, mc.player.getY()+r, mc.player.getZ()+r);

        List<Entity> targets = mc.world.getOtherEntities(mc.player, box, e -> {
            if (!e.isAlive()) return false;
            if (players.getValue() && e instanceof PlayerEntity) return true;
            if (mobs.getValue() && e instanceof HostileEntity) return true;
            return false;
        });

        if (targets.isEmpty()) return;

        Entity target = targets.stream()
            .min(Comparator.comparingDouble(e -> e.distanceTo(mc.player)))
            .orElse(null);

        if (target != null) {
            mc.interactionManager.attackEntity(mc.player, target);
            mc.player.swingHand(Hand.MAIN_HAND);
            delay = System.currentTimeMillis() + (1000 / (int)cps.getValue());
        }
    }
}
