package com.oxygenclient.module.render;

import com.oxygenclient.module.Category;
import com.oxygenclient.module.Module;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import java.util.*;

public class XRay extends Module {
    public static final Set<Block> BLOCKS = new HashSet<>(Arrays.asList(
        Blocks.DIAMOND_ORE, Blocks.DEEPSLATE_DIAMOND_ORE,
        Blocks.GOLD_ORE, Blocks.DEEPSLATE_GOLD_ORE,
        Blocks.IRON_ORE, Blocks.DEEPSLATE_IRON_ORE,
        Blocks.COAL_ORE, Blocks.DEEPSLATE_COAL_ORE,
        Blocks.EMERALD_ORE, Blocks.DEEPSLATE_EMERALD_ORE,
        Blocks.REDSTONE_ORE, Blocks.DEEPSLATE_REDSTONE_ORE,
        Blocks.LAPIS_ORE, Blocks.DEEPSLATE_LAPIS_ORE,
        Blocks.ANCIENT_DEBRIS, Blocks.NETHER_GOLD_ORE,
        Blocks.COPPER_ORE, Blocks.DEEPSLATE_COPPER_ORE,
        Blocks.NETHER_QUARTZ_ORE, Blocks.CHEST, Blocks.ENDER_CHEST
    ));

    public static boolean enabled = false;

    public XRay() {
        super("XRay", "See ores through walls", Category.RENDER);
    }

    @Override
    public void onEnable() { 
        enabled = true;
        if (mc.worldRenderer != null) mc.worldRenderer.reload();
    }

    @Override
    public void onDisable() { 
        enabled = false;
        if (mc.worldRenderer != null) mc.worldRenderer.reload();
    }
}
