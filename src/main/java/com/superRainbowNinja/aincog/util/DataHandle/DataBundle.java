package com.superRainbowNinja.aincog.util.DataHandle;


import com.google.common.collect.ImmutableMap;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Map;

/**
 * Created by SuperRainbowNinja on 24/12/2016.
 * TODO special method that ignores Instructions, or something? handle load checks better
 */

public class DataBundle<T> implements IFieldHandle<T> {
    protected final IFieldHandle<T>[] fields;
    private final Map<String, IFieldHandle<T>> fieldMap;

    public static <X> DataBundle<X> create(IFieldHandle<X> ... handles) {
        return new DataBundle<X>(handles);
    }

    /* I found this post to be quite informative about this warning/suppressor
     * http://stackoverflow.com/questions/12462079/potential-heap-pollution-via-varargs-parameter
    * */
    @SafeVarargs
    protected DataBundle(IFieldHandle<T> ... handles) {
        fields = handles;
        ImmutableMap.Builder<String, IFieldHandle<T>> builder = ImmutableMap.builder();
        for (IFieldHandle<T> field : fields) {
            builder.put(field.getName(), field);
        }
        fieldMap = builder.build();
    }

    public IFieldHandle<T> getFieldHandle(String name) {
        return fieldMap.get(name);
    }

    public int fieldCount() {
        return fields.length;
    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public void write(NBTTagCompound compound, T object) {
        for (IFieldHandle<T> field : fields) {
            field.write(compound, object);
        }
    }

    @Override
    public void read(NBTTagCompound compound, T object) {
        for (IFieldHandle<T> field : fields) {
            field.read(compound, object);
        }
    }

    @Override
    public Object read(NBTTagCompound tag) {
        return read(tag, new Object[fields.length]);
    }

    public Object[] read(NBTTagCompound tag, Object[] objects) {
        for (int i = 0; i < fields.length; i++) {
            objects[i] = fields[i].read(tag);
        }
        return objects;
    }

    @Override
    public void write(ByteBuf buf, T object) {
        for (IFieldHandle<T> field : fields) {
            field.write(buf, object);
        }
    }

    @Override
    public void read(ByteBuf buf, T object) {
        for (IFieldHandle<T> field : fields) {
            field.read(buf, object);
        }
    }

    @Override
    public Object read(ByteBuf buf) {
        return read(buf, new Object[fields.length]);

    }

    public Object[] read(ByteBuf buf, Object[] objects) {
        for (int i = 0; i < fields.length; i++) {
            objects[i] = fields[i].read(buf);
        }
        return objects;
    }

    @Override
    public void read(Object data, T object) {
        readFromCache((Object[])data, object);

    }

    public void readFromCache(Object[] data, T object) {
        for (int i = 0; i < fields.length; i++) {
            fields[i].read(data[i], object);
        }
    }
}
