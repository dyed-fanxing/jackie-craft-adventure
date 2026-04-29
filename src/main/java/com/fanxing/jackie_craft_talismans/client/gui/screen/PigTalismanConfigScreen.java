package com.fanxing.jackie_craft_talismans.client.gui.screen;

import com.fanxing.jackie_craft_talismans.client.gui.component.PigTalismanEffectPreview;
import com.fanxing.jackie_craft_talismans.entity.attachment.HeadEyeOffset;
import com.fanxing.jackie_craft_talismans.item.PigTalismanItem;
import com.fanxing.jackie_craft_talismans.net.packet.HeadEyeOffsetPacket;
import com.fanxing.lib.client.gui.Placement;
import com.fanxing.lib.client.gui.component.*;
import com.fanxing.lib.client.gui.layout.FlexBoxLayout;
import com.fanxing.lib.client.gui.renderer.BackgroundRenderer;
import com.fanxing.lib.client.gui.screen.SimpleEditBoxScreen;
import com.fanxing.lib.client.gui.utils.Buttons;
import com.fanxing.lib.item.compoent.ColorPalette;
import com.fanxing.lib.net.packet.ColorPalettesPacket;
import com.fanxing.lib.net.packet.ColorSchemePacket;
import com.fanxing.lib.registry.DataComponentsFxLib;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.List;
import java.util.Objects;

public class PigTalismanConfigScreen extends Screen {
    public static final int COLOR_SWATCH_SIZE = 20;
    public static final int PADDING = 5;
    public static final int TOP_HEIGHT = 250;
    // 数据副本
    private HeadEyeOffset currentOffset;
    // 所有 GUI 组件（创建后存储在成员变量中）
    protected StepSlider headSlider, eyeHeightSlider, surfaceSlider, gapSlider;
    protected ColorPicker colorPicker;
    protected RadioGroup<Integer, ColorSwatch> schemeGroup;
    protected RadioGroup<List<Integer>, ColorPaletteWidget> presetGroup;
    protected FlexBoxLayout topLayout;
    protected FlexBoxLayout presetLayout;
    protected HeaderAndFooterLayout rootLayout;
    protected PigTalismanEffectPreview previewWidget;


