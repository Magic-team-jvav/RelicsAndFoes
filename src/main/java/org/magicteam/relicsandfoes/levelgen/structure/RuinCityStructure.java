package org.magicteam.relicsandfoes.levelgen.structure;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import org.magicteam.relicsandfoes.init.RAFDimensions;

import java.util.Optional;

public class RuinCityStructure extends Structure {
    public static final MapCodec<RuinCityStructure> CODEC = simpleCodec(RuinCityStructure::new);

    public RuinCityStructure(StructureSettings settings) {
        super(settings);
    }

    @Override
    protected Optional<GenerationStub> findGenerationPoint(GenerationContext context) {
        ChunkPos pos = context.chunkPos();
        if (pos.x == 0 && pos.z == 0) {
            int height = context.chunkGenerator().getFirstOccupiedHeight(0, 0, Heightmap.Types.WORLD_SURFACE_WG, context.heightAccessor(), context.randomState());
            return Optional.of(new GenerationStub(new BlockPos(0, height, 0), builder -> {
                StructureTemplateManager manager = context.structureTemplateManager();
                int h = height - 5;
                for (int x = 0; x < 5; x++) {
                    for (int z = 0; z < 5; z++) {
                        builder.addPiece(new SuperTemplateStructurePiece(manager, "ruin_city_" + x + "_" + z, new BlockPos((x - 2) * 48 - 22, h, (z - 2) * 48 - 47)));
                    }
                }
            }));
        }
        return Optional.empty();
    }

    @Override
    public StructureType<?> type() {
        return RAFDimensions.RUIN_CITY_STRUCTURE.get();
    }
}
