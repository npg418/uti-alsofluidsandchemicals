package com.npg418.uti_afac.mixin;

import me.ramidzkh.mekae2.ae2.AMChemicalStackRenderer;
import me.ramidzkh.mekae2.ae2.MekanismKey;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

import static com.npg418.uti_afac.util.TooltipAppenderKt.appendTooltip;

@Mixin(AMChemicalStackRenderer.class)
public class AMChemicalStackRendererMixin {
    @Inject(
            method = "getTooltip(Lme/ramidzkh/mekae2/ae2/MekanismKey;)Ljava/util/List;",
            at = @At("RETURN"),
            cancellable = true
    )
    public void appendSecondaryLanguageToTooltip(MekanismKey stack, CallbackInfoReturnable<List<Component>> cir) {
        List<Component> tooltips = cir.getReturnValue();
        appendTooltip(tooltips, () -> stack.getStack().getType().getTextComponent());
        cir.setReturnValue(tooltips);
    }
}
