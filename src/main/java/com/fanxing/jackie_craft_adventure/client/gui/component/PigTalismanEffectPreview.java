package com.fanxing.jackie_craft_adventure.client.gui.component;

import com.fanxing.jackie_craft_adventure.client.render.item.PigTalismanItemRender;
import com.fanxing.jackie_craft_adventure.entity.attachment.HeadEyeOffset;
import com.fanxing.lib.client.gui.component.ColorSwatch;
import com.fanxing.lib.client.gui.component.LivingEntityPreview;
import com.fanxing.lib.client.gui.component.RadioGroup;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.LivingEntity;

public class PigTalismanEffectPreview extends LivingEntityPreview<LivingEntity> {
    protected HeadEyeOffset headEyeOffset;
    protected RadioGroup<Integer, ColorSwatch> schemeGroup;

    public PigTalismanEffectPreview(int x, int y, int width, int height, LivingEntity entity, HeadEyeOffset headEyeOffset, RadioGroup<Integer, ColorSwatch> schemeGroup) {
        super(x, y, width, height, entity);
        this.headEyeOffset = headEyeOffset;
        this.schemeGroup = schemeGroup;
    }

    @Override
    protected void afterRender(GuiGraphics graphics, float partialTick) {
        PigTalismanItemRender.render(graphics.pose(), graphics.bufferSource(), entity, headEyeOffset, schemeGroup.getValues(), 20, partialTick);
    }
}
