package com.fanxing.jackie_craft_adventure.item;

import com.fanxing.jackie_craft_adventure.client.particle.FlameRingParticle;
import com.fanxing.jackie_craft_adventure.client.render.item.LongTalismanItemRender;
import com.fanxing.jackie_craft_adventure.entity.summon.LongBeamEntity;
import com.fanxing.jackie_craft_adventure.registry.ParticleTypesJCA;
import com.fanxing.lib.item.capability.LockHorizontalView;
import com.fanxing.lib.particle.options.TrackEntityParticleOption;
import com.fanxing.lib.util.RotUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;
import java.util.function.Consumer;

import static com.fanxing.jackie_craft_adventure.JackieCraftAdventure.MOD_ID;

public class LongTalismanItem extends AbstractTalismanItem implements LockHorizontalView, GeoItem {
    // ======================= 龙炎束参数 =======================
    public static final int CHARGE_DURATION = 10;        // 最小蓄力刻数
    public static final int MAX_USE_DURATION = 72000;    // 最大使用时长
    public static final float MIN_RADIUS = 0.1F;          // 最小光束半径
    public static final float MAX_RADIUS = 1.0F;          // 最大光束半径（直径2.0F）
    public static final float BEAM_RANGE = 48.0F;         // 最大射程

    // 颜色方案：内层、外层
    public static final List<Integer> DEFAULT = List.of(
            0xFFFFEE88, // 内层: 亮黄白 (RGB 255,238,136) 极高亮度
            0xAA8B1A1A  // 外层: 暗红 (Alpha=0xAA ≈ 0.67) 半透明
    );

    public static final ResourceLocation ATTACK_DAMAGE_MODIFIER_ID =
            ResourceLocation.fromNamespaceAndPath(MOD_ID, "long_talisman.damage");

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public LongTalismanItem(Properties properties) {
        super(properties);
    }

    @Override
    public int getUseDuration(@NotNull ItemStack stack, @NotNull LivingEntity entity) {
        return MAX_USE_DURATION;
    }

    // ======================= 蓄力阶段 =======================
    @Override
    public void onUseTick(@NotNull Level level, @NotNull LivingEntity entity, @NotNull ItemStack stack, int remainingUseDuration) {
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        if(level instanceof ServerLevel serverLevel){
            if(player.isShiftKeyDown()){
                serverLevel.sendParticles(TrackEntityParticleOption.create(ParticleTypesJCA.FLAME_BALL.get(),player.getId()),player.getX(),player.getY(),player.getZ(),1,0,0,0,0);
            }
        }
        return super.use(level, player, hand);
    }

    // ======================= 松开发射 =======================
    @Override
    public void onStopUsing(@NotNull ItemStack stack, @NotNull LivingEntity entity, int count) {
        super.onStopUsing(stack, entity, count);
        if (entity.level().isClientSide) return;
        int usingTicks = MAX_USE_DURATION - count;
        if (usingTicks < CHARGE_DURATION) return;

        // 基于蓄力时长计算光束半径
        float chargeRatio = Math.min(1.0f, (usingTicks - CHARGE_DURATION) / 20.0f); // 30刻满蓄
        float radius = MIN_RADIUS + chargeRatio * (MAX_RADIUS - MIN_RADIUS);

        Vec3 start = entity.getEyePosition(1.0f).add(RotUtils.rotateYX(0,0,0f,entity.getViewYRot(1.0f),entity.getViewXRot(1.0f)));
        Vec3 direction = entity.getViewVector(1.0f);

        // 生成龙炎束实体
        LongBeamEntity beam = new LongBeamEntity(entity.level(), entity, radius);
        beam.setPos(start);
        beam.setDeltaMovement(direction.scale(LongBeamEntity.BEAM_SPEED));
        entity.level().addFreshEntity(beam);
    }

    // ======================= 属性 =======================
    public static ItemAttributeModifiers createAttributes() {
        return ItemAttributeModifiers.builder().add(
                Attributes.ATTACK_DAMAGE,
                new AttributeModifier(ATTACK_DAMAGE_MODIFIER_ID, 6.0, AttributeModifier.Operation.ADD_VALUE),
                EquipmentSlotGroup.MAINHAND
        ).build();
    }

    @Override
    public void appendHoverText(@NotNull ItemStack itemStack, @NotNull TooltipContext tooltipContext,
                                List<Component> tooltips, @NotNull TooltipFlag flag) {
        tooltips.add(Component.translatable("item." + MOD_ID + ".long_talisman.tooltip.line1"));
        tooltips.add(Component.translatable("item." + MOD_ID + ".long_talisman.tooltip.line2"));
    }

    // ======================= GeoItem =======================
    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {
            private LongTalismanItemRender render;

            @Override
            public @NotNull BlockEntityWithoutLevelRenderer getGeoItemRenderer() {
                if (render == null) render = new LongTalismanItemRender();
                return render;
            }
        });
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {}
}
