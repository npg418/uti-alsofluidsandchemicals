package com.npg418.uti_afac.mixin;

import bre2el.uti.LanguageMapProxy;
import bre2el.uti.TranslationTextComponentHelper;
import bre2el.uti.UntranslatedItems;
import bre2el.uti.config.Config;
import mekanism.api.chemical.ChemicalStack;
import mekanism.client.jei.ChemicalStackRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(ChemicalStackRenderer.class)
public class ChemicalStackRendererMixin<STACK extends ChemicalStack<?>> {

    @Unique
    private static void UTI_AlsoFluidsAndChemicals$addSubLangTitle(List<Component> tooltips, Component newLine) {
        int rowIndex = Config.replaceItemNames == Config.subLangOn1stLine ? 1 : 0;
        tooltips.add(rowIndex, newLine);
    }

    @Inject(
            method = "getTooltip(Lmekanism/api/chemical/ChemicalStack;Lnet/minecraft/world/item/TooltipFlag;)Ljava/util/List;",
            at = @At("RETURN"),
            cancellable = true,
            remap = false
    )
    private void appendSecondaryLanguageToTooltip(@NotNull STACK stack, TooltipFlag tooltipFlag, CallbackInfoReturnable<List<Component>> cir) {
        if (UntranslatedItems.initComplete && LanguageMapProxy.isReady() && Config.dispBothLanguagesOnTooltip) {
            List<Component> tooltips = cir.getReturnValue();
            Component currentLine = tooltips.get(0);
            Component newLine = TranslationTextComponentHelper.deepCopyTranslatable(currentLine);
            TranslationTextComponentHelper.setChgLangToTextCompo(newLine, !Config.replaceItemNames);
            if (!currentLine.getString().equals(newLine.getString())) {
                UTI_AlsoFluidsAndChemicals$addSubLangTitle(tooltips, newLine);
            } else {
                LanguageMapProxy.cancelLangMapToUs(true);
                newLine = stack.getTextComponent();
                LanguageMapProxy.cancelLangMapToUs(false);
                if (!currentLine.getString().equals(newLine.getString())) {
                    UTI_AlsoFluidsAndChemicals$addSubLangTitle(tooltips, newLine);
                }
            }
            cir.setReturnValue(tooltips);
        }
    }
}
