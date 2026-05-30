package org.magicteam.relicsandfoes.world.level.levelgen.feature;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.Weight;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.util.random.WeightedRandomList;
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
        return config.templates.getRandom(random).flatMap(t -> manager.get(t.path).map(st -> {
            BoundingBox featureBB = new BoundingBox(
                    mx - 16,
                    level.getMinBuildHeight(),
                    mz - 16,
                    mx + 31,
                    level.getMaxBuildHeight(),
                    mz + 31
            );
            Rotation rotation = config.rotation ? Rotation.getRandom(random) : Rotation.NONE;
            BoundingBox tempBB = st.getBoundingBox(origin, rotation, t.rotationPivot, Mirror.NONE);
            return featureBB.isInside(tempBB.minX(), tempBB.minY(), tempBB.minZ()) &&
                    featureBB.isInside(tempBB.maxX(), tempBB.maxY(), tempBB.maxZ()) &&
                    st.placeInWorld(level, origin, origin, new StructurePlaceSettings()
                            .addProcessor(config.overwrite ? BlockIgnoreProcessor.STRUCTURE_BLOCK : BlockIgnoreProcessor.STRUCTURE_AND_AIR)
                            .setRotation(rotation)
                            .setRotationPivot(t.rotationPivot)
                            .setRandom(random)
                            .setBoundingBox(featureBB), random, Block.UPDATE_NONE);
        })).orElse(false);
    }

    public static WeightedRandomList<Template> average(String[] templates, BlockPos rotationPivot) {
        ImmutableList.Builder<Template> builder = ImmutableList.builder();
        for (String template : templates) {
            builder.add(new Template(RelicsAndFoes.asResource(template), rotationPivot, Weight.of(1)));
        }
        return WeightedRandomList.create(builder.build());
    }

    public static WeightedRandomList<Template> average(String templatePrefix, int count, BlockPos rotationPivot) {
        ImmutableList.Builder<Template> builder = ImmutableList.builder();
        for (int i = 0; i < count; i++) {
            builder.add(new Template(RelicsAndFoes.asResource(templatePrefix + i), rotationPivot, Weight.of(1)));
        }
        return WeightedRandomList.create(builder.build());
    }

    public record Config(
            WeightedRandomList<Template> templates,
            boolean overwrite,
            boolean applyWaterlogging,
            boolean rotation
    ) implements FeatureConfiguration {
        public static final Codec<Config> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                WeightedRandomList.codec(Template.CODEC).fieldOf("templates").forGetter(Config::templates),
                Codec.BOOL.lenientOptionalFieldOf("overwrite", false).forGetter(Config::overwrite),
                Codec.BOOL.lenientOptionalFieldOf("apply_waterlogging", true).forGetter(Config::applyWaterlogging),
                Codec.BOOL.lenientOptionalFieldOf("rotation", true).forGetter(Config::rotation)
        ).apply(instance, Config::new));

        public static Config of(String templatePrefix, int count, int pivotX, int pivotZ) {
            return new Config(TemplateStructureFeature.average(templatePrefix, count, new BlockPos(pivotX, 0, pivotZ)), false, true, true);
        }
    }

    public record Template(ResourceLocation path, BlockPos rotationPivot, Weight getWeight) implements WeightedEntry {
        public static final Codec<Template> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                ResourceLocation.CODEC.fieldOf("path").forGetter(Template::path),
                BlockPos.CODEC.lenientOptionalFieldOf("rotation_pivot", BlockPos.ZERO).forGetter(Template::rotationPivot),
                Weight.CODEC.lenientOptionalFieldOf("weight", Weight.of(1)).forGetter(Template::getWeight)
        ).apply(instance, Template::new));

        public static Template of(String name, int pivotX, int pivotZ) {
            return new Template(RelicsAndFoes.asResource(name), new BlockPos(pivotX, 0, pivotZ), Weight.of(1));
        }
    }
}
