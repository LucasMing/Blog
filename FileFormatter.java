package com.example.rxjavatest.java;

import android.content.Context;
import android.content.res.Resources;
import android.text.format.Formatter;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class FileFormatter {
    /** {@hide} */
    public static final int FLAG_SHORTER = 1 << 0;
    /** {@hide} */
    public static final int FLAG_CALCULATE_ROUNDED = 1 << 1;
    /** {@hide} */
    public static final int FLAG_SI_UNITS = 1 << 2;
    /** {@hide} */
    public static final int FLAG_IEC_UNITS = 1 << 3;

    public static class BytesResult {
        public final String value;
        public final String units;
        public final long roundedBytes;

        public BytesResult(String value, String units, long roundedBytes) {
            this.value = value;
            this.units = units;
            this.roundedBytes = roundedBytes;
        }
    }
    public static String formatShortFileSize(Context context, long sizeBytes) {
        if (context == null) {
            return "";
        }
        final BytesResult res = formatBytes(context.getResources(), sizeBytes,
                FLAG_SI_UNITS | FLAG_SHORTER);
        int resId = getReflactField("com.android.internal.R$string", "fileSizeSuffix");
        Method method = getReflactMethod("bidiWrap");
        /*return bidiWrap(context, context.getString(resId,
                res.value, res.units));*/
        String result = null;
        try {
            Formatter formatter = (Formatter) Class.forName("android.text.format.Formatter").newInstance();
            result = (String) method.invoke(formatter,context, context.getString(resId,
                    res.value, res.units));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return  result;
    }


    /*private static String bidiWrap(Context context, String source) {
        final Locale locale = localeFromContext(context);
        if (TextUtils.getLayoutDirectionFromLocale(locale) == View.LAYOUT_DIRECTION_RTL) {
            return BidiFormatter.getInstance(true *//* RTL*//*).unicodeWrap(source);
        } else {
            return source;
        }
    }

    private static Locale localeFromContext(Context context) {
        return context.getResources().getConfiguration().getLocales().get(0);
    }*/

    public static BytesResult formatBytes(Resources res, long sizeBytes, int flags) {
        final int unit = ((flags & FLAG_IEC_UNITS) != 0) ? 1024 : 1000;
        final boolean isNegative = (sizeBytes < 0);
        float result = isNegative ? -sizeBytes : sizeBytes;
        int suffix = getReflactField("com.android.internal.R$string", "byteShort");
        long mult = 1;
        if (result > 900) {
            suffix = getReflactField("com.android.internal.R$string", "kilobyteShort");
            mult = unit;
            result = result / unit;
        }
        if (result > 900) {
            suffix = getReflactField("com.android.internal.R$string", "megabyteShort");
            mult *= unit;
            result = result / unit;
        }
        if (result > 900) {
            suffix = getReflactField("com.android.internal.R$string", "gigabyteShort");
            mult *= unit;
            result = result / unit;
        }
        if (result > 900) {
            suffix = getReflactField("com.android.internal.R$string", "terabyteShort");
            mult *= unit;
            result = result / unit;
        }
        if (result > 900) {
            suffix = getReflactField("com.android.internal.R$string", "petabyteShort");
            mult *= unit;
            result = result / unit;
        }
        // Note we calculate the rounded long by ourselves, but still let String.format()
        // compute the rounded value. String.format("%f", 0.1) might not return "0.1" due to
        // floating point errors.
        final int roundFactor;
        final String roundFormat;
        if (mult == 1 || result >= 100) {
            roundFactor = 1;
            roundFormat = "%.0f";
        } else if (result < 1) {
            roundFactor = 100;
            roundFormat = "%.2f";
        } else if (result < 10) {
            if ((flags & FLAG_SHORTER) != 0) {
                roundFactor = 10;
                roundFormat = "%.1f";
            } else {
                roundFactor = 100;
                roundFormat = "%.2f";
            }
        } else { // 10 <= result < 100
            if ((flags & FLAG_SHORTER) != 0) {
                roundFactor = 1;
                roundFormat = "%.0f";
            } else {
                roundFactor = 100;
                roundFormat = "%.2f";
            }
        }

        if (isNegative) {
            result = -result;
        }
        final String roundedString = String.format(roundFormat, result);

        // Note this might overflow if abs(result) >= Long.MAX_VALUE / 100, but that's like 80PB so
        // it's okay (for now)...
        final long roundedBytes =
                (flags & FLAG_CALCULATE_ROUNDED) == 0 ? 0
                        : (((long) Math.round(result * roundFactor)) * mult / roundFactor);

        final String units = res.getString(suffix);

        return new BytesResult(roundedString, units, roundedBytes);
    }
    public static int getReflactField(String className,String fieldName){
        int result = -1;
        try {
            Class<?> clz = Class.forName(className);
            Field field = clz.getField(fieldName);
            field.setAccessible(true);
            result = field.getInt(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static Method getReflactMethod(String methodName){
        Method method = null;
        try {
            Class<?> clz = Class.forName("android.text.format.Formatter");
            Formatter formatter = (Formatter) clz.newInstance();
            method = formatter.getClass().getDeclaredMethod(methodName,null);
            method.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return method;
    }
}
