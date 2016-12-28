package com.superRainbowNinja.aincog.util.DataHandle;


import com.google.common.collect.ImmutableMap;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Map;

/**
 * Created by SuperRainbowNinja on 24/12/2016.
 */

public class DataBundle<T> {
    private final IFieldHandle<T>[] fields;
    private final Map<String, IFieldHandle<T>> fieldMap;

    public static <X> DataBundle<X> create(IFieldHandle<X> ... handles) {
        return new DataBundle<X>(handles);
    }

    /* I found this post to be quite informative about this warning/suppressor
     * http://stackoverflow.com/questions/12462079/potential-heap-pollution-via-varargs-parameter
    * */
    @SafeVarargs
    private DataBundle(IFieldHandle<T> ... handles) {
        fields = handles;
        ImmutableMap.Builder<String, IFieldHandle<T>> builder = ImmutableMap.builder();
        for (IFieldHandle<T> field : fields) {
            builder.put(field.getName(), field);
        }
        fieldMap = builder.build();
    }

    public void toBytes(ByteBuf buf, T tile) {
        for (IFieldHandle<T> field : fields) {
            field.write(buf, tile);
        }
    }

    public void fromBytes(ByteBuf buf, T tile) {
        for (IFieldHandle<T> field : fields) {
            field.read(buf, tile);
        }
    }

    public void writeNBT(NBTTagCompound compound, T tile) {
        for (IFieldHandle<T> field : fields) {
            field.write(compound, tile);
        }
    }

    public void readNBT(NBTTagCompound compound, T tile) {
        for (IFieldHandle<T> field : fields) {
            field.read(compound, tile);
        }
    }

    public IFieldHandle<T> getFieldHandle(String name) {
        return fieldMap.get(name);
    }
}
