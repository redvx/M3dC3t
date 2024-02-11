/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfile
 *  net.minecraft.client.entity.EntityOtherPlayerMP
 *  net.minecraft.entity.Entity
 *  net.minecraft.world.GameType
 *  net.minecraft.world.World
 */
package dev.madcat.m3dc3t.features.modules.misc;

import com.mojang.authlib.GameProfile;
import dev.madcat.m3dc3t.features.modules.Module;
import dev.madcat.m3dc3t.features.modules.client.HUD;
import dev.madcat.m3dc3t.features.setting.Setting;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.world.GameType;

import java.util.UUID;

public class FakePlayer
extends Module {
    private final Setting<Integer> setHealth = this.register(new Setting<Integer>("SetHealth", 20, 1, 20));
    public Setting<String> fName = this.register(new Setting<String>("Name", "HuShuYu", "Fake player's name"));
    private EntityOtherPlayerMP clonedPlayer;

    public FakePlayer() {
        super("FakePlayer", "Spawns a FakePlayer for testing.", Category.MISC, true, false, false);
    }

    @Override
    public void onEnable() {
        if (FakePlayer.mc.player != null && !FakePlayer.mc.player.isDead) {
            this.clonedPlayer = new EntityOtherPlayerMP(FakePlayer.mc.world, new GameProfile(UUID.fromString("a3ca166d-c5f1-3d5a-baac-b18a5b38d4cd"), fName.getValue()));
            this.clonedPlayer.copyLocationAndAnglesFrom(FakePlayer.mc.player);
            this.clonedPlayer.rotationYawHead = FakePlayer.mc.player.rotationYawHead;
            this.clonedPlayer.rotationYaw = FakePlayer.mc.player.rotationYaw;
            this.clonedPlayer.rotationPitch = FakePlayer.mc.player.rotationPitch;
            this.clonedPlayer.inventory.copyInventory(FakePlayer.mc.player.inventory);
            this.clonedPlayer.setGameType(GameType.SURVIVAL);
            this.clonedPlayer.setHealth((float)this.setHealth.getValue().intValue());
            FakePlayer.mc.world.addEntityToWorld(-404, (Entity)this.clonedPlayer);
            this.clonedPlayer.onLivingUpdate();
            return;
        }
        this.disable();
    }

    @Override
    public void onDisable() {
        if (FakePlayer.mc.world == null) return;
        FakePlayer.mc.world.removeEntityFromWorld(-404);
    }

    public String getDisplayInfo() {
        if (!HUD.getInstance().moduleInfo.getValue()) return null;
        return fName.getValue();
    }
}

