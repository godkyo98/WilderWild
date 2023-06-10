package net.frozenblock.wilderwild.mixin.client.wind;

import com.mojang.blaze3d.vertex.PoseStack;
import net.frozenblock.lib.wind.api.ClientWindManager;
import net.frozenblock.wilderwild.misc.WilderSharedConstants;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.util.Mth;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LevelRenderer.class)
public class CloudRendererMixin {

	@Unique
	private float wilderWild$cloudHeight;

	@ModifyVariable(method = "renderClouds", at = @At(value = "STORE"), ordinal = 1)
	private float getCloudHeight(float original) {
		this.wilderWild$cloudHeight = original;
		return original;
	}

	@ModifyVariable(method = "renderClouds", at = @At(value = "STORE"), ordinal = 5)
	private double modifyX(double original, PoseStack poseStack, Matrix4f projectionMatrix, float partialTick, double camX) {
		return WilderSharedConstants.config().cloudMovement() && ClientWindManager.shouldUseWind()
			? Mth.clamp((camX / 12.0) - ClientWindManager.getCloudX(partialTick), Double.MIN_VALUE, Double.MAX_VALUE)
			: original;
	}

	@ModifyVariable(method = "renderClouds", at = @At("STORE"), ordinal = 6)
	private double modifyY(double original, PoseStack poseStack, Matrix4f projectionMatrix, float partialTick, double camX, double camY) {
		return WilderSharedConstants.config().cloudMovement() && ClientWindManager.shouldUseWind()
			? this.wilderWild$cloudHeight - camY + 0.33D + Mth.clamp(ClientWindManager.getCloudY(partialTick), -10, 10)
			: original;
	}

	@ModifyVariable(method = "renderClouds", at = @At("STORE"), ordinal = 7)
	private double modifyZ(double original, PoseStack poseStack, Matrix4f projectionMatrix, float partialTick, double camX, double camY, double camZ) {
		return WilderSharedConstants.config().cloudMovement() && ClientWindManager.shouldUseWind()
			? Mth.clamp((camZ / 12.0D + 0.33D) - ClientWindManager.getCloudZ(partialTick), Double.MIN_VALUE, Double.MAX_VALUE)
			: original;
	}
}
