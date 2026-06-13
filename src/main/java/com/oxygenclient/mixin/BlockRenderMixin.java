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
    private void onRender(BlockState s, BlockPos p, BlockRenderView w, MatrixStack m, 
                           VertexConsumerProvider v, boolean c, 
                           net.minecraft.util.math.random.Random r, CallbackInfo ci) {
        if (XRay.enabled && !XRay.BLOCKS.contains(s.getBlock())) {
            ci.cancel();
        }
    }
}
