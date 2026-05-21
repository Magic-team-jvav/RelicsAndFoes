package org.magicteam.relicsandfoes.world.level.levelgen.structure;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import org.magicteam.relicsandfoes.init.RAFDimensions;

import java.util.Optional;

public class SkyStoneStructure extends Structure {
    public static final MapCodec<SkyStoneStructure> CODEC = simpleCodec(SkyStoneStructure::new);

    public SkyStoneStructure(StructureSettings settings) {
        super(settings);
    }

    @Override
    protected Optional<GenerationStub> findGenerationPoint(GenerationContext context) {
        ChunkPos chunkPos = context.chunkPos();
        int blockX = chunkPos.getMiddleBlockX();
        int blockZ = chunkPos.getMiddleBlockZ();
        int middleX = (blockX + 1024) >> 11 << 11;
        int middleZ = (blockZ + 1024) >> 11 << 11;
        if (Mth.lengthSquared(middleX - blockX, middleZ - blockZ) > 160 * 160 && context.random().nextFloat() < 0.075F) {
            BlockPos blockPos = new BlockPos(blockX, context.random().nextIntBetweenInclusive(65, 190), blockZ);
            return Optional.of(new GenerationStub(blockPos, builder -> {
                StructureTemplateManager manager = context.structureTemplateManager();
                builder.addPiece(new SimpleTemplateStructurePiece(
                        manager,
                        "sky_stone_" + context.random().nextInt(4),
                        blockPos,
                        true,
                        true,
                        false,
                        Rotation.getRandom(context.random())
                ));
            }));
        }
        return Optional.empty();
    }

    @Override
    public StructureType<?> type() {
        return RAFDimensions.SKY_STONE.get();
    }
}
