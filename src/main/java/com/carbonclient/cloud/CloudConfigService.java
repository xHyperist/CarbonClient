package com.carbonclient.cloud;

import java.util.Optional;

public interface CloudConfigService {

    Optional<String> downloadConfig(String profileId);

    void uploadConfig(String profileId, String configJson);
}
