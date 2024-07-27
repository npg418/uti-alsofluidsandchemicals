package com.npg418.uti_afac.mixin;

import appeng.api.stacks.AEFluidKey;
import com.npg418.uti_afac.util.TooltipAppender;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(targets = {"appeng.init.client.InitStackRenderHandlers$FluidKeyRenderHandler"})
public class FluidKeyRenderHandlerMixin {
    @Inject(method = "getTooltip(Lappeng/api/stacks/AEFluidKey;)Ljava/util/List;", at = @At("RETURN"), cancellable = true)
    public void appendSecondaryLanguageToTooltip(AEFluidKey stack, CallbackInfoReturnable<List<Component>> cir) {
        List<Component> tooltips = cir.getReturnValue();
        TooltipAppender.appendTooltip(tooltips, () -> stack.toStack(1).getDisplayName());
        cir.setReturnValue(tooltips);
    }
}
