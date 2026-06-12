package com.carbonclient.update;

import java.util.Optional;

public interface UpdateService {

    Optional<UpdateInfo> checkForUpdate(String currentVersion);
}
