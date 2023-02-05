/*
 * Copyright 2022-2023 FrozenBlock
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

package net.frozenblock.wilderwild.mixin.server.general;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BigDripleafStemBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockBehaviour.class)
public class BlockBehaviourMixin {

	@Inject(at = @At("HEAD"), method = "neighborChanged", cancellable = true)
	public void neighborChanged(@NotNull BlockState state, Level level, @NotNull BlockPos pos, @NotNull Block block, @NotNull BlockPos fromPos, boolean isMoving, CallbackInfo info) {
		if (BlockBehaviour.class.cast(this) instanceof BigDripleafStemBlock bigDripleafStemBlock) {
			if (!level.isClientSide) {
				BlockState fromState = level.getBlockState(fromPos);
				if (fromState.is(Blocks.BIG_DRIPLEAF_STEM)) {
					if (pos.getY() > fromPos.getY()) {
						level.setBlock(pos, state.setValue(BlockStateProperties.POWERED, fromState.getValue(BlockStateProperties.POWERED)), 3);
					}
				} else {
					boolean powered = state.getValue(BlockStateProperties.POWERED);
					boolean power = level.hasNeighborSignal(pos);
					if (powered != power) {
						if (powered) {
							level.setBlock(pos, state.setValue(BlockStateProperties.POWERED, false), 3);
						} else {
							level.setBlock(pos, state.setValue(BlockStateProperties.POWERED, true), 3);
						}
					}
				}
			}
			info.cancel();
		}
	}

}
