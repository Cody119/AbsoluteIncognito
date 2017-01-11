package com.superRainbowNinja.aincog.util.DataHandle;

import com.superRainbowNinja.aincog.common.machineLogic.IMachineLogic;
import com.superRainbowNinja.aincog.common.machineLogic.MachineLogicRegistry;
import com.superRainbowNinja.aincog.util.NBTUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;

import java.util.function.BiConsumer;
import java.util.function.Function;


/**
 * Created by SuperRainbowNinja on 26/12/2016.
 */
public class LogicHandle<T> extends FieldHandleImp<T, IMachineLogic> {
    public LogicHandle(String nameIn, Function<T, IMachineLogic> getterIn, BiConsumer<T, IMachineLogic> setterIn) {
        super(nameIn, getterIn, setterIn);
    }

    @Override
    public void write(NBTTagCompound compound, T object) {
        compound.setTag(name, MachineLogicRegistry.writeLogic(new NBTTagCompound(), getter.apply(object)));
    }


    @Override
    public void write(ByteBuf buf, T object) {
        MachineLogicRegistry.serializeLogic(getter.apply(object), buf);
    }

    @Override
    IMachineLogic readObject(NBTTagCompound compound) {
        return MachineLogicRegistry.INSTANCE.readLogic(compound.getCompoundTag(name));
    }

    @Override
    IMachineLogic readObject(ByteBuf buf) {
        return MachineLogicRegistry.INSTANCE.deserializeLogic(buf);
    }
}
