package com.tiers.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.tiers.TiersClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PlayerEntity.class)
public abstract class ModifyNametagsClientMixin {
    @Shadow
    public abstract String getNameForScoreboard();

    @Unique
    private int tiers_cacheVersion;

    @Unique
    private Text tiers_lastOriginal;

    @Unique
    private Text tiers_cached;

    @ModifyReturnValue(at = @At("RETURN"), method = "getDisplayName")
    private Text modifyDisplayName(Text original) {
        if (!TiersClient.toggleMod)
            return original;

        if (original == tiers_lastOriginal && tiers_cacheVersion == TiersClient.cacheVersion)
            return tiers_cached;
        tiers_cacheVersion = TiersClient.cacheVersion;
        tiers_lastOriginal = original;

        return tiers_cached = TiersClient.addGetPlayer(getNameForScoreboard(), false).getFullName(original);
    }
}