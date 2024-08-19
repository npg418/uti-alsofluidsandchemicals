package com.npg418.uti_afac.mixin;

import bre2el.uti.LanguageMapProxy;
import bre2el.uti.TranslationTextComponentHelper;
import bre2el.uti.UntranslatedItems;
import bre2el.uti.config.Config;
import bre2el.uti.proxy.ServerProxy;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import mezz.jei.api.gui.builder.ITooltipBuilder;
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
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

// TODO: replaceItemNamesをtrueにしたときtooltipの方がsubLangになる問題
@Mixin(FluidHelper.class)
public abstract class FluidHelperMixin {
    @Unique
    private static void UTI_AlsoFluidsAndChemicals$addSubLangTitle(ITooltipBuilder tooltip, LocalRef<Component> localRef, Component currentLine, Component newLine) {
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

    @Inject(
            method = "getDisplayName(Lnet/minecraftforge/fluids/FluidStack;)Lnet/minecraft/network/chat/Component;",
            at = @At("HEAD"),
            remap = false
    )
    private void switchToSubLang(FluidStack ingredient, CallbackInfoReturnable<Component> cir) {
        if (UntranslatedItems.initComplete && !ServerProxy.serverSide && Config.replaceItemNames && LanguageMapProxy.isReady()) {
            LanguageMapProxy.switchLangMap(true);
        }
    }

    @Inject(
            method = "getDisplayName(Lnet/minecraftforge/fluids/FluidStack;)Lnet/minecraft/network/chat/Component;",
            at = @At("RETURN"),
            cancellable = true,
            remap = false
    )
    private void switchToPrimeLang(FluidStack ingredient, CallbackInfoReturnable<Component> cir) {
        if (UntranslatedItems.initComplete) {
            Component itc = cir.getReturnValue();
            if (!ServerProxy.serverSide && Config.replaceItemNames && LanguageMapProxy.isReady()) {
                LanguageMapProxy.switchLangMap(false);
                if (itc.getContents() instanceof TranslatableContents) {
                    TranslationTextComponentHelper.setChgLangToTextCompo(itc, true);
                }
            }
            cir.setReturnValue(itc);
        }
    }

    @Inject(
            method = "getTooltip(Lmezz/jei/api/gui/builder/ITooltipBuilder;Lnet/minecraftforge/fluids/FluidStack;Lnet/minecraft/world/item/TooltipFlag;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lmezz/jei/api/gui/builder/ITooltipBuilder;add(Lnet/minecraft/network/chat/FormattedText;)V",
                    remap = false
            ),
            remap = false
    )
    public void appendSecondaryLanguageToTooltip(ITooltipBuilder tooltip, FluidStack ingredient, TooltipFlag tooltipFlag, CallbackInfo ci, @Local LocalRef<Component> localRef) {
        if (UntranslatedItems.initComplete && LanguageMapProxy.isReady() && Config.dispBothLanguagesOnTooltip) {
            Component currentLine = localRef.get();
            Component newLine = TranslationTextComponentHelper.deepCopyTranslatable(currentLine);
            TranslationTextComponentHelper.setChgLangToTextCompo(newLine, !Config.replaceItemNames);
            String currentStr = currentLine.getString();
            String newStr = newLine.getString();
            if (newStr.equals(currentStr)) {
                LanguageMapProxy.cancelLangMapToUs(true);
                newLine = getDisplayName(ingredient);
                LanguageMapProxy.cancelLangMapToUs(false);
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
