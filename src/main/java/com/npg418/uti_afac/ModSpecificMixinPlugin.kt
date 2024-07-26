package com.npg418.uti_afac

import net.neoforged.fml.loading.LoadingModList
import org.objectweb.asm.tree.ClassNode
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin
import org.spongepowered.asm.mixin.extensibility.IMixinInfo

class ModSpecificMixinPlugin : IMixinConfigPlugin {
    companion object {
        val mixinModIdMap = mapOf(
            "BlockNameProviderMixin" to "jade",
            "FluidEmiStackMixin" to "emi",
            "ChemicalMixin" to "mekanism",
            "ChemicalEmiStackMixin" to "mekanism",
            "AMChemicalStackRendererMixin" to "appmek"
        )
    }

    override fun shouldApplyMixin(targetClassName: String, mixinClassName: String): Boolean {
        val targetModId = mixinModIdMap.filterKeys { mixinClassName.endsWith(it) }.values.firstOrNull() ?: return true
        return LoadingModList.get().getModFileById(targetModId) != null
    }


    override fun onLoad(mixinPackage: String) {
    }

    override fun getRefMapperConfig(): String? {
        return null
    }

    override fun acceptTargets(myTargets: MutableSet<String>, otherTargets: MutableSet<String>) {
    }

    override fun getMixins(): MutableList<String>? {
        return null
    }

    override fun preApply(
        targetClassName: String,
        targetClass: ClassNode,
        mixinClassName: String,
        mixinInfo: IMixinInfo
    ) {
    }

    override fun postApply(
        targetClassName: String,
        targetClass: ClassNode,
        mixinClassName: String,
        mixinInfo: IMixinInfo
    ) {
    }
}
