package com.carbonclient.permission;

public interface PermissionService {

    String getRankId();

    boolean hasPermission(String permission);
}
