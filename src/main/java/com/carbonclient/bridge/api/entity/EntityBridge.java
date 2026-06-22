package com.carbonclient.bridge.api.entity;

public interface EntityBridge {

    boolean isPlayer(Object entity);

    boolean isLiving(Object entity);

    double getPosX(Object entity);

    double getPosY(Object entity);

    double getPosZ(Object entity);

    float getEyeHeight(Object entity);

    int getEntityId(Object entity);

    int getHurtTime(Object entity);
}
