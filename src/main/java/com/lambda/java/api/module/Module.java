package com.lambda.java.api.module;

import com.lambda.config.settings.complex.Bind;
import com.lambda.module.tag.ModuleTag;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
abstract class Module extends com.lambda.module.Module {
    public Module(
            @NotNull String name,
            @NotNull String description,
            @NotNull ModuleTag tag,
            boolean alwaysListening,
            boolean enabledByDefault,
            @NotNull Bind defaultKeybind,
            boolean autoDisable
    ) { super(name, description, tag, alwaysListening, enabledByDefault, defaultKeybind, autoDisable); }

    public Module(
            @NotNull String name,
            @NotNull String description,
            @NotNull ModuleTag tag
    ) { super(name, description, tag, false, false, Bind.Companion.getEMPTY(), false); }
}
