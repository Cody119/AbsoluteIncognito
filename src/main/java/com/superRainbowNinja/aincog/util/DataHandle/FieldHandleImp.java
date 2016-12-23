package com.superRainbowNinja.aincog.util.DataHandle;


import com.superRainbowNinja.aincog.common.tileEntity.MachineFrameTile;

import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Created by SuperRainbowNinja on 23/12/2016.
 */
public abstract class FieldHandleImp<T, R> implements IFieldHandle<T> {
    protected final Function<T, R> getter;
    protected final BiConsumer<T, R> setter;
    protected final String name;

    public FieldHandleImp(String nameIn, Function<T, R> getterIn, BiConsumer<T, R> setterIn) {
        getter = getterIn;
        setter = setterIn;
        name = nameIn;
    }

    @Override
    public String getName() {
        return name;
    }
}
