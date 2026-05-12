package com.fanxing.jackie_craft_adventure.client.particle;

import com.fanxing.lib.FxLib;
import com.fanxing.lib.client.particle.mesh.QuadParticleRenderer;
import com.fanxing.lib.client.particle.property.RotationStrategy;
import com.fanxing.lib.client.particle.rendertypes.BeamParticleRenderTypes;
import com.fanxing.lib.client.particle.rendertypes.ParticleRenderTypes;
import com.fanxing.lib.client.particle.ring.BaseRingParticle;
import com.fanxing.lib.client.render.data.RingLayer;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author dyed_fanxing
 * @date 2026/5/8 11:09
 */
public class FlameRingParticle extends BaseRingParticle {
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(FxLib.MOD_ID, "textures/particle/fire_single.png");
    /**
     * 基础环层几何数据：半径1，橙色到黄色的渐变环
     */
    private static final List<RingLayer> BASE_LAYERS = List.of(
            new RingLayer(0.5f, 0.0f, 0x00FF6020), // 底部半径1，半透明橙色
            new RingLayer(0.5f, 0.15f, 0xFFFF6600), // 中间半径1，橙色
            new RingLayer(0.5f, 0.3f, 0x00FF6020)  // 顶部半径1，半透明橙色
    );


    private static final Logger log = LoggerFactory.getLogger(FlameRingParticle.class);

    public FlameRingParticle(ClientLevel level, double x, double y, double z, double vx, double vy, double vz) {
        super(level, x, y, z, vx, vy, vz, BASE_LAYERS);
    }


    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderTypes.ADDITIVE_QUADS_REPEAT.apply(TEXTURE);
    }
}
