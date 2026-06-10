package com.oxygenclient.mixin;

import com.oxygenclient.OxygenClient;
import com.oxygenclient.bypass.PacketSpoofer;
import com.oxygenclient.bypass.RotationSpoofer;
import com.oxygenclient.module.Module;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.PacketCallbacks;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientConnection.class)
public class NetworkHandlerMixin {

    @Inject(method = "send(Lnet/minecraft/network/packet/Packet;Lnet/minecraft/network/PacketCallbacks;)V", 
            at = @At("HEAD"), cancellable = true)
    private void onSendPacket(Packet<?> packet, PacketCallbacks callbacks, CallbackInfo ci) {
        if (!isClient()) return;

        // Move packet'lerini değiştir
        if (packet instanceof PlayerMoveC2SPacket movePacket) {
            Module killAura = OxygenClient.moduleManager.getModule("KillAura");
            Module silentAim = OxygenClient.moduleManager.getModule("SilentAim");

            if (killAura != null && killAura.isEnabled() || 
                silentAim != null && silentAim.isEnabled()) {
                
                // Server rotasyonunu kullan
                if (movePacket.changesLook()) {
                    packet = new PlayerMoveC2SPacket.Full(
                        movePacket.getX(0),
                        movePacket.getY(0),
                        movePacket.getZ(0),
                        RotationSpoofer.getServerYaw(),
                        RotationSpoofer.getServerPitch(),
                        movePacket.isOnGround()
                    );
                    ci.cancel();
                    sendModifiedPacket(packet, callbacks);
                }
            }

            // Packet spoofing
            if (PacketSpoofer.shouldDelayPacket(packet)) {
                ci.cancel();
                // Packet'i gecikmeli gönder (FakeLag gibi)
            }
        }
    }

    private void sendModifiedPacket(Packet<?> packet, PacketCallbacks callbacks) {
        ClientConnection conn = (ClientConnection) (Object) this;
        conn.send(packet, callbacks);
    }

    private boolean isClient() {
        try {
            return net.minecraft.client.MinecraftClient.getInstance() != null;
        } catch (Exception e) {
            return false;
        }
    }
}
