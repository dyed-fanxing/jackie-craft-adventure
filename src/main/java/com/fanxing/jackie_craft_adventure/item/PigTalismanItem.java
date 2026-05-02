package com.fanxing.jackie_craft_adventure.item;

import com.fanxing.jackie_craft_adventure.client.KeyBindings;
import com.fanxing.jackie_craft_adventure.client.gui.screen.PigTalismanConfigScreen;
import com.fanxing.jackie_craft_adventure.client.render.item.PigTalismanItemRender;
import com.fanxing.jackie_craft_adventure.entity.attachment.HeadEyeOffset;
import com.fanxing.jackie_craft_adventure.integration.curio.register.KeyBindingsJCTItg;
import com.fanxing.lib.integration.IntegrationFx;
import com.fanxing.lib.item.capability.LockHorizontalView;
import com.fanxing.lib.net.packet.StopUsingPacket;
import com.fanxing.lib.registry.DataComponentsFxLib;
import com.fanxing.lib.util.collsion.CapsuleCCDUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;
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

import static com.fanxing.jackie_craft_adventure.JackieCraftAdventure.MOD_ID;

public class PigTalismanItem extends AbstractTalismanItem implements LockHorizontalView, GeoItem {
    private static final Logger log = LoggerFactory.getLogger(PigTalismanItem.class);
    public static final ResourceLocation ATTACK_DAMAGE_MODIFIER_ID = ResourceLocation.fromNamespaceAndPath(MOD_ID, "default.attack_damage_id");

    public static final float RADIUS = 0.025F;
    public static final int GROW_DURATION = 2;      // 光束生长刻数
    public static final float LASER_RANGE = 32f;      // 最大距离
    public static final int MAX_USE_DURATION = 1000;
    // 顺序：内层、外层、中心、边缘
    public static final List<Integer> DEFAULT = List.of(
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
        super(properties);
    }

    @Override
    public void inventoryTick(@NotNull ItemStack stack, Level level, @NotNull Entity entity, int slot, boolean selected) {
        if (level.isClientSide && selected && KeyBindings.TALISMAN_CONFIG.isDown()) {
            Minecraft mc = Minecraft.getInstance();
            mc.options.keyUse.setDown(false);
            mc.setScreen(new PigTalismanConfigScreen((Player) entity));
            PacketDistributor.sendToServer(StopUsingPacket.INSTANCE);
        }
    }

    @Override
    public void onUseTick(@NotNull Level level, @NotNull LivingEntity entity, @NotNull ItemStack stack, int remainingUseDuration) {
        useTick(level, entity, stack, entity.getTicksUsingItem());
    }

    public void useTick(Level level, LivingEntity entity, ItemStack stack, int usingTicks) {
        if (!level.isClientSide()) {
            if (usingTicks > CHARGE_DURATION) {
                List<Vec3> eyesPosition = HeadEyeOffset.getEyesPosition(entity, 1.0f);
                for (Vec3 eyePosition : eyesPosition) {
                    float progress = Math.min(1.0f, (float) (usingTicks - AbstractTalismanItem.CHARGE_DURATION) / GROW_DURATION);
                    List<Entity> entities = CapsuleCCDUtils.getHitEntities(entity, eyePosition, RADIUS, LASER_RANGE * progress * progress, e -> e.isAlive() && e != entity, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE);
                    entities.forEach(target -> target.hurt(level.damageSources().indirectMagic(entity, entity), (float) entity.getAttributeValue(Attributes.ATTACK_DAMAGE)));
                }
            }
        }
    }


    public static ItemAttributeModifiers createAttributes() {
        return ItemAttributeModifiers.builder().add(
                Attributes.ATTACK_DAMAGE,
                new AttributeModifier(ATTACK_DAMAGE_MODIFIER_ID, 2, AttributeModifier.Operation.ADD_VALUE),
                EquipmentSlotGroup.MAINHAND
        ).build();
    }

    public static List<Integer> getColors(ItemStack stack) {
        return stack.get(DataComponentsFxLib.COLOR_SCHEME);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext tooltipContext, List<Component> tooltips, TooltipFlag flag) {
        tooltips.add(Component.translatable("item." + MOD_ID + ".pig_talisman.tooltip",
                Component.keybind(Minecraft.getInstance().options.keySprint.getName()).withStyle(ChatFormatting.BLUE),
                IntegrationFx.IS_LOAD_CURIOS ? Component.translatable("item.curio." + MOD_ID + ".pig_talisman.tooltip",
                        Component.keybind(KeyBindingsJCTItg.CURIO_USE_TALISMAN.getName()).withStyle(ChatFormatting.BLUE)) : Component.empty(),
                Component.keybind(KeyBindings.TALISMAN_CONFIG.getName()).withStyle(ChatFormatting.BLUE)));
    }

    // ---------- GeckoLib 渲染 ----------
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

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
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
    }
}