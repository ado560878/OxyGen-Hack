package com.oxygenclient.mixin;

import com.oxygenclient.module.render.XRay;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockRenderManager.class)
public class BlockRenderMixin {
    
    @Inject(method = "renderBlock", at = @At("HEAD"), cancellable = true)
    private void onRenderBlock(BlockState state, BlockPos pos, BlockRenderView world, MatrixStack matrices, 
                               VertexConsumerProvider vertexConsumers, boolean cull, CallbackInfo ci) {
        XRay module = XRay.getInstance();
        if (module != null && module.isEnabled()) {
            String name = state.getBlock().getName().getString().toLowerCase();
            if (!name.contains("ore") && !name.contains("quartz") && !name.contains("ancient_debris")) {
                ci.cancel();
            }
        }
    }
}
