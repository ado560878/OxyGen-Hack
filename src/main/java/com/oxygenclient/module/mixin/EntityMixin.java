package com.oxygenclient.mixin;

import com.oxygenclient.OxygenClient;
import com.oxygenclient.module.Module;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityMixin {
    @Inject(method = "isPushable", at = @At("RETURN"), cancellable = true)
    private void onIsPushable(CallbackInfoReturnable<Boolean> cir) {
        Module v = OxygenClient.moduleManager.getModules().stream()
            .filter(m -> m.getName().equals("Velocity")).findFirst().orElse(null);
        if (v != null && v.isEnabled()) cir.setReturnValue(false);
    }
}
