package com.npg418.uti_afac.mixin;

import bre2el.uti.LanguageMapProxy;
import bre2el.uti.UntranslatedItems;
import bre2el.uti.config.Config;
import com.npg418.uti_afac.util.TooltipAppender;
import mekanism.api.chemical.ChemicalStack;
import mekanism.client.jei.ChemicalStackRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(ChemicalStackRenderer.class)
public class ChemicalStackRendererMixin<STACK extends ChemicalStack<?>> {
    @Inject(
            method = "getTooltip(Lmekanism/api/chemical/ChemicalStack;Lnet/minecraft/world/item/TooltipFlag;)Ljava/util/List;",
            at = @At("RETURN"),
            cancellable = true,
            remap = false
    )
    private void appendSecondaryLanguageToTooltip(@NotNull STACK stack, TooltipFlag tooltipFlag, CallbackInfoReturnable<List<Component>> cir) {
        if (UntranslatedItems.initComplete && LanguageMapProxy.isReady() && Config.dispBothLanguagesOnTooltip) {
            List<Component> tooltips = cir.getReturnValue();
            TooltipAppender.appendTooltip(tooltips, stack::getTextComponent);
            cir.setReturnValue(tooltips);
        }
    }
}
