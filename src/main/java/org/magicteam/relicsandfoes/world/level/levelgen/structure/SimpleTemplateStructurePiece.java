package org.magicteam.relicsandfoes.world.level.levelgen.structure;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.LiquidSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import org.magicteam.relicsandfoes.RelicsAndFoes;
import org.magicteam.relicsandfoes.init.RAFDimensions;

public class SimpleTemplateStructurePiece extends TemplateStructurePiece {
    public SimpleTemplateStructurePiece(StructureTemplateManager manager, String name, BlockPos startPos, boolean overwrite, boolean ignoreEntities, boolean appleWaterlogging, Rotation rotation) {
        super(RAFDimensions.SIMPLE_TEMPLATE_STRUCTURE_PIECE.get(), 0, manager, RelicsAndFoes.asResource(name), name, makeSettings(overwrite, ignoreEntities, appleWaterlogging, rotation), startPos);
    }

    public SimpleTemplateStructurePiece(StructureTemplateManager manager, String name, BlockPos startPos) {
        this(manager, name, startPos, true, false, false, Rotation.NONE);
    }

    public SimpleTemplateStructurePiece(StructureTemplateManager manager, CompoundTag tag) {
        super(RAFDimensions.SIMPLE_TEMPLATE_STRUCTURE_PIECE.get(), tag, manager, id -> makeSettings(
                tag.getBoolean("OW"),
                tag.getBoolean("IE"),
                tag.getBoolean("AW"),
                Rotation.valueOf(tag.getString("Rot"))
        ));
    }

    private static StructurePlaceSettings makeSettings(
            boolean overwrite,
            boolean ignoreEntities,
            boolean applyWaterLogging,
            Rotation rotation
    ) {
        return new StructurePlaceSettings()
                .addProcessor(overwrite ? BlockIgnoreProcessor.STRUCTURE_BLOCK : BlockIgnoreProcessor.STRUCTURE_AND_AIR)
                .setIgnoreEntities(ignoreEntities)
                .setLiquidSettings(applyWaterLogging ? LiquidSettings.APPLY_WATERLOGGING : LiquidSettings.IGNORE_WATERLOGGING)
                .setRotation(rotation);
    }

    @Override
    protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
        super.addAdditionalSaveData(context, tag);
        tag.putBoolean("OW", placeSettings.getProcessors().getFirst() == BlockIgnoreProcessor.STRUCTURE_BLOCK);
        tag.putBoolean("IE", placeSettings.isIgnoreEntities());
        tag.putBoolean("AW", placeSettings.shouldApplyWaterlogging());
        tag.putString("Rot", placeSettings.getRotation().name());
    }

    @Override
    protected ResourceLocation makeTemplateLocation() {
        return RelicsAndFoes.asResource(templateName);
    }

    @Override
    protected void handleDataMarker(String name, BlockPos pos, ServerLevelAccessor level, RandomSource random, BoundingBox box) {}
}
