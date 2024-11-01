package com.npg418.uti_afac.mixin;

import bre2el.uti.UntranslatedItems;
import bre2el.uti.hook.FluidTypeHook;
import com.gregtechceu.gtceu.api.material.material.Material;
import net.minecraft.network.chat.MutableComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Material.class)
public class MaterialMixin {
    @Inject(method = "getLocalizedName", at = @At("RETURN"), cancellable = true)
    public void switchLangMapForMaterial(CallbackInfoReturnable<MutableComponent> cir) {
        if (UntranslatedItems.initComplete) {
            FluidTypeHook.getDescriptionPreHook();
            cir.setReturnValue(FluidTypeHook.getDescriptionHook(cir.getReturnValue()).copy());
        }
    }
}
