package org.magicteam.relicsandfoes.world.level.levelgen.structure;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectIntImmutablePair;
import it.unimi.dsi.fastutil.objects.ObjectIntPair;
import net.minecraft.core.BlockPos;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.Mth;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import org.magicteam.relicsandfoes.init.RAFDimensions;

import java.util.List;
import java.util.Optional;

public class OnGroundStructure extends Structure {
    public static final Codec<ObjectIntPair<String>> TYPE_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("prefix").forGetter(ObjectIntPair::left),
            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("count").forGetter(ObjectIntPair::rightInt)
    ).apply(instance, ObjectIntImmutablePair::new));
    public static final MapCodec<OnGroundStructure> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            settingsCodec(instance),
            Codec.STRING.fieldOf("prefix").forGetter(s -> s.prefix),
            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("types").forGetter(s -> s.types),
            ExtraCodecs.POSITIVE_FLOAT.fieldOf("chance").forGetter(s -> s.chance),
            BlockPos.CODEC.listOf().lenientOptionalFieldOf("pivots", List.of()).forGetter(s -> s.pivots)
    ).apply(instance, OnGroundStructure::new));

    private final String prefix;
    private final int types;
    private final float chance;
    private final List<BlockPos> pivots;

    public OnGroundStructure(StructureSettings settings, String prefix, int types, float chance, List<BlockPos> pivots) {
        super(settings);
        this.prefix = prefix;
        this.types = types;
        this.chance = chance;
        this.pivots = pivots;
        if (!pivots.isEmpty() && pivots.size() != types) {
            throw new IllegalArgumentException("Pivots size must be 0 or equals to types, currently is pivots.size=" + pivots.size() + ", types=" + types);
        }
    }

    public OnGroundStructure(StructureSettings settings, String prefix, int types, float chance) {
        this(settings, prefix, types, chance, List.of());
    }

    @Override
    protected Optional<GenerationStub> findGenerationPoint(GenerationContext context) {
        ChunkPos chunkPos = context.chunkPos();
        int blockX = chunkPos.getMiddleBlockX();
        int blockZ = chunkPos.getMiddleBlockZ();
        int middleX = (blockX + 1024) >> 11 << 11;
        int middleZ = (blockZ + 1024) >> 11 << 11;
        if (Mth.lengthSquared(middleX - blockX, middleZ - blockZ) > 160 * 160 && context.random().nextFloat() < chance) {
            int height = context.chunkGenerator().getFirstOccupiedHeight(
                    chunkPos.getMiddleBlockX(),
                    chunkPos.getMiddleBlockZ(),
                    Heightmap.Types.WORLD_SURFACE_WG,
                    context.heightAccessor(),
                    context.randomState()
            );
            BlockPos blockPos = new BlockPos(blockX, height + 1, blockZ);
            return Optional.of(new GenerationStub(blockPos, builder -> {
                StructureTemplateManager manager = context.structureTemplateManager();
                int type = context.random().nextInt(types);
                builder.addPiece(new SimpleTemplateStructurePiece(
                        manager,
                        prefix + type,
                        blockPos,
                        false,
                        true,
                        false,
                        Rotation.getRandom(context.random()),
                        pivots.isEmpty() ? BlockPos.ZERO : pivots.get(type)
                ));
            }));
        }
        return Optional.empty();
    }

    @Override
    public StructureType<?> type() {
        return RAFDimensions.ON_GROUND_STRUCTURE.get();
    }
}
