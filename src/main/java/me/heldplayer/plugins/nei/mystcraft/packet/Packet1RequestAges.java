
package me.heldplayer.plugins.nei.mystcraft.packet;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.io.IOException;

import me.heldplayer.plugins.nei.mystcraft.AgeInfo;
import me.heldplayer.plugins.nei.mystcraft.CommonProxy;
import me.heldplayer.plugins.nei.mystcraft.PluginNEIMystcraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.specialattack.forge.core.packet.SpACorePacket;
import cpw.mods.fml.relauncher.Side;

public class Packet1RequestAges extends SpACorePacket {

    public Packet1RequestAges() {
        super(null);
    }

    @Override
    public Side getSendingSide() {
        return Side.CLIENT;
    }

    @Override
    public void read(ChannelHandlerContext context, ByteBuf in) throws IOException {}

    @Override
    public void write(ChannelHandlerContext context, ByteBuf out) throws IOException {}

    @Override
    public void onData(ChannelHandlerContext context, EntityPlayer player) {
        boolean addToNEI = PluginNEIMystcraft.addAgeList.getValue();
        boolean listSymbols = PluginNEIMystcraft.addAgeExplorer.getValue() && PluginNEIMystcraft.allowSymbolExploring.getValue();
        boolean listPages = PluginNEIMystcraft.addAgeExplorer.getValue() && PluginNEIMystcraft.allowPageExploring.getValue();

        boolean playerOpped = MinecraftServer.getServer().getConfigurationManager().isPlayerOpped(player.getCommandSenderName());

        if (addToNEI && PluginNEIMystcraft.opOnlyAgeList.getValue()) {
            addToNEI = playerOpped;
        }
        if (listSymbols && PluginNEIMystcraft.opOnlySymbolExplorer.getValue()) {
            listSymbols = playerOpped;
        }
        if (listPages && PluginNEIMystcraft.opOnlyPageExploring.getValue()) {
            listPages = playerOpped;
        }

        for (AgeInfo info : CommonProxy.serverAgesMap.values()) {
            PluginNEIMystcraft.packetHandler.sendPacketToPlayer(new Packet2AgeInfo(info, addToNEI, listSymbols, listPages), player);
        }
    }

}