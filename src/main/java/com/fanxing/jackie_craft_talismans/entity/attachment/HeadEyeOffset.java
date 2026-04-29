package com.fanxing.jackie_craft_talismans.entity.attachment;

import com.fanxing.jackie_craft_talismans.registry.AttachmentTypesJCT;
import com.fanxing.lib.util.RotUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HeadEyeOffset {
    // 默认玩家数据（用于后备）
    private static final HeadEyeOffset DEFAULT_PLAYER = new HeadEyeOffset(-0.22f, 0.18f, 0.25f, 0.125f);

    // Codec（手动实现，因为不再是 record）
    public static final Codec<HeadEyeOffset> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.FLOAT.fieldOf("headPivotLogicEyeOffset").forGetter(HeadEyeOffset::headPivotLogicEyeOffset),
            Codec.FLOAT.fieldOf("eyeHeightOffset").forGetter(HeadEyeOffset::eyeHeightOffset),
            Codec.FLOAT.fieldOf("eyeSurfaceOffset").forGetter(HeadEyeOffset::eyeSurfaceOffset),
            Codec.FLOAT.fieldOf("eyeGap").forGetter(HeadEyeOffset::eyeGap)
    ).apply(instance, HeadEyeOffset::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, HeadEyeOffset> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT, HeadEyeOffset::headPivotLogicEyeOffset,
            ByteBufCodecs.FLOAT, HeadEyeOffset::eyeHeightOffset,
            ByteBufCodecs.FLOAT, HeadEyeOffset::eyeSurfaceOffset,
            ByteBufCodecs.FLOAT, HeadEyeOffset::eyeGap,
            HeadEyeOffset::new
    );

    private float headPivotLogicEyeOffset;
    private float eyeHeightOffset;
    private float eyeSurfaceOffset;
    private float eyeGap;

    // 构造器
    public HeadEyeOffset(float headPivotLogicEyeOffset, float eyeHeightOffset, float eyeSurfaceOffset, float eyeGap) {
        this.headPivotLogicEyeOffset = headPivotLogicEyeOffset;
        this.eyeHeightOffset = eyeHeightOffset;
        this.eyeSurfaceOffset = eyeSurfaceOffset;
        this.eyeGap = eyeGap;
    }

    // Getter
    public float headPivotLogicEyeOffset() {
        return headPivotLogicEyeOffset;
    }

    public float eyeHeightOffset() {
        return eyeHeightOffset;
    }

    public float eyeSurfaceOffset() {
        return eyeSurfaceOffset;
    }

    public float eyeGap() {
        return eyeGap;
    }
    public void resetPlayer(){
        this.headPivotLogicEyeOffset = DEFAULT_PLAYER.headPivotLogicEyeOffset;
        this.eyeHeightOffset = DEFAULT_PLAYER.eyeHeightOffset;
        this.eyeSurfaceOffset = DEFAULT_PLAYER.eyeSurfaceOffset;
        this.eyeGap = DEFAULT_PLAYER.eyeGap;
    }
    // Setter
    public void setHeadPivotLogicEyeOffset(float headPivotLogicEyeOffset) {
        this.headPivotLogicEyeOffset = headPivotLogicEyeOffset;
    }

    public void setEyeHeight(float eyeHeightOffset) {
        this.eyeHeightOffset = eyeHeightOffset;
    }

    public void setEyeSurfaceOffset(float eyeSurfaceOffset) {
        this.eyeSurfaceOffset = eyeSurfaceOffset;
    }

    public void setEyeGap(float eyeGap) {
        this.eyeGap = eyeGap;
    }


    private static final Map<EntityType<?>, HeadEyeOffset> DEFAULTS = new HashMap<>();

    public static void updateDefaults(Map<EntityType<?>, HeadEyeOffset> map) {
        DEFAULTS.clear();
        DEFAULTS.putAll(map);
    }

    public static HeadEyeOffset getOffset(LivingEntity entity) {
        if (entity instanceof Player) return entity.getData(AttachmentTypesJCT.EYE_OFFSET);
        else return DEFAULTS.getOrDefault(entity.getType(), getDefaultPlayer());
    }

    public static HeadEyeOffset getDefaultPlayer() {
        return DEFAULT_PLAYER.copy();
    }

    public HeadEyeOffset copy() {
        return new HeadEyeOffset(this.headPivotLogicEyeOffset, this.eyeHeightOffset, this.eyeSurfaceOffset, this.eyeGap);
    }

    /**
     * 双眼真实位置
     */
    public static List<Vec3> getEyesPosition(LivingEntity entity, float partialTick) {
        HeadEyeOffset offset = getOffset(entity);
        if (offset == null) return List.of(entity.getEyePosition(partialTick), entity.getEyePosition(partialTick));
        Vec3 position = entity.position();
        Vec3 headPosition = position.add(0,entity.getEyeHeight()+ offset.headPivotLogicEyeOffset, 0);
        return List.of(
                headPosition.add(RotUtils.rotateYX(offset.eyeGap, offset.eyeHeightOffset, offset.eyeSurfaceOffset,
                        entity.getViewYRot(partialTick), entity.getViewXRot(partialTick))),
                headPosition.add(RotUtils.rotateYX(-offset.eyeGap, offset.eyeHeightOffset, offset.eyeSurfaceOffset,
                        entity.getViewYRot(partialTick), entity.getViewXRot(partialTick))));
    }
    /**
     * 相对于逻辑眼睛位置的真实眼睛表面位置
     */
    public static Vec3 getEyePosition(HeadEyeOffset offset, LivingEntity entity, float partialTick,float eyeGap) {
        Vec3 position = entity.position();
        Vec3 headPosition = position.add(0,entity.getEyeHeight()+ offset.headPivotLogicEyeOffset, 0);
        return headPosition.add(RotUtils.rotateYX(0, offset.eyeHeightOffset, offset.eyeSurfaceOffset,
                entity.getViewYRot(partialTick), entity.getViewXRot(partialTick)));
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        HeadEyeOffset eyeOffset = (HeadEyeOffset) o;
        return Float.compare(headPivotLogicEyeOffset, eyeOffset.headPivotLogicEyeOffset) == 0 && Float.compare(eyeHeightOffset, eyeOffset.eyeHeightOffset) == 0 && Float.compare(eyeSurfaceOffset, eyeOffset.eyeSurfaceOffset) == 0 && Float.compare(eyeGap, eyeOffset.eyeGap) == 0;
    }

    @Override
    public int hashCode() {
        int result = Float.hashCode(headPivotLogicEyeOffset);
        result = 31 * result + Float.hashCode(eyeHeightOffset);
        result = 31 * result + Float.hashCode(eyeSurfaceOffset);
        result = 31 * result + Float.hashCode(eyeGap);
        return result;
    }

    @Override
    public String toString() {
        return "HeadEyeOffset{" +
                "headPivotLogicEyeOffset=" + headPivotLogicEyeOffset +
                ", eyeHeightOffset=" + eyeHeightOffset +
                ", eyeSurfaceOffset=" + eyeSurfaceOffset +
                ", eyeGap=" + eyeGap +
                '}';
    }
}