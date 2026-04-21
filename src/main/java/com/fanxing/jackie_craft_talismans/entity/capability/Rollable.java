package com.fanxing.jackie_craft_talismans.entity.capability;

public interface Rollable {
    float getRoll();
    default float getRollO(){
        return 0;
    }
}
