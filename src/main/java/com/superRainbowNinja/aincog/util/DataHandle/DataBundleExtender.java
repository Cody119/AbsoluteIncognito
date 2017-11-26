package com.superRainbowNinja.aincog.util.DataHandle;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Created by SuperRainbowNinja on 27/11/2017.
 */
public class DataBundleExtender<T> extends DataBundle<T> {
    private DataBundle<T> bundle;
    private int totalLength;

    private DataBundleExtender(DataBundle<T> bundleIn, IFieldHandle<T>... handles) {
        super(handles);
        bundle = bundleIn;
        totalLength = bundle.fieldCount() + fields.length;
    }

    public int fieldCount() {
        return totalLength;
    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public void write(NBTTagCompound compound, T object) {
        bundle.write(compound, object);
        super.write(compound, object);
    }

    @Override
    public void read(NBTTagCompound compound, T object) {
        bundle.read(compound, object);
        super.read(compound, object);
    }

    @Override
    public Object read(NBTTagCompound tag) {
        return read(tag, new Object[totalLength]);
    }

    public Object[] read(NBTTagCompound tag, Object[] objects) {
        bundle.read(tag, objects);
        for (int i = bundle.fieldCount(); i < totalLength; i++) {
            objects[i] = fields[i].read(tag);
        }
        return objects;
    }

    @Override
    public void write(ByteBuf buf, T object) {
        bundle.write(buf, object);
        super.write(buf, object);
    }

    @Override
    public void read(ByteBuf buf, T object) {
        bundle.read(buf, object);
        super.read(buf, object);
    }

    @Override
    public Object read(ByteBuf buf) {
        return read(buf, new Object[totalLength]);

    }

    public Object[] read(ByteBuf buf, Object[] objects) {
        bundle.read(buf, objects);
        for (int i = bundle.fieldCount(); i < totalLength; i++) {
            objects[i] = fields[i].read(buf);
        }
        return objects;
    }

    @Override
    public void read(Object data, T object) {
        readFromCache((Object[])data, object);

    }

    public void readFromCache(Object[] data, T object) {
        bundle.readFromCache(data, object);
        for (int i = bundle.fieldCount(); i < totalLength; i++) {
            fields[i].read(data[i], object);
        }
    }
}
