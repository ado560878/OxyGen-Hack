package com.oxygenclient.module.combat;

import com.oxygenclient.bypass.AntiCheatBypass;
import com.oxygenclient.module.Category;
import com.oxygenclient.module.Module;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import java.util.Comparator;
import java.util.List;

public class SilentAim extends Module {
    public SilentAim() { super("SilentAim", Category.COMBAT); }

    @Override
    public void onTick() {
        if (mc.player == null || !mc.options.attackKey.isPressed()) return;
        Box box = new Box(mc.player.getX()-6, mc.player.getY()-6, mc.player.getZ()-6, mc.player.getX()+6, mc.player.getY()+6, mc.player.getZ()+6);
        List<Entity> targets = mc.world.getOtherEntities(mc.player, box, e -> e instanceof PlayerEntity && e.isAlive());
        if (targets.isEmpty()) return;
        Entity t = targets.stream().min(Comparator.comparingDouble(e -> e.distanceTo(mc.player))).orElse(null);
        if (t != null) {
            double dx = t.getX()-mc.player.getX(), dy = t.getEyeY()-mc.player.getEyeY(), dz = t.getZ()-mc.player.getZ();
            double dist = Math.sqrt(dx*dx+dz*dz);
            mc.player.setYaw((float)(MathHelper.atan2(dz,dx)*180/Math.PI)-90f + AntiCheatBypass.getRandomYawOffset());
            mc.player.setPitch((float)(-MathHelper.atan2(dy,dist)*180/Math.PI) + AntiCheatBypass.getRandomPitchOffset());
        }
    }
}
