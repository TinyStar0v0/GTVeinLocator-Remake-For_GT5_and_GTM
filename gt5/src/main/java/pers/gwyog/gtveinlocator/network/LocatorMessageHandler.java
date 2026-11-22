package pers.gwyog.gtveinlocator.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import pers.gwyog.gtveinlocator.compat.JourneyMapHelper;

public class LocatorMessageHandler implements IMessageHandler<LocatorMessage, IMessage> {

    @Override
    public IMessage onMessage(LocatorMessage message, MessageContext ctx) {
        for (int i = 0; i < message.names.length; i++) {
            JourneyMapHelper.addWaypoint(message.names[i], message.xs[i], message.ys[i], message.zs[i]);
        }
        return null;
    }
}
