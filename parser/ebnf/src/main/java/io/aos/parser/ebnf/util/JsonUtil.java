package io.aos.parser.ebnf.util;

import java.io.File;
import java.io.InputStream;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {

    private static ObjectMapper objectMapper = new ObjectMapper();

    public static <T> T fromString(final String jsonStr) {
        if (jsonStr == null) {
            return null;
        }
        try {
            return (T) objectMapper.readValue(jsonStr, String.class);
        }
        catch (Exception e) {
            return null;
        }
    }

    public static <T> T fromFile(final File file) {
        if (file == null) {
            return null;
        }
        try {
            return (T) objectMapper.readValue(file, File.class);
        }
        catch (Exception e) {
            return null;
        }
    }

    public static <T> T fromStream(final InputStream is) {
        if (is == null) {
            return null;
        }
        try {
            return (T) objectMapper.readValue(is, InputStream.class);
        }
        catch (Exception e) {
            return null;
        }
    }

    public static String toJson(Object object) {
        try {
            if (object == null) {
                return null;
            }
            return objectMapper.writeValueAsString(object);
        }
        catch (Exception e) {
            return null;
        }

    }

}
