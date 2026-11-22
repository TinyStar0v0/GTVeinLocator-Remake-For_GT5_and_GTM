package pers.gwyog.gtveinlocator.network;

import java.util.List;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;
import pers.gwyog.gtveinlocator.GT5Scanner.FoundVein;

public class LocatorMessage implements IMessage {

    public String[] names = new String[0];
    public int[] xs = new int[0];
    public int[] ys = new int[0];
    public int[] zs = new int[0];

    public LocatorMessage() {}

    public LocatorMessage(List<FoundVein> found) {
        int size = found.size();
        names = new String[size];
        xs = new int[size];
        ys = new int[size];
        zs = new int[size];
        for (int i = 0; i < size; i++) {
            FoundVein vein = found.get(i);
            names[i] = vein.name;
            xs[i] = vein.x;
            ys[i] = vein.y;
            zs[i] = vein.z;
        }
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        int size = buf.readInt();
        names = new String[size];
        xs = new int[size];
        ys = new int[size];
        zs = new int[size];
        for (int i = 0; i < size; i++) {
            names[i] = ByteBufUtils.readUTF8String(buf);
            xs[i] = buf.readInt();
            ys[i] = buf.readInt();
            zs[i] = buf.readInt();
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(names.length);
        for (int i = 0; i < names.length; i++) {
            ByteBufUtils.writeUTF8String(buf, names[i]);
            buf.writeInt(xs[i]);
            buf.writeInt(ys[i]);
            buf.writeInt(zs[i]);
        }
    }
}
