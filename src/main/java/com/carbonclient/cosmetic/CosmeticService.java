package com.carbonclient.cosmetic;

public interface CosmeticService {

    boolean hasEntitlement(String cosmeticId);

    boolean isEquipped(String cosmeticId);
}
