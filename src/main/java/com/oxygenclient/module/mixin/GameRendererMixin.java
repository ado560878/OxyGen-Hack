package com.oxygenclient.mixin;

import com.oxygenclient.OxygenClient;
import com.oxygenclient.module.Module;
import com.oxygenclient.module.render.XRay;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public class GameRendererMixin {
    @Inject(method = "renderBlock", at = @At("HEAD"), cancellable = true)
    private void onRender(BlockState s, BlockPos p, BlockRenderView w, MatrixStack m, VertexConsumerProvider v, boolean c, CallbackInfo ci) {
        if (OxygenClient.moduleManager == null) return;
        Module x = OxygenClient.moduleManager.getModules().stream().filter(m2 -> m2.getName().equals("XRay")).findFirst().orElse(null);
        if (x != null && x.isEnabled() && !XRay.BLOCKS.contains(s.getBlock())) ci.cancel();
    }
}
