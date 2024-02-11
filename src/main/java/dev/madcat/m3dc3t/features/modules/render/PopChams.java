/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfile
 *  net.minecraft.client.entity.EntityOtherPlayerMP
 *  net.minecraft.client.model.ModelBase
 *  net.minecraft.client.model.ModelPlayer
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.GlStateManager$DestFactor
 *  net.minecraft.client.renderer.GlStateManager$SourceFactor
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.network.play.server.SPacketEntityStatus
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.world.World
 *  net.minecraftforge.client.event.RenderWorldLastEvent
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  org.lwjgl.opengl.GL11
 */
package dev.madcat.m3dc3t.features.modules.render;

import com.mojang.authlib.GameProfile;
import dev.madcat.m3dc3t.event.events.PacketEvent;
import dev.madcat.m3dc3t.features.modules.Module;
import dev.madcat.m3dc3t.features.modules.client.HUD;
import dev.madcat.m3dc3t.features.modules.client.ClickGui;
import dev.madcat.m3dc3t.features.setting.Setting;
import dev.madcat.m3dc3t.util.ColorUtil;
import dev.madcat.m3dc3t.util.TotemPopChams;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class PopChams
        extends Module {
    public static Setting<Boolean> self;
    public static Setting<Boolean> elevator;
    public static Setting<Integer> rL;
    public static Setting<Integer> gL;
    public static Setting<Integer> bL;
    public static Setting<Integer> aL;
    public static Setting<Integer> rF;
    public static Setting<Integer> gF;
    public static Setting<Integer> bF;
    public static Setting<Integer> aF;
    public static Setting<Integer> fadestart;
    public static Setting<Float> fadetime;
    public static Setting<Boolean> fillrainbow;
    public static Setting<Boolean> outlinerainbow;
    public static Setting<Boolean> onlyOneEsp;
    public static Setting<ElevatorMode> elevatorMode;
    EntityOtherPlayerMP player;
    ModelPlayer playerModel;
    Long startTime;
    double alphaFill;
    double alphaLine;

    public PopChams() {
        super("PopChams", "Pop rendering.", Module.Category.RENDER, true, false, false);
        self = this.register(new Setting<Boolean>("Render Own Pops", true));
        elevator = this.register(new Setting<Boolean>("Travel", true));
        elevatorMode = this.register(new Setting<Object>("Elevator", (Object)ElevatorMode.UP, v -> elevator.getValue()));
        rL = this.register(new Setting<Integer>("Outline Red", 30, 0, 255));
        bL = this.register(new Setting<Integer>("Outline Green", 167, 0, 255));
        gL = this.register(new Setting<Integer>("Outline Blue", 255, 0, 255));
        aL = this.register(new Setting<Integer>("Outline Alpha", 255, 0, 255));
        outlinerainbow = this.register(new Setting<Boolean>("Outline Rainbow", true));
        rF = this.register(new Setting<Integer>("Fill Red", 30, 0, 255));
        bF = this.register(new Setting<Integer>("Fill Green", 167, 0, 255));
        gF = this.register(new Setting<Integer>("Fill Blue", 255, 0, 255));
        aF = this.register(new Setting<Integer>("Fill Alpha", 140, 0, 255));
        fillrainbow = this.register(new Setting<Boolean>("Fill Rainbow", true));
        fadestart = this.register(new Setting<Integer>("Fade Start", 0, 0, 255));
        fadetime = this.register(new Setting<Float>("Fade Time", Float.valueOf(0.5f), Float.valueOf(0.0f), Float.valueOf(2.0f)));
        onlyOneEsp = this.register(new Setting<Boolean>("Only Render One", true));
    }

    public static void renderEntity(EntityLivingBase entity, ModelBase modelBase, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, int scale) {
        if (mc.getRenderManager() == null) {
            return;
        }
        float partialTicks = mc.getRenderPartialTicks();
        double x = entity.posX - PopChams.mc.getRenderManager().viewerPosX;
        double y = entity.posY - PopChams.mc.getRenderManager().viewerPosY;
        double z = entity.posZ - PopChams.mc.getRenderManager().viewerPosZ;
        GlStateManager.pushMatrix();
        if (entity.isSneaking()) {
            y -= 0.125;
        }
        float interpolateRotation = PopChams.interpolateRotation(entity.prevRenderYawOffset, entity.renderYawOffset, partialTicks);
        float interpolateRotation2 = PopChams.interpolateRotation(entity.prevRotationYawHead, entity.rotationYawHead, partialTicks);
        float rotationInterp = interpolateRotation2 - interpolateRotation;
        float renderPitch = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks;
        PopChams.renderLivingAt(x, y, z);
        float f8 = PopChams.handleRotationFloat(entity, partialTicks);
        PopChams.prepareRotations(entity);
        float f9 = PopChams.prepareScale(entity, scale);
        GlStateManager.enableAlpha();
        modelBase.setLivingAnimations(entity, limbSwing, limbSwingAmount, partialTicks);
        modelBase.setRotationAngles(limbSwing, limbSwingAmount, f8, entity.rotationYaw, entity.rotationPitch, f9, (Entity)entity);
        modelBase.render((Entity)entity, limbSwing, limbSwingAmount, f8, entity.rotationYaw, entity.rotationPitch, f9);
        GlStateManager.popMatrix();
    }

    public static void prepareTranslate(EntityLivingBase entityIn, double x, double y, double z) {
        PopChams.renderLivingAt(x - PopChams.mc.getRenderManager().viewerPosX, y - PopChams.mc.getRenderManager().viewerPosY, z - PopChams.mc.getRenderManager().viewerPosZ);
    }

    public static void renderLivingAt(double x, double y, double z) {
        GlStateManager.translate((float)((float)x), (float)((float)y), (float)((float)z));
    }

    public static float prepareScale(EntityLivingBase entity, float scale) {
        GlStateManager.enableRescaleNormal();
        GlStateManager.scale((float)-1.0f, (float)-1.0f, (float)1.0f);
        double widthX = entity.getRenderBoundingBox().maxX - entity.getRenderBoundingBox().minX;
        double widthZ = entity.getRenderBoundingBox().maxZ - entity.getRenderBoundingBox().minZ;
        GlStateManager.scale((double)((double)scale + widthX), (double)(scale * entity.height), (double)((double)scale + widthZ));
        float f = 0.0625f;
        GlStateManager.translate((float)0.0f, (float)-1.501f, (float)0.0f);
        return 0.0625f;
    }

    public static void prepareRotations(EntityLivingBase entityLivingBase) {
        GlStateManager.rotate((float)(180.0f - entityLivingBase.rotationYaw), (float)0.0f, (float)1.0f, (float)0.0f);
    }

    public static float interpolateRotation(float prevYawOffset, float yawOffset, float partialTicks) {
        float f;
        for (f = yawOffset - prevYawOffset; f < -180.0f; f += 360.0f) {
        }
        while (f >= 180.0f) {
            f -= 360.0f;
        }
        return prevYawOffset + partialTicks * f;
    }

    public static Color newAlpha(Color color, int alpha) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }

    public static void glColor(Color color) {
        GL11.glColor4f((float)((float)color.getRed() / 255.0f), (float)((float)color.getGreen() / 255.0f), (float)((float)color.getBlue() / 255.0f), (float)((float)color.getAlpha() / 255.0f));
    }

    public static float handleRotationFloat(EntityLivingBase livingBase, float partialTicks) {
        return 0.0f;
    }

    public static void prepareGL() {
        GL11.glBlendFunc((int)770, (int)771);
        GlStateManager.tryBlendFuncSeparate((GlStateManager.SourceFactor)GlStateManager.SourceFactor.SRC_ALPHA, (GlStateManager.DestFactor)GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, (GlStateManager.SourceFactor)GlStateManager.SourceFactor.ONE, (GlStateManager.DestFactor)GlStateManager.DestFactor.ZERO);
        GlStateManager.glLineWidth((float)1.5f);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask((boolean)false);
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.disableLighting();
        GlStateManager.disableCull();
        GlStateManager.enableAlpha();
        GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f);
    }

    public static void releaseGL() {
        GlStateManager.enableCull();
        GlStateManager.depthMask((boolean)true);
        GlStateManager.enableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.enableDepth();
    }

    @SubscribeEvent
    public void onUpdate(PacketEvent.Receive event) {
        SPacketEntityStatus packet;
        if (event.getPacket() instanceof SPacketEntityStatus && (packet = (SPacketEntityStatus)event.getPacket()).getOpCode() == 35 && packet.getEntity((World)PopChams.mc.world) != null && (self.getValue().booleanValue() || packet.getEntity((World)PopChams.mc.world).getEntityId() != PopChams.mc.player.getEntityId())) {
            GameProfile profile = new GameProfile(PopChams.mc.player.getUniqueID(), "");
            this.player = new EntityOtherPlayerMP((World)PopChams.mc.world, profile);
            this.player.copyLocationAndAnglesFrom(packet.getEntity((World)PopChams.mc.world));
            this.playerModel = new ModelPlayer(0.0f, false);
            this.startTime = System.currentTimeMillis();
            this.playerModel.bipedHead.showModel = false;
            this.playerModel.bipedBody.showModel = false;
            this.playerModel.bipedLeftArmwear.showModel = false;
            this.playerModel.bipedLeftLegwear.showModel = false;
            this.playerModel.bipedRightArmwear.showModel = false;
            this.playerModel.bipedRightLegwear.showModel = false;
            this.alphaFill = aF.getValue().intValue();
            this.alphaLine = aL.getValue().intValue();
            if (!onlyOneEsp.getValue().booleanValue()) {
                TotemPopChams totemPopChams = new TotemPopChams(this.player, this.playerModel, this.startTime, this.alphaFill, this.alphaLine);
            }
        }
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        if (onlyOneEsp.getValue().booleanValue()) {
            if (this.player == null || PopChams.mc.world == null || PopChams.mc.player == null) {
                return;
            }
            if (elevator.getValue().booleanValue()) {
                if (elevatorMode.getValue() == ElevatorMode.UP) {
                    this.player.posY += (double)(0.05f * event.getPartialTicks());
                } else if (elevatorMode.getValue() == ElevatorMode.DOWN) {
                    this.player.posY -= (double)(0.05f * event.getPartialTicks());
                }
            }
            GL11.glLineWidth((float)1.0f);
            Color lineColorS = outlinerainbow.getValue() != false ? ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()) : new Color(rL.getValue(), bL.getValue(), gL.getValue(), aL.getValue());
            Color fillColorS = fillrainbow.getValue() != false ? ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()) : new Color(rF.getValue(), bF.getValue(), gF.getValue(), aF.getValue());
            int lineA = lineColorS.getAlpha();
            int fillA = fillColorS.getAlpha();
            long time = System.currentTimeMillis() - this.startTime - ((Number)fadestart.getValue()).longValue();
            if (System.currentTimeMillis() - this.startTime > ((Number)fadestart.getValue()).longValue()) {
                double normal = this.normalize(time, 0.0, ((Number)fadetime.getValue()).doubleValue());
                normal = MathHelper.clamp((double)normal, (double)0.0, (double)1.0);
                normal = -normal + 1.0;
                lineA *= (int)normal;
                fillA *= (int)normal;
            }
            Color lineColor = PopChams.newAlpha(lineColorS, lineA);
            Color fillColor = PopChams.newAlpha(fillColorS, fillA);
            if (this.player != null && this.playerModel != null) {
                PopChams.prepareGL();
                GL11.glPushAttrib((int)1048575);
                GL11.glEnable((int)2881);
                GL11.glEnable((int)2848);
                if (this.alphaFill > 1.0) {
                    this.alphaFill -= (double)fadetime.getValue().floatValue();
                }
                Color fillFinal = new Color(fillColor.getRed(), fillColor.getGreen(), fillColor.getBlue(), (int)this.alphaFill);
                if (this.alphaLine > 1.0) {
                    this.alphaLine -= (double)fadetime.getValue().floatValue();
                }
                Color outlineFinal = new Color(lineColor.getRed(), lineColor.getGreen(), lineColor.getBlue(), (int)this.alphaLine);
                PopChams.glColor(fillFinal);
                GL11.glPolygonMode((int)1032, (int)6914);
                PopChams.renderEntity((EntityLivingBase)this.player, (ModelBase)this.playerModel, this.player.limbSwing, this.player.limbSwingAmount, this.player.ticksExisted, this.player.rotationYawHead, this.player.rotationPitch, 1);
                PopChams.glColor(outlineFinal);
                GL11.glPolygonMode((int)1032, (int)6913);
                PopChams.renderEntity((EntityLivingBase)this.player, (ModelBase)this.playerModel, this.player.limbSwing, this.player.limbSwingAmount, this.player.ticksExisted, this.player.rotationYawHead, this.player.rotationPitch, 1);
                GL11.glPolygonMode((int)1032, (int)6914);
                GL11.glPopAttrib();
                PopChams.releaseGL();
            }
        }
    }

    double normalize(double value, double min, double max) {
        return (value - min) / (max - min);
    }

    public static enum ElevatorMode {
        UP,
        DOWN;

    }
}

