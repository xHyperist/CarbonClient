package com.carbonclient.visual;

import com.carbonclient.event.EventBus;
import com.carbonclient.event.EventListener;
import com.carbonclient.event.impl.ClientTickEvent;
import com.carbonclient.visual.impl.FullbrightVisual;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public final class VisualManager {

    private final FullbrightVisual fullbright = new FullbrightVisual();
    private final EventListener<ClientTickEvent> tickListener =
        new EventListener<ClientTickEvent>() {
            @Override
            public void onEvent(ClientTickEvent event) {
                fullbright.onTick();
            }
        };

    public VisualManager(EventBus eventBus) {
        if (eventBus == null) {
            throw new IllegalArgumentException("EventBus cannot be null.");
        }
        eventBus.subscribe(ClientTickEvent.class, tickListener);
    }

    public FullbrightVisual getFullbright() {
        return fullbright;
    }

    public void resetAllToDefaults() {
        fullbright.resetToDefaults();
    }

    public JsonObject createSnapshot() {
        JsonObject visuals = new JsonObject();
        visuals.add("fullbright", fullbright.serialize());
        return visuals;
    }

    public JsonObject createDefaultSnapshot() {
        JsonObject visuals = new JsonObject();
        visuals.add("fullbright", fullbright.serializeDefaults());
        return visuals;
    }

    public void applySnapshot(JsonObject visuals) {
        resetAllToDefaults();
        if (visuals == null) {
            return;
        }
        JsonElement fullbrightElement = visuals.get("fullbright");
        if (fullbrightElement != null && fullbrightElement.isJsonObject()) {
            fullbright.load(fullbrightElement.getAsJsonObject());
        }
    }

    public void shutdown() {
        fullbright.restoreOriginalGamma();
    }
}
