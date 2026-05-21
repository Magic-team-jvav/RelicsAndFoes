package org.magicteam.relicsandfoes.world.level.levelgen.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import org.magicteam.relicsandfoes.RelicsAndFoes;

public class TemplateStructureFeature extends Feature<TemplateStructureFeature.Config> {
    public TemplateStructureFeature(Codec<Config> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<Config> context) {
        BlockPos origin = context.origin();
        int cx = SectionPos.blockToSectionCoord(origin.getX());
        int cz = SectionPos.blockToSectionCoord(origin.getZ());
        int mx = SectionPos.sectionToBlockCoord(cx);
        int mz = SectionPos.sectionToBlockCoord(cz);

        WorldGenLevel level = context.level();
        StructureTemplateManager manager = level.getLevel().getServer().getStructureManager();
        RandomSource random = context.random();
        Config config = context.config();
        return config.templates.getRandomValue(random).flatMap(manager::get).map(template -> {
            BoundingBox featureBB = new BoundingBox(
                    mx - 16,
                    level.getMinBuildHeight(),
                    mz - 16,
                    mx + 31,
                    level.getMaxBuildHeight(),
                    mz + 31
            );
            Rotation rotation = config.rotation ? Rotation.getRandom(random) : Rotation.NONE;
            BlockPos rotationPivot = config.rotationPivot;
            BoundingBox tempBB = template.getBoundingBox(origin, rotation, rotationPivot, Mirror.NONE);
            return featureBB.isInside(tempBB.minX(), tempBB.minY(), tempBB.minZ()) &&
                    featureBB.isInside(tempBB.maxX(), tempBB.maxY(), tempBB.maxZ()) &&
                    template.placeInWorld(level, origin, origin, new StructurePlaceSettings()
                            .addProcessor(config.overwrite ? BlockIgnoreProcessor.STRUCTURE_BLOCK : BlockIgnoreProcessor.STRUCTURE_AND_AIR)
                            .setRotation(rotation)
                            .setRotationPivot(rotationPivot)
                            .setRandom(random)
                            .setBoundingBox(featureBB), random, Block.UPDATE_NONE);
        }).orElse(false);
    }

    public static SimpleWeightedRandomList<ResourceLocation> average(String... templates) {
        SimpleWeightedRandomList.Builder<ResourceLocation> builder = SimpleWeightedRandomList.builder();
        for (String template : templates) {
            builder.add(RelicsAndFoes.asResource(template));
        }
        return builder.build();
    }

    public static SimpleWeightedRandomList<ResourceLocation> average(String templatePrefix, int count) {
        SimpleWeightedRandomList.Builder<ResourceLocation> builder = SimpleWeightedRandomList.builder();
        for (int i = 0; i < count; i++) {
            builder.add(RelicsAndFoes.asResource(templatePrefix + i));
        }
        return builder.build();
    }

    public record Config(
            SimpleWeightedRandomList<ResourceLocation> templates,
            boolean overwrite,
            boolean applyWaterlogging,
            boolean rotation,
            BlockPos rotationPivot
    ) implements FeatureConfiguration {
        public static final Codec<Config> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                SimpleWeightedRandomList.wrappedCodec(ResourceLocation.CODEC).fieldOf("templates").forGetter(Config::templates),
                Codec.BOOL.lenientOptionalFieldOf("overwrite", false).forGetter(Config::overwrite),
                Codec.BOOL.lenientOptionalFieldOf("apply_waterlogging", true).forGetter(Config::applyWaterlogging),
                Codec.BOOL.lenientOptionalFieldOf("rotation", true).forGetter(Config::rotation),
                BlockPos.CODEC.lenientOptionalFieldOf("rotation_pivot", BlockPos.ZERO).forGetter(Config::rotationPivot)
        ).apply(instance, Config::new));

        public static Config of(String templatePrefix, int count, int pivotX, int pivotZ) {
            return new Config(TemplateStructureFeature.average(templatePrefix, count), false, true, true, new BlockPos(pivotX, 0, pivotZ));
        }
    }
}
