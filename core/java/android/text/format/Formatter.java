/*
 * Copyright (C) 2006 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package android.text.format;

import android.content.Context;
import android.net.NetworkUtils;

/**
 * Utility class to aid in formatting common values that are not covered
 * by the {@link java.util.Formatter} class in {@link java.util}
 */
public final class Formatter {

    /**
     * Formats a content size to be in the form of bytes, kilobytes, megabytes, etc
     *
     * @param context Context to use to load the localized units
     * @param number size value to be formatted
     * @return formatted string with the number
     */
    public static String formatFileSize(Context context, long number) {
        return formatFileSize(context, number, false);
    }

    /**
     * Like {@link #formatFileSize}, but trying to generate shorter numbers
     * (showing fewer digits of precision).
     */
    public static String formatShortFileSize(Context context, long number) {
        return formatFileSize(context, number, true);
    }

    private static String formatFileSize(Context context, long number, boolean shorter) {
        if (context == null) {
            return "";
        }

        float result = number;
        int suffix = com.android.internal.R.string.byteShort;
        if (result > 900) {
            suffix = com.android.internal.R.string.kilobyteShort;
            result = result / 1024;
        }
        if (result > 900) {
            suffix = com.android.internal.R.string.megabyteShort;
            result = result / 1024;
        }
        if (result > 900) {
            suffix = com.android.internal.R.string.gigabyteShort;
            result = result / 1024;
        }
        if (result > 900) {
            suffix = com.android.internal.R.string.terabyteShort;
            result = result / 1024;
        }
        if (result > 900) {
            suffix = com.android.internal.R.string.petabyteShort;
            result = result / 1024;
        }
        String value;
        if (result < 1) {
            value = String.format("%.2f", result);
        } else if (result < 10) {
            if (shorter) {
                value = String.format("%.1f", result);
            } else {
                value = String.format("%.2f", result);
            }
        } else if (result < 100) {
            if (shorter) {
                value = String.format("%.0f", result);
            } else {
                value = String.format("%.2f", result);
            }
        } else {
            value = String.format("%.0f", result);
        }
        return context.getResources().
            getString(com.android.internal.R.string.fileSizeSuffix,
                      value, context.getString(suffix));
    }

    /**
     * Returns a string in the canonical IPv4 format ###.###.###.### from a packed integer
     * containing the IP address. The IPv4 address is expected to be in little-endian
     * format (LSB first). That is, 0x01020304 will return "4.3.2.1".
     *
     * @deprecated Use {@link java.net.InetAddress#getHostAddress()}, which supports both IPv4 and
     *     IPv6 addresses. This method does not support IPv6 addresses.
     */
    @Deprecated
    public static String formatIpAddress(int ipv4Address) {
        return NetworkUtils.intToInetAddress(ipv4Address).getHostAddress();
    }

    private static final int SECONDS_PER_MINUTE = 60;
    private static final int SECONDS_PER_HOUR = 60 * 60;
    private static final int SECONDS_PER_DAY = 24 * 60 * 60;

    /**
     * Returns elapsed time for the given millis, in the following format:
     * 1 day 5 hrs; will include at most two units, can go down to seconds precision.
     * @param context the application context
     * @param millis the elapsed time in milli seconds
     * @return the formatted elapsed time
     * @hide
     */
    public static String formatShortElapsedTime(Context context, long millis) {
        long secondsLong = millis / 1000;

        int days = 0, hours = 0, minutes = 0;
        if (secondsLong >= SECONDS_PER_DAY) {
            days = (int)(secondsLong / SECONDS_PER_DAY);
            secondsLong -= days * SECONDS_PER_DAY;
        }
        if (secondsLong >= SECONDS_PER_HOUR) {
            hours = (int)(secondsLong / SECONDS_PER_HOUR);
            secondsLong -= hours * SECONDS_PER_HOUR;
        }
        if (secondsLong >= SECONDS_PER_MINUTE) {
            minutes = (int)(secondsLong / SECONDS_PER_MINUTE);
            secondsLong -= minutes * SECONDS_PER_MINUTE;
        }
        int seconds = (int)secondsLong;

        if (days >= 2) {
            days += (hours+12)/24;
            return context.getString(com.android.internal.R.string.durationDays, days);
        } else if (days > 0) {
            if (hours == 1) {
                return context.getString(com.android.internal.R.string.durationDayHour, days, hours);
            }
            return context.getString(com.android.internal.R.string.durationDayHours, days, hours);
        } else if (hours >= 2) {
            hours += (minutes+30)/60;
            return context.getString(com.android.internal.R.string.durationHours, hours);
        } else if (hours > 0) {
            if (minutes == 1) {
                return context.getString(com.android.internal.R.string.durationHourMinute, hours,
                        minutes);
            }
            return context.getString(com.android.internal.R.string.durationHourMinutes, hours,
                    minutes);
        } else if (minutes >= 2) {
            minutes += (seconds+30)/60;
            return context.getString(com.android.internal.R.string.durationMinutes, minutes);
        } else if (minutes > 0) {
            if (seconds == 1) {
                return context.getString(com.android.internal.R.string.durationMinuteSecond, minutes,
                        seconds);
            }
            return context.getString(com.android.internal.R.string.durationMinuteSeconds, minutes,
                    seconds);
        } else if (seconds == 1) {
            return context.getString(com.android.internal.R.string.durationSecond, seconds);
        } else {
            return context.getString(com.android.internal.R.string.durationSeconds, seconds);
        }
    }
}
