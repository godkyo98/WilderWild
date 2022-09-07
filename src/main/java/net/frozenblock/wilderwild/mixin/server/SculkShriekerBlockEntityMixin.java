package net.frozenblock.wilderwild.mixin.server;

import net.frozenblock.wilderwild.WilderWild;
import net.frozenblock.wilderwild.misc.server.EasyPacket;
import net.frozenblock.wilderwild.registry.RegisterProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.SculkShriekerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEventListener;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SculkShriekerBlockEntity.class)
public class SculkShriekerBlockEntityMixin {

    public int bubbles;

    @Inject(at = @At("HEAD"), method = "canRespond", cancellable = true)
    private void canRespond(ServerLevel world, CallbackInfoReturnable<Boolean> info) {
        SculkShriekerBlockEntity entity = SculkShriekerBlockEntity.class.cast(this);
        BlockState blockState = entity.getBlockState();
        if (blockState.getValue(RegisterProperties.SOULS_TAKEN) == 2) {
            WilderWild.log(Blocks.SCULK_SHRIEKER, entity.getBlockPos(), "All Souls Have Already Been Taken, Cannot Warn", WilderWild.DEV_LOGGING);
            info.setReturnValue(false);
            info.cancel();
        }
    }

    @Inject(at = @At("HEAD"), method = "shouldListen", cancellable = true)
    public void shouldListen(ServerLevel world, GameEventListener listener, BlockPos pos, GameEvent event, GameEvent.Context emitter, CallbackInfoReturnable<Boolean> info) {
        SculkShriekerBlockEntity entity = SculkShriekerBlockEntity.class.cast(this);
        if (entity.getBlockState().getValue(RegisterProperties.SOULS_TAKEN) == 2) {
            info.setReturnValue(false);
            info.cancel();
        }
    }

    @Inject(at = @At("HEAD"), method = "shriek", cancellable = true)
    public void shriek(ServerLevel world, @Nullable Entity entity, CallbackInfo info) {
        SculkShriekerBlockEntity shrieker = SculkShriekerBlockEntity.class.cast(this);
        if (shrieker.getBlockState().getValue(RegisterProperties.SOULS_TAKEN) == 2) {
            info.cancel();
        } else {
            if (shrieker.getBlockState().getValue(BlockStateProperties.WATERLOGGED)) {//TODO: fix this. for some reason this only works when stepping on the shrieker.
                bubbles(world, Vec3.atCenterOf(shrieker.getBlockPos()), shrieker);
                this.bubbles = 60;
            }
        }
    }

    public void bubbles(ServerLevel world, Vec3 pos, SculkShriekerBlockEntity shrieker) {
        if (this.bubbles > 0 && world != null) {
            --this.bubbles;
            EasyPacket.EasyFloatingSculkBubblePacket.createParticle(world, pos, Math.random() > 0.7 ? 1 : 0, 20 + WilderWild.random().nextInt(80), 0.075, world.random.nextIntBetweenInclusive(8, 14));
        }
    }

}
