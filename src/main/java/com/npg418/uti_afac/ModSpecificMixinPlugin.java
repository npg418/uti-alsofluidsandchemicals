package com.npg418.uti_afac;

import net.neoforged.fml.loading.LoadingModList;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ModSpecificMixinPlugin implements IMixinConfigPlugin {
    private static final Map<String, String> mixinModIdMap = new HashMap<>() {
        {
            put("BlockNameProviderMixin", "jade");
            put("FluidEmiStackMixin", "emi");
            put("ChemicalMixin", "mekanism");
            put("ChemicalEmiStackMixin", "mekanism");
            put("FluidKeyRenderHandlerMixin", "ae2");
            put("AMChemicalStackRendererMixin", "appmek");
        }
    };


    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        String[] packageSec = mixinClassName.split("\\.");
        String targetModId = mixinModIdMap.get(packageSec[packageSec.length - 1]);
        if (targetModId == null) return true;
        return LoadingModList.get().getModFileById(targetModId) != null;
    }


    @Override
    public void onLoad(String mixinPackage) {

    }

    @Override
    public String getRefMapperConfig() {
        return "";
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

    }

    @Override
    public List<String> getMixins() {
        return List.of();
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }
}
