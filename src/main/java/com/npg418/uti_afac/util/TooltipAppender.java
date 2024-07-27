package com.npg418.uti_afac.util;

import bre2el.uti.LanguageMapProxy;
import bre2el.uti.TranslationTextComponentHelper;
import bre2el.uti.config.Config;
import net.minecraft.network.chat.Component;

import java.util.List;
import java.util.function.Supplier;

public class TooltipAppender {
    public static void appendTooltip(List<Component> tooltips , Supplier<Component> newName) {
        if (LanguageMapProxy.isReady() && Config.dispBothLanguagesOnTooltip) {
            Component currentLine = tooltips.get(0);
            Component newLine = TranslationTextComponentHelper.deepCopyTranslatable(currentLine);
            TranslationTextComponentHelper.setChgLangToTextCompo(newLine, !Config.replaceItemNames);
            if (!currentLine.getString().equals(newLine.getString())) {
                addSubLangTitle(tooltips, newLine);
            } else {
                LanguageMapProxy.cancelLangMapToUs(true);
                newLine = newName.get();
                LanguageMapProxy.cancelLangMapToUs(false);
                if (!currentLine.getString().equals(newLine.getString())) {
                    addSubLangTitle(tooltips, newLine);
                }
            }
        }
    }

    private static void addSubLangTitle(List<Component> tooltips, Component newLine) {
        int rowIndex = Config.replaceItemNames == Config.subLangOn1stLine ? 1 : 0;
        tooltips.add(rowIndex, newLine);
    }
}
