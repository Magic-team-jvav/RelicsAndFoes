package org.magicteam.relicsandfoes.world.level.levelgen.structure.placement;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.chunk.ChunkGeneratorStructureState;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacementType;
import org.magicteam.relicsandfoes.init.RAFDimensions;

import java.util.Optional;

public final class CurrentChunkStructurePlacement extends StructurePlacement {
    public static final MapCodec<CurrentChunkStructurePlacement> CODEC = RecordCodecBuilder.mapCodec(instance -> placementCodec(instance).apply(instance, CurrentChunkStructurePlacement::new));

    @SuppressWarnings({"OptionalUsedAsFieldOrParameterType", "deprecation"})
    public CurrentChunkStructurePlacement(Vec3i locateOffset, FrequencyReductionMethod frequencyReductionMethod, float frequency, int salt, Optional<ExclusionZone> exclusionZone) {
        super(locateOffset, frequencyReductionMethod, frequency, salt, exclusionZone);
    }

    public CurrentChunkStructurePlacement(float frequency, int salt) {
        this(Vec3i.ZERO, FrequencyReductionMethod.DEFAULT, frequency, salt, Optional.empty());
    }

    public CurrentChunkStructurePlacement(float frequency) {
        this(frequency, 0);
    }

    public CurrentChunkStructurePlacement() {
        this(1);
    }

    @Override
    protected boolean isPlacementChunk(ChunkGeneratorStructureState structureState, int x, int z) {
        return true;
    }

    @Override
    public StructurePlacementType<?> type() {
        return RAFDimensions.CURRENT_CHUNK_STRUCTURE_PLACEMENT.get();
    }
}
