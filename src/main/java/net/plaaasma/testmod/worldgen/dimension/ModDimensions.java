package net.plaaasma.testmod.worldgen.dimension;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.plaaasma.testmod.TestMod;
import net.plaaasma.testmod.worldgen.biome.ModBiomes;

import java.util.List;
import java.util.OptionalLong;

public class ModDimensions {
    public static final ResourceKey<LevelStem> TESTDIM_KEY = ResourceKey.create(Registries.LEVEL_STEM,
            new ResourceLocation(TestMod.MODID, "testdim"));
    public static final ResourceKey<Level> TESTDIM_LEVEL_KEY = ResourceKey.create(Registries.DIMENSION,
            new ResourceLocation(TestMod.MODID, "testdim"));
    public static final ResourceKey<DimensionType> TEST_DIM_TYPE = ResourceKey.create(Registries.DIMENSION_TYPE,
            new ResourceLocation(TestMod.MODID, "testdim_type"));

    public static void bootstrapType(BootstapContext<DimensionType> context) {
        context.register(TEST_DIM_TYPE, new DimensionType(
                OptionalLong.of(12000), // fixedTime
                false, // hasSkylight
                false, // hasCeiling
                false, // ultraWarm
                false, // natural
                1.0, // coordinateScale
                true, // bedWorks
                false, // respawnAnchorWorks
                -256, // minY
                256, // height
                256, // logicalHeight
                BlockTags.INFINIBURN_OVERWORLD, // infiniburn
                BuiltinDimensionTypes.OVERWORLD_EFFECTS, // effectsLocation
                1.0f, // ambientLight
                new DimensionType.MonsterSettings(false, false, ConstantInt.of(0), -1)));
    }

    public static void bootstrapStem(BootstapContext<LevelStem> context) {
        HolderGetter<Biome> biomeRegistry = context.lookup(Registries.BIOME);
        HolderGetter<DimensionType> dimTypes = context.lookup(Registries.DIMENSION_TYPE);
        HolderGetter<NoiseGeneratorSettings> noiseGenSettings = context.lookup(Registries.NOISE_SETTINGS);

        NoiseBasedChunkGenerator wrappedChunkGenerator = new NoiseBasedChunkGenerator(
                new FixedBiomeSource(biomeRegistry.getOrThrow(ModBiomes.TEST_BIOME)),
                noiseGenSettings.getOrThrow(NoiseGeneratorSettings.AMPLIFIED));

//        NoiseBasedChunkGenerator noiseBasedChunkGenerator = new NoiseBasedChunkGenerator(
//                MultiNoiseBiomeSource.createFromList(
//                        new Climate.ParameterList<>(List.of(Pair.of(
//                                Climate.parameters(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F), biomeRegistry.getOrThrow(ModBiomes.TEST_BIOME)),
//                                Pair.of(
//                                        Climate.parameters(0.1F, 0.2F, 0.0F, 0.2F, 0.0F, 0.0F, 0.0F), biomeRegistry.getOrThrow(Biomes.BIRCH_FOREST)),
//                                Pair.of(
//                                        Climate.parameters(0.3F, 0.6F, 0.1F, 0.1F, 0.0F, 0.0F, 0.0F), biomeRegistry.getOrThrow(Biomes.OCEAN)),
//                                Pair.of(
//                                        Climate.parameters(0.4F, 0.3F, 0.2F, 0.1F, 0.0F, 0.0F, 0.0F), biomeRegistry.getOrThrow(Biomes.DARK_FOREST))
//
//                        ))),
//                noiseGenSettings.getOrThrow(NoiseGeneratorSettings.AMPLIFIED));

        LevelStem stem = new LevelStem(dimTypes.getOrThrow(ModDimensions.TEST_DIM_TYPE), wrappedChunkGenerator);

        context.register(TESTDIM_KEY, stem);
    }
}
