package com.jeeva.android.converter;

import com.fasterxml.jackson.databind.ObjectReader;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;

final class JacksonResponseBodyConverter<T> implements Converter<ResponseBody, T> {

    private final ObjectReader adapter;

    private final Type mType;

    JacksonResponseBodyConverter(ObjectReader adapter, Type type) {
        this.adapter = adapter;
        this.mType = type;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        if(String.class.equals(mType)) {
            return (T) value.string();
        }
        try {
            return adapter.readValue(value.charStream());
        } finally {
            value.close();
        }
    }
}