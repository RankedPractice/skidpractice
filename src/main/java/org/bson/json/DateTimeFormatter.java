/*
 * Decompiled with CFR 0.152.
 */
package org.bson.json;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalQuery;
import java.util.Calendar;
import java.util.TimeZone;

final class DateTimeFormatter {
    private static final FormatterImpl FORMATTER_IMPL;

    private static FormatterImpl loadDateTimeFormatter(String className) {
        try {
            return (FormatterImpl)Class.forName(className).getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
        }
        catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError(e);
        }
        catch (InstantiationException e) {
            throw new ExceptionInInitializerError(e);
        }
        catch (IllegalAccessException e) {
            throw new ExceptionInInitializerError(e);
        }
        catch (NoSuchMethodException e) {
            throw new ExceptionInInitializerError(e);
        }
        catch (InvocationTargetException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    static long parse(String dateTimeString) {
        return FORMATTER_IMPL.parse(dateTimeString);
    }

    static String format(long dateTime) {
        return FORMATTER_IMPL.format(dateTime);
    }

    private DateTimeFormatter() {
    }

    static {
        FormatterImpl dateTimeHelper;
        try {
            dateTimeHelper = DateTimeFormatter.loadDateTimeFormatter("org.bson.json.DateTimeFormatter$Java8DateTimeFormatter");
        }
        catch (LinkageError e) {
            dateTimeHelper = DateTimeFormatter.loadDateTimeFormatter("org.bson.json.DateTimeFormatter$JaxbDateTimeFormatter");
        }
        FORMATTER_IMPL = dateTimeHelper;
    }

    static class Java8DateTimeFormatter
    implements FormatterImpl {
        Java8DateTimeFormatter() {
        }

        @Override
        public long parse(String dateTimeString) {
            try {
                return java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME.parse((CharSequence)dateTimeString, new TemporalQuery<Instant>(){

                    @Override
                    public Instant queryFrom(TemporalAccessor temporal) {
                        return Instant.from(temporal);
                    }
                }).toEpochMilli();
            }
            catch (DateTimeParseException e) {
                throw new IllegalArgumentException(e.getMessage());
            }
        }

        @Override
        public String format(long dateTime) {
            return ZonedDateTime.ofInstant(Instant.ofEpochMilli(dateTime), ZoneId.of("Z")).format(java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        }

        static {
            try {
                Class.forName("java.time.format.DateTimeFormatter");
            }
            catch (ClassNotFoundException e) {
                throw new ExceptionInInitializerError(e);
            }
        }
    }

    static class JaxbDateTimeFormatter
    implements FormatterImpl {
        private static final Method DATATYPE_CONVERTER_PARSE_DATE_TIME_METHOD;
        private static final Method DATATYPE_CONVERTER_PRINT_DATE_TIME_METHOD;

        JaxbDateTimeFormatter() {
        }

        @Override
        public long parse(String dateTimeString) {
            try {
                return ((Calendar)DATATYPE_CONVERTER_PARSE_DATE_TIME_METHOD.invoke(null, dateTimeString)).getTimeInMillis();
            }
            catch (IllegalAccessException e) {
                throw new IllegalStateException(e);
            }
            catch (InvocationTargetException e) {
                throw (RuntimeException)e.getCause();
            }
        }

        @Override
        public String format(long dateTime) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(dateTime);
            calendar.setTimeZone(TimeZone.getTimeZone("Z"));
            try {
                return (String)DATATYPE_CONVERTER_PRINT_DATE_TIME_METHOD.invoke(null, calendar);
            }
            catch (IllegalAccessException e) {
                throw new IllegalStateException();
            }
            catch (InvocationTargetException e) {
                throw (RuntimeException)e.getCause();
            }
        }

        static {
            try {
                DATATYPE_CONVERTER_PARSE_DATE_TIME_METHOD = Class.forName("javax.xml.bind.DatatypeConverter").getDeclaredMethod("parseDateTime", String.class);
                DATATYPE_CONVERTER_PRINT_DATE_TIME_METHOD = Class.forName("javax.xml.bind.DatatypeConverter").getDeclaredMethod("printDateTime", Calendar.class);
            }
            catch (NoSuchMethodException e) {
                throw new ExceptionInInitializerError(e);
            }
            catch (ClassNotFoundException e) {
                throw new ExceptionInInitializerError(e);
            }
        }
    }

    private static interface FormatterImpl {
        public long parse(String var1);

        public String format(long var1);
    }
}

