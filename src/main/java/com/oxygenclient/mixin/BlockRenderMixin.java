package com.oxygenclient.mixin;

import com.oxygenclient.module.render.XRay;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;
import java.util.List;

@Mixin(BlockRenderManager.class)
public class BlockRenderMixin {
    
    // Görünecek bloklar (cevherler)
    private static final List<Block> VISIBLE_BLOCKS = Arrays.asList(
        Blocks.IRON_ORE,
        Blocks.DEEPSLATE_IRON_ORE,
        Blocks.GOLD_ORE,
        Blocks.DEEPSLATE_GOLD_ORE,
        Blocks.DIAMOND_ORE,
        Blocks.DEEPSLATE_DIAMOND_ORE,
        Blocks.EMERALD_ORE,
        Blocks.DEEPSLATE_EMERALD_ORE,
        Blocks.LAPIS_ORE,
        Blocks.DEEPSLATE_LAPIS_ORE,
        Blocks.REDSTONE_ORE,
        Blocks.DEEPSLATE_REDSTONE_ORE,
        Blocks.COAL_ORE,
        Blocks.DEEPSLATE_COAL_ORE,
        Blocks.COPPER_ORE,
        Blocks.DEEPSLATE_COPPER_ORE,
        Blocks.NETHER_QUARTZ_ORE,
        Blocks.NETHER_GOLD_ORE,
        Blocks.ANCIENT_DEBRIS
    );
    
    @Inject(method = "renderBlock", at = @At("HEAD"), cancellable = true)
    private void onRenderBlock(BlockState state, BlockPos pos, BlockRenderView world, MatrixStack matrices,
                               VertexConsumerProvider vertexConsumers, boolean cull, CallbackInfo ci) {
        
        // XRay modu açık mı?
        if (XRay.isXRayEnabled()) {
            Block block = state.getBlock();
            
            // Eğer blok görünür bloklar listesinde değilse render etme
            if (!VISIBLE_BLOCKS.contains(block)) {
                ci.cancel();
            }
        }
    }
}
