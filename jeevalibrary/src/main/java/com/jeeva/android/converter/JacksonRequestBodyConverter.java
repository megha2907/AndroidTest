package com.jeeva.android.converter;

import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Converter;

final class JacksonRequestBodyConverter<T> implements Converter<T, RequestBody> {

    private static final MediaType BYTE_MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8");

    private final ObjectWriter mObjectWriter;

    private final Type mType;

    JacksonRequestBodyConverter(ObjectWriter mObjectWriter, Type type) {
        this.mObjectWriter = mObjectWriter;
        this.mType = type;
    }

    @Override
    public RequestBody convert(T value) throws IOException {
        if(String.class.equals(mType)) {
            return RequestBody.create(BYTE_MEDIA_TYPE, value.toString().getBytes("UTF-8"));
        }

        byte[] bytes = mObjectWriter.writeValueAsBytes(value);
        return RequestBody.create(BYTE_MEDIA_TYPE, bytes);
    }
}