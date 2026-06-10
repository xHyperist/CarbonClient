package com.carbonclient.module.impl;

import com.carbonclient.module.Module;
import com.carbonclient.module.ModuleCategory;

public final class ExampleModule extends Module {

    public ExampleModule() {
        super(
            "Example Module",
            "A temporary module used to verify the module system.",
            ModuleCategory.MISCELLANEOUS
        );
    }
}
