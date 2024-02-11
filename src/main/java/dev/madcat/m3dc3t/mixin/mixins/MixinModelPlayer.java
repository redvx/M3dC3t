package dev.madcat.m3dc3t.mixin.mixins;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ModelPlayer.class})
public class MixinModelPlayer {
    @Inject(method={"setRotationAngles"}, at={@At(value="RETURN")})
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn, CallbackInfo callbackInfo) {
        if (Minecraft.getMinecraft().world == null) return;
        if (Minecraft.getMinecraft().player == null) return;
        if (!(entityIn instanceof EntityPlayer)) return;
    }
}
