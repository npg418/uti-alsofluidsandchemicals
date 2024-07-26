package com.npg418.uti_afac.mixin;

import dev.emi.emi.api.stack.FluidEmiStack;
import net.minecraft.nbt.CompoundTag;
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

import static com.npg418.uti_afac.util.TooltipAppenderKt.appendTooltip;

@Mixin(FluidEmiStack.class)
public class FluidEmiStackMixin {
    @Shadow
    @Final
    private Fluid fluid;

    @Shadow
    @Final
    private CompoundTag nbt;

    @Inject(
            method = "getTooltipText",
            at = @At("RETURN"),
            cancellable = true)
    private void appendSecondaryLanguageToTooltip(CallbackInfoReturnable<List<Component>> cir) {
        List<Component> tooltip = new ArrayList<>(cir.getReturnValue());
        appendTooltip(tooltip, () -> new FluidStack(fluid, 1000, nbt).getDisplayName());
        cir.setReturnValue(tooltip);
    }
}
