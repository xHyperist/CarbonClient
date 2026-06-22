package com.carbonclient.event.impl;

import com.carbonclient.event.Event;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;

public final class PlayerDamageEvent extends Event {

    private final EntityPlayer player;
    private final DamageSource source;
    private final Entity attacker;

    public PlayerDamageEvent(
        EntityPlayer player,
        DamageSource source,
        Entity attacker
    ) {
        this.player = player;
        this.source = source;
        this.attacker = attacker;
    }

    public EntityPlayer getPlayer() {
        return player;
    }

    public DamageSource getSource() {
        return source;
    }

    public Entity getAttacker() {
        return attacker;
    }
}
