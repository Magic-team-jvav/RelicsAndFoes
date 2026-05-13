package org.magicteam.relicsandfoes.client;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import org.magicteam.relicsandfoes.RelicsAndFoes;

@Mod(value = RelicsAndFoes.MODID, dist = Dist.CLIENT)
public final class RelicsAndFoesClient {
    public RelicsAndFoesClient(ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }
}