    public PigTalismanConfigScreen(LivingEntity entity) {
        super(Component.literal("眼睛偏移配置"));
        this.currentOffset = HeadEyeOffset.getOffset(entity).copy();
        ItemStack currentItem = entity.getMainHandItem();
        // 左侧面板（滑块和按钮）
        FlexBoxLayout headEyePanel = createHeadEyePanel(entity);
        FlexBoxLayout colorPickerPanel = createColorPickerPanel(currentItem.get(DataComponentsFxLib.COLOR_SCHEME));
        // 2.2 实体预览
        previewWidget = new PigTalismanEffectPreview(0, 0, 250, TOP_HEIGHT, entity, currentOffset, schemeGroup);
        Button savePreset = new Button.Builder(
                Component.translatable("selectWorld.edit.save").append(Component.translatable("gui.fx_lib.preset")),
                (t) -> savePresetPalettes()).build();
        rootLayout = new HeaderAndFooterLayout(this, TOP_HEIGHT, savePreset.getHeight());
        // 2.4 底部预设栏
        presetLayout = creatPresetColorPalette(Objects.requireNonNull(currentItem.get(DataComponentsFxLib.COLOR_PALETTES)));

        topLayout = new FlexBoxLayout(width, TOP_HEIGHT).horizontal().padding(PADDING, 0);
        topLayout.addChild(headEyePanel);
        topLayout.addChild(previewWidget);
        topLayout.addChild(colorPickerPanel);

        rootLayout.addToHeader(topLayout);
        rootLayout.addToContents(presetLayout);
        rootLayout.addToFooter(savePreset);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    protected void init() {
        super.init();
        rootLayout.arrangeElements();
        topLayout.setWidth(width);
        presetLayout.setWidth(width);
        presetLayout.setHeight(rootLayout.getContentHeight());
        rootLayout.arrangeElements();

        // 添加背景渲染器（放在子组件注册之前，确保背景先渲染）
        addRenderableOnly(BackgroundRenderer.textured(presetLayout, 0));
        rootLayout.visitWidgets(this::addRenderableWidget);
    }

    private FlexBoxLayout createHeadEyePanel(LivingEntity entity) {
        int panelWidth = 150;
        FlexBoxLayout panel = new FlexBoxLayout(0, 0, panelWidth, TOP_HEIGHT).vertical().justifyContent(FlexBoxLayout.JustifyContent.SPACE_EVENLY).padding(PADDING, 0);
        double eyeHeightMin = 0, eyeHeightMax = 20.0;
        double surfaceMin = 0, surfaceMax = 20.0;
        double eyeGapMin = 0, eyeGapMax = 20.0;
        if (entity instanceof Player) {
            eyeHeightMin = 0.0;
            eyeHeightMax = 0.5;
            surfaceMin = 0.0;
            surfaceMax = 0.3;
            eyeGapMin = 0.0;
            eyeGapMax = 0.3;
        } else {
            headSlider = StepSlider.createWithOptionName(0, 0, panelWidth, 20,
                    0.0, 20.0, 0.01, currentOffset.headPivotLogicEyeOffset(), 2,
                    Component.translatable("gui.jackie_craft_talismans.head_pivot_logic_eye_offset"),
                    val -> currentOffset.setHeadPivotLogicEyeOffset(val.floatValue())
            );
            headSlider.setTooltip(Tooltip.create(Component.translatable("gui.jackie_craft_talismans.head_pivot_logic_eye_offset_tooltip")));
            panel.addChild(headSlider);
        }

        eyeHeightSlider = StepSlider.createWithOptionName(0, 0, panelWidth, 20,
                eyeHeightMin, eyeHeightMax, 0.005, currentOffset.eyeHeightOffset(), 2,
                Component.translatable("gui.jackie_craft_talismans.eye_height_offset"),
                val -> currentOffset.setEyeHeight(val.floatValue())
        );
        eyeHeightSlider.setTooltip(Tooltip.create(Component.translatable("gui.jackie_craft_talismans.eye_height_offset_tooltip")));
        surfaceSlider = StepSlider.createWithOptionName(0, 0, panelWidth, 20,
                surfaceMin, surfaceMax, 0.005, currentOffset.eyeSurfaceOffset(), 2,
                Component.translatable("gui.jackie_craft_talismans.eye_surface_offset"),
                val -> currentOffset.setEyeSurfaceOffset(val.floatValue())
        );
        surfaceSlider.setTooltip(Tooltip.create(Component.translatable("gui.jackie_craft_talismans.eye_surface_offset_tooltip")));
        gapSlider = StepSlider.createWithOptionName(0, 0, panelWidth, 20,
                eyeGapMin, eyeGapMax, 0.005, currentOffset.eyeGap(), 2,
                Component.translatable("gui.jackie_craft_talismans.eye_gap"),
                val -> currentOffset.setEyeGap(val.floatValue())
        );

        panel.addChild(eyeHeightSlider);
        panel.addChild(surfaceSlider);
        panel.addChild(gapSlider);

        FlexBoxLayout buttonLayout = new FlexBoxLayout(0, 0, panelWidth, 0)
                .justifyContent(FlexBoxLayout.JustifyContent.SPACE_EVENLY);
        buttonLayout.addChild(new Button.Builder(Component.translatable("selectWorld.edit.save"), btn -> saveHeadEyeOffset())
                .size(panelWidth / 3, 20).build());
        buttonLayout.addChild(new Button.Builder(Component.translatable("controls.reset"), btn -> {
            currentOffset.resetPlayer();
            if (headSlider != null) headSlider.setValue(currentOffset.headPivotLogicEyeOffset());
            eyeHeightSlider.setValue(currentOffset.eyeHeightOffset());
            surfaceSlider.setValue(currentOffset.eyeSurfaceOffset());
            gapSlider.setValue(currentOffset.eyeGap());
        }).size(panelWidth / 3, 20).build());
        buttonLayout.arrangeElements();
        panel.addChild(buttonLayout);
        panel.arrangeElements();
        return panel;
    }

    private FlexBoxLayout createColorPickerPanel(List<Integer> colors) {
        int width = 150;
        FlexBoxLayout layout = new FlexBoxLayout(width, 0).vertical().justifyContent(FlexBoxLayout.JustifyContent.SPACE_EVENLY).gap(8);
        // 色块单选组
        schemeGroup = new RadioGroup<>();
        FlexBoxLayout swatchBarLayout = new FlexBoxLayout(width, 0).justifyContent(FlexBoxLayout.JustifyContent.SPACE_EVENLY).gap(8);
        String[] labels = {"beam_inner", "beam_outer", "lightning_core", "lightning_edge"};
        if (colors != null) {
            for (int i = 0; i < colors.size(); i++) {
                ColorSwatch swatch = new ColorSwatch(0, 0, 24, colors.get(i), s -> {
                    schemeGroup.select(s);
                    colorPicker.setColor(s.getValue());
                }, Component.translatable("gui.jackie_craft_talismans." + labels[i]));
                swatchBarLayout.addChild(swatch);
                schemeGroup.addOption(swatch);
            }
            schemeGroup.selectFirst();
        }

        // 颜色选择器
        colorPicker = new ColorPicker(0, 0, width, width, schemeGroup.getSelected().getValue(), color -> schemeGroup.getSelected().setColor(color));
        FlexBoxLayout buttonLayout = new FlexBoxLayout(0, 0, width, 0).horizontal().justifyContent(FlexBoxLayout.JustifyContent.SPACE_EVENLY);
        int buttonWidth = width/3-6;
        buttonLayout.addChild(new Button.Builder(Component.translatable("selectWorld.edit.save"), btn -> saveCurrentColor()).size(buttonWidth, 20).build());
        buttonLayout.addChild(new Button.Builder(Component.translatable("gui.fx_lib.add").append(
                Component.translatable("gui.fx_lib.preset")), btn -> savePresetColor()).size(buttonWidth, 20).build());
        buttonLayout.addChild(new Button.Builder(Component.translatable("controls.reset"), btn -> {
            List<ColorSwatch> options = schemeGroup.getOptions();
            for (int i = 0; i < options.size(); i++) {
                options.get(i).setColor(PigTalismanItem.DEFAULT.get(i));
            }
        }).size(buttonWidth, 20).build());
        layout.addChild(colorPicker);
        layout.addChild(swatchBarLayout);
        layout.addChild(buttonLayout);
        return layout;
    }

    private FlexBoxLayout creatPresetColorPalette(List<ColorPalette> palettes) {
        FlexBoxLayout layout = new FlexBoxLayout(width, 0).horizontal()
                .justifyContent(FlexBoxLayout.JustifyContent.CENTER)
                .alignItems(FlexBoxLayout.AlignItems.CENTER)
                .flexWrap(FlexBoxLayout.FlexWrap.WRAP)
                .padding(5, 5).gap(8);
        presetGroup = new RadioGroup<>();
        for (ColorPalette cp : palettes) {
            ColorPaletteWidget palette = createColorPaletteWidget(cp.label(), cp.colors());
            presetGroup.addOption(palette);
            layout.addChild(createColorPaletteDeletePopup(palette));
        }
        return layout;
    }

    private void refreshPresetColorPaletteByGroup() {
        List<ColorPaletteWidget> options = presetGroup.getOptions();
        presetLayout.visitWidgets(this::removeWidget);
        presetLayout.getChildren().clear();
        presetGroup = new RadioGroup<>();
        for (ColorPaletteWidget palette : options) {
            presetGroup.addOption(palette);
            presetLayout.addChild(createColorPaletteDeletePopup(palette));
        }
        presetLayout.arrangeElements();
        presetLayout.visitWidgets(this::addRenderableWidget);
    }

    public InlinePopup<ColorPaletteWidget> createColorPaletteDeletePopup(ColorPaletteWidget palette) {
        return new InlinePopup<>(0, 0, palette, Buttons.delete(() -> {
            presetGroup.getOptions().remove(palette);
            refreshPresetColorPaletteByGroup();
        }), Placement.BOTTOM, 1);
    }

    protected ColorPaletteWidget createColorPaletteWidget(String name, List<Integer> colors) {
        return ColorPaletteWidget.horizontal(0, 0, COLOR_SWATCH_SIZE, COLOR_SWATCH_SIZE, colors, this::onPressColorPalette, Component.translatable(name));
    }


    public void onPressColorPalette(ColorPaletteWidget pal) {
        List<Integer> colors = pal.getValue();
        List<ColorSwatch> options = schemeGroup.getOptions();
        for (int idx = 0; idx < colors.size(); idx++) {
            options.get(idx).setColor(colors.get(idx));
        }
        colorPicker.setColor(schemeGroup.getSelected().getValue());
        presetGroup.select(pal);
    }

    private void saveHeadEyeOffset() {
        PacketDistributor.sendToServer(new HeadEyeOffsetPacket(currentOffset));
        onClose();
    }

    protected void saveCurrentColor() {
        PacketDistributor.sendToServer(new ColorSchemePacket(schemeGroup.getValues()));
        onClose();
    }

    private void savePresetColor() {
        minecraft.setScreen(new SimpleEditBoxScreen(this, Component.translatable("gui.jackie_craft_talismans.color_preset.tooltip"), this::doSavePreset));
    }

    private void doSavePreset(String name) {
        presetGroup.addOption(createColorPaletteWidget(name, schemeGroup.getValues()));
        //刷新预设栏
        refreshPresetColorPaletteByGroup();
    }

    private void savePresetPalettes() {
        PacketDistributor.sendToServer(new ColorPalettesPacket(presetGroup.getOptions().stream().map((t) -> new ColorPalette(t.getMessage().getString(), t.getValue())).toList()));
        onClose();
    }


}