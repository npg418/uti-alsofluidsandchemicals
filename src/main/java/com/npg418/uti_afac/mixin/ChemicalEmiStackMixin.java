package com.npg418.uti_afac.mixin;

import com.npg418.uti_afac.util.TooltipAppender;
import mekanism.api.chemical.Chemical;
import mekanism.client.recipe_viewer.emi.ChemicalEmiStack;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(ChemicalEmiStack.class)
public class ChemicalEmiStackMixin {
    @Shadow
    @Final
    private Chemical chemical;

    @Inject(
            method = "getTooltipText",
            at = @At("RETURN"),
            cancellable = true
    )
    public void appendSecondaryLanguageToTooltip(CallbackInfoReturnable<List<Component>> cir) {
        List<Component> tooltips = cir.getReturnValue();
        TooltipAppender.appendTooltip(tooltips, () -> chemical.getTextComponent());
        cir.setReturnValue(tooltips);
    }
}
