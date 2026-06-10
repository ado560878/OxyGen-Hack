package com.oxygenclient.module.combat;

import com.oxygenclient.bypass.AntiCheatBypass;
import com.oxygenclient.bypass.RotationSpoofer;
import com.oxygenclient.module.Category;
import com.oxygenclient.module.Module;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import java.util.Comparator;
import java.util.List;

public class SilentAim extends Module {
    private double range = 6.0;

    public SilentAim() {
        super("SilentAim", "Görünmez aimbot - ekranın oynamaz!", Category.COMBAT);
    }

    @Override
    public void onTick() {
        if (mc.player == null || mc.world == null) return;
        if (!mc.options.attackKey.isPressed()) return;

        Box searchBox = new Box(
            mc.player.getX() - range, mc.player.getY() - range, mc.player.getZ() - range,
            mc.player.getX() + range, mc.player.getY() + range, mc.player.getZ() + range
        );

        List<Entity> targets = mc.world.getOtherEntities(mc.player, searchBox,
            entity -> (entity instanceof HostileEntity || entity instanceof PlayerEntity)
                && entity.isAlive());

        if (targets.isEmpty()) return;

        Entity target = targets.stream()
            .min(Comparator.comparingDouble(e -> e.distanceTo(mc.player)))
            .orElse(null);

        if (target != null) {
            double dx = target.getX() - mc.player.getX();
            double dy = target.getEyeY() - mc.player.getEyeY();
            double dz = target.getZ() - mc.player.getZ();
            double dist = Math.sqrt(dx * dx + dz * dz);

            float yaw = (float) (MathHelper.atan2(dz, dx) * 180.0 / Math.PI) - 90.0f;
            float pitch = (float) (-MathHelper.atan2(dy, dist) * 180.0 / Math.PI);

            // Sadece server rotasyonunu değiştir, client ekranı aynı kalır
            RotationSpoofer.setRotation(
                yaw + AntiCheatBypass.getRandomYawOffset(),
                pitch + AntiCheatBypass.getRandomPitchOffset(),
                false
            );
        }
    }
}
