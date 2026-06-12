package com.oxygenclient.module.combat;

import com.oxygenclient.module.Category;
import com.oxygenclient.module.Module;
import com.oxygenclient.module.settings.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import java.util.Comparator;
import java.util.List;

public class SilentAim extends Module {
    private final NumberSetting range = new NumberSetting("Range", "Aim range", 6.0, 1.0, 10.0, 0.1);

    public SilentAim() {
        super("SilentAim", "Aim without moving screen", Category.COMBAT);
        addSetting(range);
    }

    @Override
    public void onTick() {
        if (mc.player == null || !mc.options.attackKey.isPressed()) return;

        double r = range.getValue();
        Box box = new Box(mc.player.getX()-r, mc.player.getY()-r, mc.player.getZ()-r,
                          mc.player.getX()+r, mc.player.getY()+r, mc.player.getZ()+r);

        List<Entity> targets = mc.world.getOtherEntities(mc.player, box,
            e -> e instanceof PlayerEntity && e.isAlive());
        if (targets.isEmpty()) return;

        Entity t = targets.stream().min(Comparator.comparingDouble(e -> e.distanceTo(mc.player))).orElse(null);
        if (t != null) {
            double dx = t.getX()-mc.player.getX(), dy = t.getEyeY()-mc.player.getEyeY(), dz = t.getZ()-mc.player.getZ();
            double dist = Math.sqrt(dx*dx+dz*dz);
            mc.player.setYaw((float)(MathHelper.atan2(dz,dx)*180/Math.PI)-90f);
            mc.player.setPitch((float)(-MathHelper.atan2(dy,dist)*180/Math.PI));
        }
    }
}
