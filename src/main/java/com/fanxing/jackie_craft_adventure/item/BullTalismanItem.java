package com.fanxing.jackie_craft_adventure.item;

import com.fanxing.jackie_craft_adventure.client.render.item.BullTalismanItemRender;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;

public class BullTalismanItem extends AbstractTalismanItem implements GeoItem {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public BullTalismanItem(Properties properties) {
        super(properties);
    }


    @Override
    public void inventoryTick(@NotNull ItemStack stack, Level level, @NotNull Entity entity, int slotId, boolean selected) {
        // 仅在服务端执行
        if (!level.isClientSide && entity instanceof LivingEntity living && ( slotId < 9 || slotId == 33)){
            // === 力量 III 效果 ===
            MobEffectInstance strengthEffect = living.getEffect(MobEffects.DAMAGE_BOOST);
            if ((strengthEffect == null) || (strengthEffect.getAmplifier() < 2) || strengthEffect.getDuration() <= 1) {
                living.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 40, 2));
            }
            // === 急迫 III 效果 ===
            MobEffectInstance hasteEffect = living.getEffect(MobEffects.DIG_SPEED);
            if ((hasteEffect == null) || (hasteEffect.getAmplifier() < 2) || hasteEffect.getDuration() <= 1) {
                living.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, 40, 2));
            }
        }
    }


    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {
            private BullTalismanItemRender render;

            @Override
            public @NotNull BlockEntityWithoutLevelRenderer getGeoItemRenderer() {
                if (render == null) {
                    render = new BullTalismanItemRender();
                }
                return render;
            }
        });
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
    }
}
