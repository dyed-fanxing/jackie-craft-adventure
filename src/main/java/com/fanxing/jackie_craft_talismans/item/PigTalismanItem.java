package com.fanxing.jackie_craft_talismans.item;

import com.fanxing.jackie_craft_talismans.JackieCraftTalismans;
import com.fanxing.jackie_craft_talismans.client.render.item.PigTalismanItemRender;
import com.fanxing.lib.registry.Attributes;
import com.fanxing.lib.registry.DataComponents;
import com.fanxing.lib.utils.collsion.CapsuleCCDUtils;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;
import java.util.function.Consumer;

public class PigTalismanItem extends AbstractTalismanItem implements GeoItem {
    private static final Logger log = LoggerFactory.getLogger(PigTalismanItem.class);
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public static final float RADIUS = 0.025F;
    public static final float GAP = 0.125F;
    public static final int SEGMENTS = 32;
    public static final int GROW_DURATION = 2;      // 光束生长刻数


    public static final ResourceLocation DAMAGE_INTERVAL_ID = ResourceLocation.fromNamespaceAndPath(JackieCraftTalismans.MOD_ID, "damage_interval");
    // 激光参数
    public static final float LASER_RANGE = 32f;      // 最大距离
    public static final float DAMAGE = 2.0f;      // 每次伤害
    public static final int DAMAGE_INTERVAL = 4;        // 每4刻（0.2秒）造成一次伤害
    public static final int USE_EASE = 4;        // 每4刻（0.2秒）造成一次伤害
    public static final int MAX_USE_DURATION = 1000;
    // 顺序：内层、外层、中心、边缘
    private static final List<Integer> DEFAULT = List.of(
            0xFFFFFF78, // 内层: R=255, G=255, B=120, A=255  -> (255<<24)|(255<<16)|(120<<8)|255
            0xB4AA6622, // 外层: R=170, G=102, B=34,  A=180  -> (180<<24)|(170<<16)|(102<<8)|34
            0xC8FFC850, // 中心: R=255, G=200, B=80,  A=200  -> (200<<24)|(255<<16)|(200<<8)|80
            0xC8AA6622  // 边缘: R=170, G=102, B=34,  A=200  -> (200<<24)|(170<<16)|(102<<8)|34
    );

    @Override
    public int getUseDuration(@NotNull ItemStack stack, @NotNull LivingEntity entity) {
        return MAX_USE_DURATION;
    }

    public PigTalismanItem(Properties properties) {
        super(properties.stacksTo(1).component(DataComponents.COLOR_SCHEME, DEFAULT));
    }


    /**
     * 每刻调用（长按持续）
     */
    @Override
    public void onUseTick(@NotNull Level level, @NotNull LivingEntity entity, @NotNull ItemStack stack, int remainingUseDuration) {
        // 只服务端执行伤害逻辑
        if (!level.isClientSide()) {
            int usingTicks = entity.getTicksUsingItem();
            if(usingTicks > CHARGE_DURATION){
                float progress = Math.min(1.0f, (float) (usingTicks - AbstractTalismanItem.CHARGE_DURATION) / GROW_DURATION);
                List<Entity> entities = CapsuleCCDUtils.getHitEntitiesOnViewVector(entity, 0.2f, LASER_RANGE*progress, e -> e.isAlive() && e != entity, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE);
                entities.forEach(target -> {
                    target.hurt(level.damageSources().indirectMagic(entity, entity), DAMAGE);
                    if (level instanceof ServerLevel serverLevel) {
                        // 命中音效（可选，也可以放在渲染器里）
                        serverLevel.playSound(null, target.blockPosition(), SoundEvents.LIGHTNING_BOLT_IMPACT, SoundSource.PLAYERS, 1.0f, 1.0f);
                    }
                });
            }
        }
        super.onUseTick(level, entity, stack, remainingUseDuration);
    }


    public static ItemAttributeModifiers createAttributes() {
        return ItemAttributeModifiers.builder().add(Attributes.DAMAGE_INTERVAL,
                        new AttributeModifier(DAMAGE_INTERVAL_ID, 4, AttributeModifier.Operation.ADD_VALUE),EquipmentSlotGroup.MAINHAND)
                .build();
    }


    public static int[] getBeamColor(ItemStack stack) {
        List<Integer> colors = stack.get(DataComponents.COLOR_SCHEME);
        return new int[]{colors.get(0),colors.get(1)};
    }
    public static int[] getLightingColor(ItemStack stack) {
        List<Integer> colors = stack.get(DataComponents.COLOR_SCHEME);
        return new int[]{colors.get(2),colors.get(3)};
    }

    // ---------- GeckoLib 渲染 ----------
    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {
            private PigTalismanItemRender render;

            @Override
            public @NotNull BlockEntityWithoutLevelRenderer getGeoItemRenderer() {
                if (render == null) {
                    render = new PigTalismanItemRender();
                }
                return render;
            }
        });
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
    }
}