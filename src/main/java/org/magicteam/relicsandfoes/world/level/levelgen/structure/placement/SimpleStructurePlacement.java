package org.magicteam.relicsandfoes.world.level.levelgen.structure.placement;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.chunk.ChunkGeneratorStructureState;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacementType;
import org.magicteam.relicsandfoes.init.RAFDimensions;

import java.util.Optional;

public final class SimpleStructurePlacement extends StructurePlacement {
    public static final SimpleStructurePlacement INSTANCE = new SimpleStructurePlacement();

    public static final MapCodec<SimpleStructurePlacement> CODEC = MapCodec.unit(INSTANCE);

    private SimpleStructurePlacement() {
        super(Vec3i.ZERO, FrequencyReductionMethod.DEFAULT, 1, 0, Optional.empty());
    }

    @Override
    protected boolean isPlacementChunk(ChunkGeneratorStructureState structureState, int x, int z) {
        return true;
    }

    @Override
    public StructurePlacementType<?> type() {
        return RAFDimensions.SIMPLE_STRUCTURE_PLACEMENT.get();
    }
}
