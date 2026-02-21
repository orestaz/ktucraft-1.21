package net.orestas.ktucraft;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.particle.SonicBoomParticle;
import net.orestas.ktucraft.entity.ModEntities;
import net.orestas.ktucraft.entity.client.TheTriarchModel;
import net.orestas.ktucraft.entity.client.TheTriarchRenderer;
import net.orestas.ktucraft.entity.client.TheTriarchStarModel;
import net.orestas.ktucraft.entity.client.TheTriarchStarRenderer;
import net.orestas.ktucraft.network.packet.TriarchMusicClient;
import net.orestas.ktucraft.particle.ModParticles;

public class KtuCraftClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityModelLayerRegistry.registerModelLayer(TheTriarchModel.THE_TRIARCH, TheTriarchModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(TheTriarchStarModel.THE_TRIARCH_STAR, TheTriarchStarModel::getTexturedModelData);

        EntityRendererRegistry.register(ModEntities.THE_TRIARCH, TheTriarchRenderer::new);
        EntityRendererRegistry.register(ModEntities.THE_TRIARCH_STAR, TheTriarchStarRenderer::new);
        ParticleFactoryRegistry.getInstance().register(
                ModParticles.THE_TRIARCH_SONIC_BOOM,
                SonicBoomParticle.Factory::new
        );
        TriarchMusicClient.initReceivers();

    }
}
