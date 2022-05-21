/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  net.minecraft.util.org.apache.commons.lang3.time.FastDateFormat
 */
package cc.fyre.potpvp.util;

import java.text.DecimalFormat;
import java.time.ZoneId;
import java.util.Locale;
import java.util.TimeZone;
import net.minecraft.util.org.apache.commons.lang3.time.FastDateFormat;

public class DateTimeFormats {
    public static TimeZone SERVER_TIME_ZONE = TimeZone.getTimeZone("EST");
    public static ZoneId SERVER_ZONE_ID = SERVER_TIME_ZONE.toZoneId();
    public static FastDateFormat DAY_MTH_HR_MIN_SECS = FastDateFormat.getInstance((String)"dd/MM HH:mm:ss", (TimeZone)SERVER_TIME_ZONE, (Locale)Locale.ENGLISH);
    public static FastDateFormat DAY_MTH_YR_HR_MIN_AMPM = FastDateFormat.getInstance((String)"dd/MM/yy hh:mma", (TimeZone)SERVER_TIME_ZONE, (Locale)Locale.ENGLISH);
    public static FastDateFormat DAY_MTH_HR_MIN_AMPM = FastDateFormat.getInstance((String)"dd/MM hh:mma", (TimeZone)SERVER_TIME_ZONE, (Locale)Locale.ENGLISH);
    public static FastDateFormat HR_MIN_AMPM = FastDateFormat.getInstance((String)"hh:mma", (TimeZone)SERVER_TIME_ZONE, (Locale)Locale.ENGLISH);
    public static FastDateFormat HR_MIN_AMPM_TIMEZONE = FastDateFormat.getInstance((String)"hh:mma z", (TimeZone)SERVER_TIME_ZONE, (Locale)Locale.ENGLISH);
    public static FastDateFormat HR_MIN = FastDateFormat.getInstance((String)"hh:mm", (TimeZone)SERVER_TIME_ZONE, (Locale)Locale.ENGLISH);
    public static FastDateFormat MIN_SECS = FastDateFormat.getInstance((String)"mm:ss", (TimeZone)SERVER_TIME_ZONE, (Locale)Locale.ENGLISH);
    public static FastDateFormat KOTH_FORMAT = FastDateFormat.getInstance((String)"m:ss", (TimeZone)SERVER_TIME_ZONE, (Locale)Locale.ENGLISH);
    public static ThreadLocal<DecimalFormat> REMAINING_SECONDS = new ThreadLocal<DecimalFormat>(){

        @Override
        protected DecimalFormat initialValue() {
            return new DecimalFormat("0.#");
        }
    };
    public static ThreadLocal<DecimalFormat> REMAINING_SECONDS_TRAILING = new ThreadLocal<DecimalFormat>(){

        @Override
        protected DecimalFormat initialValue() {
            return new DecimalFormat("0.0");
        }
    };
}

