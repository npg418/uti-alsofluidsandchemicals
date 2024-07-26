package com.npg418.uti_afac.mixin;

import bre2el.uti.compat.jade.BlockNameProvider;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BlockNameProvider.class)
public class BlockNameProviderMixin {
    @Redirect(
            method = "appendTooltip",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;liquid()Z")
    )
    public boolean notExcludeLiquidBlock(BlockState instance) {
        return false;
    }
}
