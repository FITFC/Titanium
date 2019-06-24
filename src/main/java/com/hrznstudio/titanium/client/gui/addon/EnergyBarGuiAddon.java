/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.client.gui.addon;

import com.hrznstudio.titanium.api.client.AssetTypes;
import com.hrznstudio.titanium.api.client.IAsset;
import com.hrznstudio.titanium.client.gui.asset.IAssetProvider;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.energy.IEnergyStorage;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

public class EnergyBarGuiAddon extends BasicGuiAddon {

    private final IEnergyStorage handler;
    private IAsset background;

    public EnergyBarGuiAddon(int posX, int posY, IEnergyStorage handler) {
        super(posX, posY);
        this.handler = handler;
    }

    @Override
    public void drawGuiContainerBackgroundLayer(Screen screen, IAssetProvider provider, int guiX, int guiY, int mouseX, int mouseY, float partialTicks) {
        background = IAssetProvider.getAsset(provider, AssetTypes.ENERGY_BACKGROUND);
        Point offset = background.getOffset();
        Rectangle area = background.getArea();
        screen.getMinecraft().getTextureManager().bindTexture(background.getResourceLocation());
        screen.blit(guiX + getPosX() + offset.x, guiY + getPosY() + offset.y, area.x, area.y, area.width, area.height);
    }

    @Override
    public void drawGuiContainerForegroundLayer(Screen screen, IAssetProvider provider, int guiX, int guiY, int mouseX, int mouseY) {
        IAsset asset = IAssetProvider.getAsset(provider, AssetTypes.ENERGY_BAR);
        Point offset = asset.getOffset();
        Rectangle area = asset.getArea();
        screen.getMinecraft().getTextureManager().bindTexture(asset.getResourceLocation());
        long stored = handler.getEnergyStored();
        long capacity = handler.getMaxEnergyStored();
        int powerOffset = (int) (stored * area.height / capacity);
        screen.blit(getPosX() + offset.x, getPosY() + offset.y + area.height - powerOffset, area.x, area.y + (area.height - powerOffset), area.width, powerOffset);
    }

    @Override
    public List<String> getTooltipLines() {
        return Arrays.asList(TextFormatting.GOLD + "Power:", new DecimalFormat().format(handler.getEnergyStored()) + TextFormatting.GOLD + "/" + TextFormatting.WHITE + new DecimalFormat().format(handler.getMaxEnergyStored()) + TextFormatting.DARK_AQUA + " FE");
    }

    @Override
    public int getXSize() {
        return background != null ? background.getArea().width : 0;
    }

    @Override
    public int getYSize() {
        return background != null ? background.getArea().height : 0;
    }
}