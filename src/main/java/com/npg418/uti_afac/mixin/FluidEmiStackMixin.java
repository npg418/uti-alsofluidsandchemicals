package com.npg418.uti_afac.mixin;

import com.npg418.uti_afac.util.TooltipAppender;
import dev.emi.emi.api.stack.FluidEmiStack;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"UnstableApiUsage", "deprecation"})
@Mixin(FluidEmiStack.class)
public class FluidEmiStackMixin {
    @Shadow
    @Final
    private Fluid fluid;

    @Final
    @Shadow
    private DataComponentPatch componentChanges;

    @Inject(
            method = "getTooltipText",
            at = @At("RETURN"),
            cancellable = true)
    private void appendSecondaryLanguageToTooltip(CallbackInfoReturnable<List<Component>> cir) {
        List<Component> tooltip = new ArrayList<>(cir.getReturnValue());
        TooltipAppender.appendTooltip(tooltip, () -> new FluidStack(fluid.builtInRegistryHolder(), 1000, componentChanges).getHoverName());
        cir.setReturnValue(tooltip);
    }
}
