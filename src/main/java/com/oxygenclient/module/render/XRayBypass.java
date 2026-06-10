package com.oxygenclient.module.render;

import com.oxygenclient.module.Category;
import com.oxygenclient.module.Module;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import java.util.*;

public class XRayBypass extends Module {
    public static final Set<Block> XRAY_BLOCKS = new HashSet<>(Arrays.asList(
        Blocks.DIAMOND_ORE, Blocks.DEEPSLATE_DIAMOND_ORE,
        Blocks.GOLD_ORE, Blocks.DEEPSLATE_GOLD_ORE,
        Blocks.IRON_ORE, Blocks.DEEPSLATE_IRON_ORE,
        Blocks.COAL_ORE, Blocks.DEEPSLATE_COAL_ORE,
        Blocks.EMERALD_ORE, Blocks.DEEPSLATE_EMERALD_ORE,
        Blocks.REDSTONE_ORE, Blocks.DEEPSLATE_REDSTONE_ORE,
        Blocks.LAPIS_ORE, Blocks.DEEPSLATE_LAPIS_ORE,
        Blocks.NETHER_GOLD_ORE, Blocks.ANCIENT_DEBRIS,
        Blocks.COPPER_ORE, Blocks.DEEPSLATE_COPPER_ORE,
        Blocks.NETHER_QUARTZ_ORE,
        Blocks.CHEST, Blocks.ENDER_CHEST, Blocks.TRAPPED_CHEST,
        Blocks.SPAWNER, Blocks.TRIAL_SPAWNER,
        Blocks.BARREL, Blocks.SHULKER_BOX
    ));

    public XRayBypass() {
        super("XRay", "X-Ray - Tamamen tespit edilemez (sadece client-side)", Category.RENDER);
    }

    @Override
    public void onEnable() {
        if (mc.worldRenderer != null) {
            mc.worldRenderer.reload();
        }
    }

    @Override
    public void onDisable() {
        if (mc.worldRenderer != null) {
            mc.worldRenderer.reload();
        }
    }

    public static boolean shouldRender(Block block) {
        return XRAY_BLOCKS.contains(block);
    }
}
