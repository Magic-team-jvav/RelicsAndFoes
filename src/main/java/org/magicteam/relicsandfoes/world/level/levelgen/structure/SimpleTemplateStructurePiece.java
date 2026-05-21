package org.magicteam.relicsandfoes.world.level.levelgen.structure;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
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
    public SimpleTemplateStructurePiece(StructureTemplateManager manager, String name, BlockPos startPos, boolean overwrite, boolean ignoreEntities, boolean appleWaterlogging, Rotation rotation, BlockPos rotationPivot) {
        super(RAFDimensions.SIMPLE_TEMPLATE_STRUCTURE_PIECE.get(), 0, manager, RelicsAndFoes.asResource(name), name, makeSettings(overwrite, ignoreEntities, appleWaterlogging, rotation, rotationPivot), startPos);
    }

    public SimpleTemplateStructurePiece(StructureTemplateManager manager, String name, BlockPos startPos, boolean overwrite, boolean ignoreEntities, boolean appleWaterlogging, Rotation rotation) {
        this(manager, name, startPos, overwrite, ignoreEntities, appleWaterlogging, rotation, BlockPos.ZERO);
    }

    public SimpleTemplateStructurePiece(StructureTemplateManager manager, String name, BlockPos startPos) {
        this(manager, name, startPos, true, false, false, Rotation.NONE);
    }

    public SimpleTemplateStructurePiece(StructureTemplateManager manager, CompoundTag tag) {
        super(RAFDimensions.SIMPLE_TEMPLATE_STRUCTURE_PIECE.get(), tag, manager, id -> makeSettings(
                tag.getBoolean("Overwrite"),
                tag.getBoolean("IgnoreEntities"),
                tag.getBoolean("ApplyWaterlogging"),
                Rotation.valueOf(tag.getString("Rotation")),
                NbtUtils.readBlockPos(tag, "RotationPivot").orElse(BlockPos.ZERO)
        ));
    }

    private static StructurePlaceSettings makeSettings(
            boolean overwrite,
            boolean ignoreEntities,
            boolean applyWaterlogging,
            Rotation rotation,
            BlockPos rotationPivot
    ) {
        return new StructurePlaceSettings()
                .addProcessor(overwrite ? BlockIgnoreProcessor.STRUCTURE_BLOCK : BlockIgnoreProcessor.STRUCTURE_AND_AIR)
                .setIgnoreEntities(ignoreEntities)
                .setLiquidSettings(applyWaterlogging ? LiquidSettings.APPLY_WATERLOGGING : LiquidSettings.IGNORE_WATERLOGGING)
                .setRotation(rotation)
                .setRotationPivot(rotationPivot);
    }

    @Override
    protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
        super.addAdditionalSaveData(context, tag);
        tag.putBoolean("Overwrite", placeSettings.getProcessors().getFirst() == BlockIgnoreProcessor.STRUCTURE_BLOCK);
        tag.putBoolean("IgnoreEntities", placeSettings.isIgnoreEntities());
        tag.putBoolean("ApplyWaterlogging", placeSettings.shouldApplyWaterlogging());
        tag.putString("Rotation", placeSettings.getRotation().name());
        tag.put("RotationPivot", NbtUtils.writeBlockPos(placeSettings.getRotationPivot()));
    }

    @Override
    protected ResourceLocation makeTemplateLocation() {
        return RelicsAndFoes.asResource(templateName);
    }

    @Override
    protected void handleDataMarker(String name, BlockPos pos, ServerLevelAccessor level, RandomSource random, BoundingBox box) {}
}
