package com.npg418.uti_afac.mixin;

import bre2el.uti.LanguageMapProxy;
import bre2el.uti.TranslatableContentsHelper;
import bre2el.uti.UntranslatedItems;
import bre2el.uti.config.Config;
import bre2el.uti.proxy.ServerProxy;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import mezz.jei.forge.platform.FluidHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.item.TooltipFlag;
import net.minecraftforge.fluids.FluidStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

// TODO: replaceItemNamesをtrueにしたときtooltipの方がsubLangになる問題
@Mixin(FluidHelper.class)
public abstract class FluidHelperMixin {
    @Unique
    private static void UTI_AlsoFluidsAndChemicals$addSubLangTitle(List<Component> tooltip, LocalRef<Component> localRef, Component currentLine, Component newLine) {
        if (Config.replaceItemNames == Config.subLangOn1stLine) {
            tooltip.add(currentLine);
            localRef.set(newLine);
        } else {
            tooltip.add(newLine);
            localRef.set(currentLine);
        }
    }

    @Shadow(remap = false)
    public abstract Component getDisplayName(FluidStack ingredient);

    @WrapMethod(method = "getDisplayName(Lnet/minecraftforge/fluids/FluidStack;)Lnet/minecraft/network/chat/Component;", remap = false)
    private Component UTI_AlsoFluidsAndChemicals$wrapGetDisplayName(FluidStack ingredient, Operation<Component> original) {
        if (!UntranslatedItems.initComplete) {
            return original.call(ingredient);
        }
        boolean switched = !ServerProxy.serverSide && Config.replaceItemNames && LanguageMapProxy.isReady();
        if (switched) {
            LanguageMapProxy.switchLangMap(true);
        }
        Component itc;
        try {
            itc = original.call(ingredient);
        } finally {
            if (switched) {
                LanguageMapProxy.switchLangMap(false);
            }
        }
        if (switched && itc.getContents() instanceof TranslatableContents) {
            TranslatableContentsHelper.setChgLangToTextCompo(itc, true);
        }
        return itc;
    }

    @Inject(method = "getTooltip(Ljava/util/List;Lnet/minecraftforge/fluids/FluidStack;Lnet/minecraft/world/item/TooltipFlag;)V", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", remap = false), remap = false)
    public void appendSecondaryLanguageToTooltip(List<Component> tooltip, FluidStack ingredient, TooltipFlag tooltipFlag, CallbackInfo ci,
                                                 @Local(name = "displayName") LocalRef<Component> localRef) {
        if (UntranslatedItems.initComplete && LanguageMapProxy.isReady() && Config.dispBothLanguagesOnTooltip) {
            Component currentLine = localRef.get();
            Component newLine = TranslatableContentsHelper.deepCopyTranslatable(currentLine);
            TranslatableContentsHelper.setChgLangToTextCompo(newLine, !Config.replaceItemNames);
            String currentStr = currentLine.getString();
            String newStr = newLine.getString();
            if (newStr.equals(currentStr)) {
                LanguageMapProxy.cancelSwitchLangMap(true);
                newLine = getDisplayName(ingredient);
                LanguageMapProxy.cancelSwitchLangMap(false);
                newStr = newLine.getString();
                if (!newStr.equals(currentStr)) {
                    UTI_AlsoFluidsAndChemicals$addSubLangTitle(tooltip, localRef, currentLine, newLine);
                }
            } else {
                UTI_AlsoFluidsAndChemicals$addSubLangTitle(tooltip, localRef, currentLine, newLine);
            }
        }
    }
}