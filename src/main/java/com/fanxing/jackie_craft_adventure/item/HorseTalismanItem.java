package com.fanxing.jackie_craft_adventure.item;

import com.fanxing.jackie_craft_adventure.client.render.item.HorseTalismanItemRender;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.effect.MobEffectCategory;
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

public class HorseTalismanItem extends AbstractTalismanItem implements GeoItem {
    public HorseTalismanItem(Properties properties) {
        super(properties);
    }

    @Override
    public void inventoryTick(@NotNull ItemStack stack, Level level, @NotNull Entity entity, int slotId, boolean isSelected) {
        if (!level.isClientSide && entity instanceof LivingEntity living && ( slotId < 9 || slotId == 33)){
            MobEffectInstance regeneration = living.getEffect(MobEffects.REGENERATION);
            if (regeneration == null || regeneration.getAmplifier() < 2  || regeneration.getDuration() <= 1) {
                living.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 40, 2));
            }
            living.getActiveEffects().stream()
                    .map(MobEffectInstance::getEffect)
                    .filter(effect -> effect.value().getCategory() == MobEffectCategory.HARMFUL)
                    .toList()
                    .forEach(living::removeEffect);
        }
    }





    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {
            private HorseTalismanItemRender render;

            @Override
            public @NotNull BlockEntityWithoutLevelRenderer getGeoItemRenderer() {
                if (render == null) {
                    render = new HorseTalismanItemRender();
                }
                return render;
            }
        });
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
    }
}