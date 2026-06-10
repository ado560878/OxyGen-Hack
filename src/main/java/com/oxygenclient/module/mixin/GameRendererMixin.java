package com.oxygenclient.mixin;

import com.oxygenclient.OxygenClient;
import com.oxygenclient.module.Module;
import com.oxygenclient.module.render.XRayBypass;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public class GameRendererMixin {

    @Inject(method = "renderBlock", at = @At("HEAD"), cancellable = true)
    private void onRenderBlock(BlockState state, BlockPos pos, 
                                net.minecraft.world.BlockRenderView world,
                                MatrixStack matrices,
                                VertexConsumerProvider vertexConsumers,
                                boolean cull,
                                net.minecraft.util.math.random.Random random,
                                LightmapTextureManager lightmap,
                                int overlay,
                                CallbackInfo ci) {
        
        Module xray = OxygenClient.moduleManager.getModule("XRay");
        if (xray != null && xray.isEnabled()) {
            if (!XRayBypass.shouldRender(state.getBlock())) {
                ci.cancel();
            }
        }
    }
}
