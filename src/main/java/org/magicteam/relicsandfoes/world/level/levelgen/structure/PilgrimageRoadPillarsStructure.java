package org.magicteam.relicsandfoes.world.level.levelgen.structure;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import org.magicteam.relicsandfoes.init.RAFDimensions;

import java.util.Optional;

public class PilgrimageRoadPillarsStructure extends Structure {
    public static final MapCodec<PilgrimageRoadPillarsStructure> CODEC = simpleCodec(PilgrimageRoadPillarsStructure::new);
    private static final SimpleWeightedRandomList<String> PILLARS = SimpleWeightedRandomList.<String>builder()
            .add("pilgrimage_pillar_0", 10)
            .add("pilgrimage_pillar_1", 10)
            .add("pilgrimage_pillar_2", 20)
            .add("pilgrimage_pillar_3", 20)
            .add("pilgrimage_eye_chest", 8)
            .add("pilgrimage_item_chest", 3)
            .add("pilgrimage_spawn_point", 5)
            .add("pilgrimage_airport", 8)
            .build();

    public PilgrimageRoadPillarsStructure(StructureSettings settings) {
        super(settings);
    }

    @Override
    protected Optional<GenerationStub> findGenerationPoint(GenerationContext context) {
        ChunkPos pos = context.chunkPos();
        int mx = pos.x % 128;
        int regionX = mx < 0 ? mx + 128 : mx;
        int mz = pos.z % 128;
        int regionZ = mz < 0 ? mz + 128 : mz;
        boolean xRoad = (regionX >= 7 && regionX <= 120) && (mz == 0 || mz == -1);
        boolean zRoad = (regionZ >= 6 && regionZ <= 119) && (mx == 0 || mx == -1);
        if (xRoad || zRoad) {
            int height = context.chunkGenerator().getFirstOccupiedHeight(
                    pos.getMiddleBlockX(),
                    pos.getMiddleBlockZ(),
                    Heightmap.Types.WORLD_SURFACE_WG,
                    context.heightAccessor(),
                    context.randomState()
            );
            BlockPos blockAt = new BlockPos(pos.getMinBlockX(), height, pos.getMinBlockZ());
            return PILLARS.getRandom(context.random()).map(pillar -> new GenerationStub(blockAt, builder -> {
                BlockPos offset;
                if (xRoad) {
                    offset = blockAt.offset(0, -4, mz == 0 ? 4 : -2);
                } else {
                    offset = blockAt.offset(mx == 0 ? 4 : -2, -4, 0);
                }
                builder.addPiece(new SimpleTemplateStructurePiece(
                        context.structureTemplateManager(),
                        pillar.data(),
                        offset,
                        true,
                        false,
                        false,
                        Rotation.getRandom(context.random()),
                        new BlockPos(7, 0, 7)
                ));
            }));
        }
        return Optional.empty();
    }

    @Override
    public StructureType<?> type() {
        return RAFDimensions.PILGRIMAGE_ROAD_PILLARS_STRUCTURE.get();
    }
}
