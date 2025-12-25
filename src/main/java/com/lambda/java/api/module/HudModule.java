package com.lambda.java.api.module;

import com.lambda.config.settings.complex.Bind;
import com.lambda.module.tag.ModuleTag;
import org.jetbrains.annotations.NotNull;

abstract class HudModule extends com.lambda.module.HudModule {
    public HudModule(
            @NotNull String name,
            @NotNull String description,
            @NotNull ModuleTag tag,
            boolean customWindow,
            boolean alwaysListening,
            boolean enabledByDefault,
            @NotNull Bind defaultKeybind
    ) { super(name, description, tag, customWindow, alwaysListening, enabledByDefault, defaultKeybind); }

    public HudModule(
            @NotNull String name,
            @NotNull String description,
            @NotNull ModuleTag tag
    ) { super(name, description, tag, false, false, false, Bind.Companion.getEMPTY()); }
}
