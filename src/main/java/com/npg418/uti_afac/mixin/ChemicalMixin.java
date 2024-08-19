package com.npg418.uti_afac.mixin;

import bre2el.uti.LanguageMapProxy;
import bre2el.uti.TranslationTextComponentHelper;
import bre2el.uti.config.Config;
import mekanism.api.chemical.Chemical;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Chemical.class)
public class ChemicalMixin {
    @Inject(
            method = "getTextComponent",
            at = @At("RETURN"),
            cancellable = true,
            remap = false
    )
    public void replaceChemicalName(CallbackInfoReturnable<Component> cir) {
        if (Config.replaceItemNames && LanguageMapProxy.isReady()) {
            Component itc = cir.getReturnValue();
            LanguageMapProxy.switchLangMap(false);
            if (itc.getContents() instanceof TranslatableContents) {
                TranslationTextComponentHelper.setChgLangToTextCompo(itc, true);
            }
            cir.setReturnValue(itc);
        }
    }
}
