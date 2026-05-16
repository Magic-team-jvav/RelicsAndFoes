package org.magicteam.relicsandfoes.world.level.levelgen.structure;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.LiquidSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import org.magicteam.relicsandfoes.RelicsAndFoes;
import org.magicteam.relicsandfoes.init.RAFDimensions;

public class SimpleTemplateStructurePiece extends TemplateStructurePiece {
    public SimpleTemplateStructurePiece(StructureTemplateManager manager, String name, BlockPos startPos) {
        super(RAFDimensions.SIMPLE_TEMPLATE_STRUCTURE_PIECE.get(), 0, manager, RelicsAndFoes.asResource(name), name, makeSettings(), startPos);
    }

    public SimpleTemplateStructurePiece(StructureTemplateManager manager, CompoundTag tag) {
        super(RAFDimensions.SIMPLE_TEMPLATE_STRUCTURE_PIECE.get(), tag, manager, id -> makeSettings());
    }

    private static StructurePlaceSettings makeSettings() {
        return new StructurePlaceSettings()
                .setIgnoreEntities(false)
                .setLiquidSettings(LiquidSettings.IGNORE_WATERLOGGING)
                .addProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK)
                .setRotation(Rotation.NONE);
    }

    @Override
    protected void handleDataMarker(String name, BlockPos pos, ServerLevelAccessor level, RandomSource random, BoundingBox box) {}
}
