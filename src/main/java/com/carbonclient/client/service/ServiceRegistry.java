package com.carbonclient.client.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class ServiceRegistry {

    private final Map<Class<?>, Object> services = new HashMap<Class<?>, Object>();

    public <T> void register(Class<T> serviceType, T service) {
        if (serviceType == null || service == null) {
            throw new IllegalArgumentException("Service type and instance cannot be null.");
        }
        if (!serviceType.isInstance(service)) {
            throw new IllegalArgumentException(
                "Service instance does not implement " + serviceType.getName()
            );
        }

        services.put(serviceType, service);
    }

    public <T> Optional<T> find(Class<T> serviceType) {
        if (serviceType == null) {
            throw new IllegalArgumentException("Service type cannot be null.");
        }

        Object service = services.get(serviceType);
        return service == null
            ? Optional.<T>empty()
            : Optional.of(serviceType.cast(service));
    }

    public <T> T require(Class<T> serviceType) {
        Optional<T> service = find(serviceType);
        if (!service.isPresent()) {
            throw new IllegalStateException(
                "No service registered for " + serviceType.getName()
            );
        }

        return service.get();
    }
}
