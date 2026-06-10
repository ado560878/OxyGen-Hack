package com.oxygenclient.module.combat;

import com.oxygenclient.module.Category;
import com.oxygenclient.module.Module;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import java.util.*;

public class BackTrack extends Module {
    private final Map<UUID, List<Vec3d>> history = new HashMap<>();

    public BackTrack() {
        super("BackTrack", "Hit past positions", Category.COMBAT);
    }

    @Override
    public void onTick() {
        if (mc.world == null) return;
        for (PlayerEntity p : mc.world.getPlayers()) {
            history.computeIfAbsent(p.getUuid(), k -> new ArrayList<>()).add(p.getPos());
        }
    }
}
