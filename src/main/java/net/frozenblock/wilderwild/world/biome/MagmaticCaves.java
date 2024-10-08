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

package net.frozenblock.wilderwild.world.biome;

import com.mojang.datafixers.util.Pair;
import java.util.function.Consumer;
import net.frozenblock.lib.worldgen.biome.api.FrozenBiome;
import net.frozenblock.lib.worldgen.biome.api.parameters.Continentalness;
import net.frozenblock.lib.worldgen.biome.api.parameters.Erosion;
import net.frozenblock.lib.worldgen.biome.api.parameters.Humidity;
import net.frozenblock.lib.worldgen.biome.api.parameters.Temperature;
import net.frozenblock.lib.worldgen.biome.api.parameters.Weirdness;
import net.frozenblock.wilderwild.WilderConstants;
import net.frozenblock.wilderwild.config.WorldgenConfig;
import net.frozenblock.wilderwild.registry.RegisterEntities;
import net.frozenblock.wilderwild.registry.RegisterSounds;
import net.frozenblock.wilderwild.world.WilderSharedWorldgen;
import net.frozenblock.wilderwild.world.feature.placed.WilderCavePlaced;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.data.worldgen.biome.OverworldBiomes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.Musics;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.AmbientAdditionsSettings;
import net.minecraft.world.level.biome.AmbientMoodSettings;
import net.minecraft.world.level.biome.AmbientParticleSettings;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class MagmaticCaves extends FrozenBiome {
	public static final Climate.Parameter TEMPERATURE = Climate.Parameter.span(Temperature.WARM, Temperature.HOT);
	public static final Climate.Parameter HUMIDITY = Climate.Parameter.span(Humidity.ARID, Humidity.NEUTRAL);
	public static final Climate.Parameter CONTINENTALNESS = Climate.Parameter.span(Continentalness.COAST, Continentalness.FAR_INLAND);
	public static final Climate.Parameter EROSION = Climate.Parameter.span(Erosion.EROSION_2, Erosion.EROSION_5);
	public static final Climate.Parameter WEIRDNESS = Weirdness.FULL_RANGE;
	public static final float OFFSET = 0.000F;
	public static final float TEMP = 2.0F;
	public static final float DOWNFALL = 0.4F;
	public static final int WATER_COLOR = 4566514;
	public static final int WATER_FOG_COLOR = 267827;
	public static final int FOG_COLOR = WilderSharedWorldgen.STOCK_FOG_COLOR;
	public static final int SKY_COLOR = OverworldBiomes.calculateSkyColor(TEMP);
	public static final MagmaticCaves INSTANCE = new MagmaticCaves();

	@Override
	public String modID() {
		return WilderConstants.MOD_ID;
	}

	@Override
	public String biomeID() {
		return "magmatic_caves";
	}

	@Override
	public float temperature() {
		return TEMP;
	}

	@Override
	public float downfall() {
		return DOWNFALL;
	}

	@Override
	public boolean hasPrecipitation() {
		return true;
	}

	@Override
	public int skyColor() {
		return SKY_COLOR;
	}

	@Override
	public int fogColor() {
		return FOG_COLOR;
	}

	@Override
	public int waterColor() {
		return WATER_COLOR;
	}

	@Override
	public int waterFogColor() {
		return WATER_FOG_COLOR;
	}

	@Override
	public @Nullable Integer foliageColorOverride() {
		return null;
	}

	@Override
	public @Nullable Integer grassColorOverride() {
		return null;
	}

	@Override
	public @Nullable AmbientParticleSettings ambientParticleSettings() {
		return null;
	}

	@Override
	public @Nullable Holder<SoundEvent> ambientLoopSound() {
		return null;
	}

	@Override
	public @NotNull AmbientMoodSettings ambientMoodSettings() {
		return AmbientMoodSettings.LEGACY_CAVE_SETTINGS;
	}

	@Override
	public @Nullable AmbientAdditionsSettings ambientAdditionsSound() {
		return null;
	}

	@Override
	public @NotNull Music backgroundMusic() {
		return Musics.createGameMusic(RegisterSounds.MUSIC_OVERWORLD_MAGMATIC_CAVES);
	}

	@Override
	public void addFeatures(@NotNull BiomeGenerationSettings.Builder features) {
		BiomeDefaultFeatures.addFossilDecoration(features);
		BiomeDefaultFeatures.addDefaultCrystalFormations(features);
		BiomeDefaultFeatures.addDefaultMonsterRoom(features);
		features.addFeature(GenerationStep.Decoration.LAKES, WilderCavePlaced.MAGMA_LAVA_POOL.getKey());
		features.addFeature(GenerationStep.Decoration.LAKES, WilderCavePlaced.LAVA_LAKE_EXTRA.getKey());
		features.addFeature(GenerationStep.Decoration.FLUID_SPRINGS, WilderCavePlaced.LAVA_SPRING_EXTRA.getKey());
		BiomeDefaultFeatures.addDefaultUndergroundVariety(features);
		BiomeDefaultFeatures.addSurfaceFreezing(features);
		BiomeDefaultFeatures.addPlainGrass(features);
		BiomeDefaultFeatures.addDefaultOres(features, false);
		BiomeDefaultFeatures.addDefaultSoftDisks(features);
		BiomeDefaultFeatures.addPlainVegetation(features);
		BiomeDefaultFeatures.addDefaultMushrooms(features);
		BiomeDefaultFeatures.addDefaultExtraVegetation(features);
		BiomeDefaultFeatures.addDefaultCarversAndLakes(features);
		features.addFeature(GenerationStep.Decoration.UNDERGROUND_STRUCTURES, WilderCavePlaced.FOSSIL_LAVA.getKey());
		features.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, WilderCavePlaced.UPSIDE_DOWN_MAGMA.getKey());
		features.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, WilderCavePlaced.MAGMA_DISK.getKey());
		features.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, WilderCavePlaced.MAGMA_PILE.getKey());
		features.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, WilderCavePlaced.OBSIDIAN_DISK.getKey());
		features.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, WilderCavePlaced.BASALT_PILE.getKey());
		features.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, WilderCavePlaced.BASALT_SPIKE.getKey());
		features.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, WilderCavePlaced.DOWNWARDS_BASALT_COLUMN.getKey());
		features.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, WilderCavePlaced.MAGMA_PATH.getKey());
		features.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, WilderCavePlaced.FIRE_PATCH_MAGMA.getKey());
		features.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, WilderCavePlaced.GEYSER_PILE.getKey());
		features.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, WilderCavePlaced.GEYSER_UP.getKey());
		features.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, WilderCavePlaced.GEYSER_DOWN.getKey());
		features.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, WilderCavePlaced.DOWNWARDS_GEYSER_COLUMN.getKey());
	}

	@Override
	public void addSpawns(MobSpawnSettings.Builder spawns) {
		BiomeDefaultFeatures.commonSpawns(spawns);
		spawns.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(RegisterEntities.SCORCHED, 385, 4, 4));
	}

	@Override
	public void injectToOverworld(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> parameters) {
		if (WorldgenConfig.get().biomeGeneration.generateMagmaticCaves) {
			this.addBottomBiome(
				parameters,
				TEMPERATURE,
				HUMIDITY,
				CONTINENTALNESS,
				EROSION,
				WEIRDNESS,
				OFFSET
			);
		}
	}

}
