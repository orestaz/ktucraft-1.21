package net.orestas.ktucraft;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.orestas.ktucraft.entity.ModEntities;
import net.orestas.ktucraft.entity.client.TheTriarchModel;
import net.orestas.ktucraft.entity.client.TheTriarchRenderer;

public class KtuCraftClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityModelLayerRegistry.registerModelLayer(TheTriarchModel.THE_TRIARCH, TheTriarchModel::getTexturedModelData);
        EntityRendererRegistry.register(ModEntities.THE_TRIARCH, TheTriarchRenderer::new);
    }
}
