package com.npg418.uti_afac.mixin;

import com.npg418.uti_afac.util.TooltipAppender;
import me.ramidzkh.mekae2.ae2.AMChemicalStackRenderer;
import me.ramidzkh.mekae2.ae2.MekanismKey;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(AMChemicalStackRenderer.class)
public class AMChemicalStackRendererMixin {
    @Inject(
            method = "getTooltip(Lme/ramidzkh/mekae2/ae2/MekanismKey;)Ljava/util/List;",
            at = @At("RETURN"),
            cancellable = true
    )
    public void appendSecondaryLanguageToTooltip(MekanismKey stack, CallbackInfoReturnable<List<Component>> cir) {
        List<Component> tooltips = cir.getReturnValue();
        TooltipAppender.appendTooltip(tooltips, () -> stack.getStack().getChemical().getTextComponent());
        cir.setReturnValue(tooltips);
    }
}