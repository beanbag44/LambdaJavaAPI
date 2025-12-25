package com.lambda.java.api.util;

import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class Communication {
    public static void debug(@NotNull Object caller, @NotNull String message) { com.lambda.util.Communication.INSTANCE.debug(caller, message, ""); }
    public static void debug(@NotNull Object caller, @NotNull String message, @NotNull String source) { com.lambda.util.Communication.INSTANCE.debug(caller, message, source); }
    public static void info(@NotNull Object caller, @NotNull String message) { com.lambda.util.Communication.INSTANCE.info(caller, message, ""); }
    public static void info(@NotNull Object caller, @NotNull String message, @NotNull String source) { com.lambda.util.Communication.INSTANCE.info(caller, message, source); }
    public static void warn(@NotNull Object caller, @NotNull String message) { com.lambda.util.Communication.INSTANCE.warn(caller, message, ""); }
    public static void warn(@NotNull Object caller, @NotNull String message, @NotNull String source) { com.lambda.util.Communication.INSTANCE.warn(caller, message, source); }
    public static void logError(@NotNull Object caller, @NotNull String message) { com.lambda.util.Communication.INSTANCE.logError(caller, message, ""); }
    public static void logError(@NotNull Object caller, @NotNull String message, @NotNull String source) { com.lambda.util.Communication.INSTANCE.logError(caller, message, source); }
    public static void logError(@NotNull Object caller, @NotNull Throwable throwable) {
        var message = throwable.getMessage();
        logError(caller, message != null ? message : "");
    }
}
