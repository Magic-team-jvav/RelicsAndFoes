package org.magicteam.relicsandfoes.world.level.levelgen.structure;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import org.magicteam.relicsandfoes.init.RAFDimensions;

import java.util.Optional;

public class CrossRealmAncientRuinsStructure extends Structure {
    public static final MapCodec<CrossRealmAncientRuinsStructure> CODEC = simpleCodec(CrossRealmAncientRuinsStructure::new);

    public CrossRealmAncientRuinsStructure(StructureSettings settings) {
        super(settings);
    }

    @Override
    protected Optional<GenerationStub> findGenerationPoint(GenerationContext context) {
        ChunkPos pos = context.chunkPos();
        if (pos.x % 128 == 0 && Math.floorMod(pos.z, 128) == 127) {
            int minBlockX = pos.getMinBlockX();
            int minBlockZ = pos.getMinBlockZ();
            int height = context.chunkGenerator().getFirstOccupiedHeight(minBlockX, minBlockZ, Heightmap.Types.WORLD_SURFACE_WG, context.heightAccessor(), context.randomState());
            return Optional.of(new GenerationStub(new BlockPos(minBlockX, height, minBlockZ), builder -> {
                StructureTemplateManager manager = context.structureTemplateManager();
                int h = height - 5;
                for (int x = 0; x < 5; x++) {
                    for (int z = 0; z < 5; z++) {
                        BlockPos startPos = new BlockPos(minBlockX + (x - 2) * 48 - 22, h, minBlockZ + (z - 2) * 48 - 31);
                        builder.addPiece(new SimpleTemplateStructurePiece(manager, "cross_realm_ancient_ruins" + x + z, startPos));
                    }
                }
            }));
        }
        return Optional.empty();
    }

    @Override
    public StructureType<?> type() {
        return RAFDimensions.CROSS_REALM_ANCIENT_RUINS_STRUCTURE.get();
    }
}
