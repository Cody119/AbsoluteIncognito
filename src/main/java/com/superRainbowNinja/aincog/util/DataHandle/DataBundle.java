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
    private final Map<String, IFieldHandle> fieldMap;

    public DataBundle(IFieldHandle ... handles) {
        fields = handles;
        ImmutableMap.Builder<String, IFieldHandle> builder = ImmutableMap.builder();
        for (IFieldHandle field : fields) {
            builder.put(field.getName(), field);
        }
        fieldMap = builder.build();
    }

    public void toBytes(ByteBuf buf, T tile) {
        for (IFieldHandle field : fields) {
            field.toBytes(buf, tile);
        }
    }

    public void fromBytes(ByteBuf buf, T tile) {
        for (IFieldHandle field : fields) {
            field.fromBytes(buf, tile);
        }
    }

    public void writeNBT(NBTTagCompound compound, T tile) {
        for (IFieldHandle field : fields) {
            field.writeNBT(compound, tile);
        }
    }

    public void readNBT(NBTTagCompound compound, T tile) {
        for (IFieldHandle field : fields) {
            field.readNBT(compound, tile);
        }
    }

    public IFieldHandle<T> getFieldHandle(String name) {
        return fieldMap.get(name);
    }
}
