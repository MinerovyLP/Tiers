package com.tiers.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.tiers.TiersClient;
import com.tiers.profile.PlayerProfile;
import com.tiers.profile.Status;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PlayerListEntry.class)
public class ModifyTabClientMixin {
    @ModifyReturnValue(at = @At("RETURN"), method = "getDisplayName")
    private Text modifyPlayerName(Text original) {
        if (!TiersClient.toggleMod || !TiersClient.toggleTab || original == null)
            return original;

        String originalString = original.getString();

        for (PlayerProfile playerProfile : TiersClient.playerProfiles)
            if (playerProfile.status == Status.READY && (originalString.contains(playerProfile.name) || originalString.contains(playerProfile.inGameName)))
                return playerProfile.deepReplace(original);

        return original;
    }
}