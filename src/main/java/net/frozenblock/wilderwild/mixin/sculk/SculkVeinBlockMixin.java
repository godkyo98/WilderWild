/*
 * Copyright 2023-2024 FrozenBlock
 * This file is part of Wilder Wild.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, see <https://www.gnu.org/licenses/>.
 */

package net.frozenblock.wilderwild.mixin.sculk;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.frozenblock.wilderwild.block.impl.SlabWallStairSculkBehavior;
import net.frozenblock.wilderwild.registry.WWBlocks;
import net.frozenblock.wilderwild.tag.WWBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.MultifaceBlock;
import net.minecraft.world.level.block.MultifaceSpreader;
import net.minecraft.world.level.block.SculkBehaviour;
import net.minecraft.world.level.block.SculkSpreader;
import net.minecraft.world.level.block.SculkVeinBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SculkVeinBlock.class)
public abstract class SculkVeinBlockMixin extends MultifaceBlock implements SculkBehaviour, SimpleWaterloggedBlock {

	@Final
	@Shadow
	private MultifaceSpreader veinSpreader;

	private SculkVeinBlockMixin(Properties properties) {
		super(properties);
	}

	@Inject(
		method = "attemptPlaceSculk",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/core/Direction;getOpposite()Lnet/minecraft/core/Direction;",
			opcode = 0,
			shift = At.Shift.AFTER
		)
	)
	private void wilderWild$attemptPlaceSculk(
		SculkSpreader sculkBehavior, LevelAccessor level, BlockPos pos, RandomSource random, CallbackInfoReturnable<Boolean> info,
		@Local(ordinal = 1) BlockPos blockPos, @Local(ordinal = 1) BlockState blockState2, @Local(ordinal = 2) BlockState blockState3
	) {
		boolean canReturn = false;
		if (blockState2.is(WWBlockTags.SCULK_STAIR_REPLACEABLE_WORLDGEN) || blockState2.is(WWBlockTags.SCULK_STAIR_REPLACEABLE)) {
			blockState3 = WWBlocks.SCULK_STAIRS.withPropertiesOf(blockState2);
			canReturn = true;
		} else if (blockState2.is(WWBlockTags.SCULK_WALL_REPLACEABLE_WORLDGEN) || blockState2.is(WWBlockTags.SCULK_WALL_REPLACEABLE)) {
			blockState3 = WWBlocks.SCULK_WALL.withPropertiesOf(blockState2);
			canReturn = true;
		} else if (blockState2.is(WWBlockTags.SCULK_SLAB_REPLACEABLE_WORLDGEN) || blockState2.is(WWBlockTags.SCULK_SLAB_REPLACEABLE)) {
			blockState3 = WWBlocks.SCULK_SLAB.withPropertiesOf(blockState2);
			canReturn = true;
		}

		if (canReturn) {
			level.setBlock(blockPos, blockState3, 3);
			Block.pushEntitiesUp(blockState2, blockState3, level, blockPos);
			SlabWallStairSculkBehavior.clearSculkVeins(level, blockPos);
			this.veinSpreader.spreadAll(blockState3, level, blockPos, sculkBehavior.isWorldGeneration());
		}
	}

	@WrapOperation(
		method = "onDischarged",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z",
			ordinal = 1
		)
	)
	private boolean wilderWild$onDischarged(BlockState state, Block block, Operation<Boolean> operation) {
		return state.is(WWBlocks.SCULK_SLAB) || state.is(WWBlocks.SCULK_STAIRS) || state.is(WWBlocks.SCULK_WALL) || operation.call(state, block);
	}

}
