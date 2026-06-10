package com.oxygenclient.mixin;

import com.oxygenclient.OxygenClient;
import com.oxygenclient.module.Module;
import com.oxygenclient.module.render.XRay;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public class GameRendererMixin {
    @Inject(method = "renderBlock", at = @At("HEAD"), cancellable = true)
    private void onRenderBlock(BlockState state, BlockPos pos, BlockRenderView world,
                                net.minecraft.client.util.math.MatrixStack matrices,
                                net.minecraft.client.render.VertexConsumerProvider vcp,
                                boolean cull,
                                net.minecraft.util.math.random.Random random,
                                net.minecraft.client.render.LightmapTextureManager lightmap,
                                int overlay, CallbackInfo ci) {
        Module xray = OxygenClient.moduleManager.getModules().stream()
            .filter(m -> m.getName().equals("XRay")).findFirst().orElse(null);
        if (xray != null && xray.isEnabled() && !XRay.BLOCKS.contains(state.getBlock())) {
            ci.cancel();
        }
    }
}
