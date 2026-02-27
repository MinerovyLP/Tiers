package com.tiers.mixin.client;

import com.tiers.TiersClient;
import com.tiers.profile.PlayerProfile;
import com.tiers.profile.Status;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.text.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ChatHud.class)
public class ModifyChatClientMixin {
    @ModifyVariable(at = @At("HEAD"), method = "addMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSignatureData;Lnet/minecraft/client/gui/hud/MessageIndicator;)V", argsOnly = true)
    private Text addMessage(Text original) {
        if (!TiersClient.toggleMod || !TiersClient.toggleChat)
            return original;

        Text text = original;

        for (PlayerProfile playerProfile : TiersClient.playerProfiles) {
            if (playerProfile.status != Status.READY)
                continue;

            if (!text.getString().contains(playerProfile.inGameName))
                continue;

            text = playerProfile.deepReplace(text);
        }

        return text;
    }
}