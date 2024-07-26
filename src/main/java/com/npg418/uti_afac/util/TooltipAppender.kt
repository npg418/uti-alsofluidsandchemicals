package com.npg418.uti_afac.util

import bre2el.uti.LanguageMapProxy
import bre2el.uti.TranslationTextComponentHelper
import bre2el.uti.config.Config
import net.minecraft.network.chat.Component

fun appendTooltip(tooltips: MutableList<Component>, newName: () -> Component) {
    if (LanguageMapProxy.isReady() && Config.dispBothLanguagesOnTooltip) {
        val currentLine = tooltips[0]
        var newLine = TranslationTextComponentHelper.deepCopyTranslatable(currentLine)
        TranslationTextComponentHelper.setChgLangToTextCompo(newLine, !Config.replaceItemNames)
        if (currentLine.string != newLine.string) {
            addSubLangTitle(tooltips, newLine)
        } else {
            LanguageMapProxy.cancelLangMapToUs(true)
            newLine = newName()
            LanguageMapProxy.cancelLangMapToUs(false)
            if (currentLine.string != newLine.string) {
                addSubLangTitle(tooltips, newLine)
            }
        }
    }
}

fun addSubLangTitle(textLines: MutableList<Component>, newLine: Component) {
    val rowIndex = if (Config.replaceItemNames == Config.subLangOn1stLine) 0 else 1
    textLines.add(rowIndex, newLine)
}
