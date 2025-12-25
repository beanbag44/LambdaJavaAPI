package com.lambda.java.api;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LambdaJavaApi implements ClientModInitializer {
    final String MOD_NAME = "Lambda Java API";
    final String MOD_ID = "lambda-java-api";
    final String SYMBOL = "λ";

    String VERSION = FabricLoader.getInstance()
            .getModContainer(MOD_ID).orElseThrow()
            .getMetadata().getVersion().getFriendlyString();

    Logger LOG = LogManager.getLogger(SYMBOL);

    @Override
    public void onInitializeClient() {
        LOG.info("Plugin $MOD_NAME $VERSION initialized.");
    }
}
