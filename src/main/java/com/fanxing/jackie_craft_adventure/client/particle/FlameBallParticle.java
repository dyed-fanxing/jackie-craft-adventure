package com.fanxing.jackie_craft_adventure.client.particle;

import com.fanxing.lib.client.particle.property.RotationStrategy;
import com.fanxing.lib.client.particle.quad.GlowCircleSoftParticle;
import com.fanxing.lib.client.particle.util.F2FFactoryUtils;
import com.fanxing.lib.particle.options.TrackEntityParticleOption;
import com.fanxing.lib.util.math.ease.EasingType;
import com.fanxing.lib.util.RotUtils;
import com.fanxing.lib.util.math.RandomUtils;
import it.unimi.dsi.fastutil.floats.FloatUnaryOperator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.NoRenderParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author dyed_fanxing
 * @date 2026/5/8 20:46
 */
public class FlameBallParticle extends NoRenderParticle {
    protected Entity entity;
    protected List<Particle> children = new ArrayList<>();


    public FlameBallParticle(ClientLevel level, double x, double y, double z, double vx, double vy, double vz) {
        super(level, x, y, z, vx, vy, vz);
        setLifetime(1000);
    }

    @Override
    public void tick() {
        if (entity instanceof LivingEntity living && living.isAlive() && !living.isUsingItem()){
            remove();
            return;
        }
        super.tick();
        Vec3 pos = RotUtils.rotateYX(0, 0, 1, entity.getViewYRot(1.0f), entity.getViewXRot(1.0f)).add(entity.getEyePosition());
        setPos(pos.x,pos.y,pos.z);
        if(age==1) children.add(createGlowSoftParticle(level, x, y, z));
        for(int i = 0; i < 2; ++i) {
            children.add(createDiffusionFireRing(level,x,y,z));
            children.add(createInnerFireRing(level,x,y,z));
        }
        if(age % 3==0){
            children.add(createGlowSoftParticle(level, x, y, z));
        }
        Iterator<Particle> iterator = children.iterator();
        while (iterator.hasNext()) {
            Particle child = iterator.next();
            if (child.isAlive()) child.setPos(x, y, z);
            else iterator.remove();
        }
    }


    // 直接使用 RingParticle，并在构造后配置策略
    public static FlameRingParticle createDiffusionFireRing(ClientLevel level, double x, double y, double z) {
        FlameRingParticle ring = new FlameRingParticle(level, x, y, z, 0, 0, 0);
        int lifetime = RandomUtils.around(30, 10);
        ring.setLifetime(lifetime); // 2 秒
        // 旋转策略：绕 Y 轴匀速旋转，生成时候随机旋转角度
        RotationStrategy.AngularVelocity rot = new RotationStrategy.AngularVelocity(
                RandomUtils.around(0f,180f), RandomUtils.around(0,360f), RandomUtils.around(0f,360f),
                RandomUtils.around(0f,3f), RandomUtils.around(0f,3f), RandomUtils.bipolarAbs(6f,3f)
        );
        ring.setRotationProperty(rot);

        FloatUnaryOperator scaleEaseOut = F2FFactoryUtils.easing(0, RandomUtils.around(1.0f,0.3f), EasingType.OUT_QUAD);
        ring.setScale(scaleEaseOut);
        FloatUnaryOperator alphaEaseOut = F2FFactoryUtils.easing(1.0f, 0f, EasingType.OUT_QUAD);
        ring.setAlphaFactory(alphaEaseOut);
        Minecraft.getInstance().particleEngine.add(ring);
        return ring;
    }
    // 直接使用 RingParticle，并在构造后配置策略
    public static FlameRingParticle createInnerFireRing(ClientLevel level, double x, double y, double z) {
        FlameRingParticle ring = new FlameRingParticle(level, x, y, z, 0, 0, 0);
        int lifetime = RandomUtils.around(30, 10);
        ring.setLifetime(lifetime); // 2 秒
        // 旋转策略：绕 Y 轴匀速旋转，生成时候随机旋转角度
        RotationStrategy.AngularVelocity rot = new RotationStrategy.AngularVelocity(
                RandomUtils.around(0f,180f), RandomUtils.around(0,360f), RandomUtils.around(0f,360f),
                RandomUtils.bipolarAbs(5F,1f), RandomUtils.bipolarAbs(5F,1f), RandomUtils.bipolarAbs(10F,2f)
        );
        ring.setRotationProperty(rot);

        FloatUnaryOperator scaleEaseOut = F2FFactoryUtils.easing(0f, RandomUtils.around(0.9f,0.2f), EasingType.OUT_QUAD);
        ring.setScale(scaleEaseOut);
        FloatUnaryOperator alphaEaseOut = F2FFactoryUtils.easing(1.0f, 0f, EasingType.OUT_QUAD);
        ring.setAlphaFactory(alphaEaseOut);

        Minecraft.getInstance().particleEngine.add(ring);
        return ring;
    }
    public static GlowCircleSoftParticle createGlowSoftParticle(ClientLevel level, double x, double y, double z){
        GlowCircleSoftParticle glow = new GlowCircleSoftParticle(level, x, y, z);
        glow.setAlpha(RandomUtils.range(48, 80));
        glow.setColor(255,128,64);
        glow.setLifetime(RandomUtils.around(30, 10)); // 2 秒
        glow.setSize(F2FFactoryUtils.easing(0f, RandomUtils.around(1.5f,0.1F), EasingType.LINEAR));
        glow.setAlphaFactory(F2FFactoryUtils.easing(1.0f, 0f, EasingType.LINEAR));
        Minecraft.getInstance().particleEngine.add(glow);
        return glow;
    }


    public static class Provider implements ParticleProvider<TrackEntityParticleOption> {
        @Override
        public Particle createParticle(@NotNull TrackEntityParticleOption type, @NotNull ClientLevel level,
                                       double x, double y, double z,
                                       double vx, double vy, double vz) {
            FlameBallParticle particle = new FlameBallParticle(level, x, y, z, vx, vy, vz);
            particle.entity = level.getEntity(type.getEntityId());
            return particle;
        }
    }
}
