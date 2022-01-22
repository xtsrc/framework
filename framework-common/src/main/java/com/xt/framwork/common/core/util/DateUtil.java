package com.xt.framwork.common.core.util;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.IllegalInstantException;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 描述: 时间工具类
 *
 * @author tao.xiong
 */
public final class DateUtil {

    private static final Logger logger = LoggerFactory.getLogger(DateUtil.class);

    public static final DateTimeFormatter yyyy_MM_dd_HH_mm_ss_SSS = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSS");
    public static final DateTimeFormatter yyyy_MM_dd_HH_mm_ss = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
    public static final DateTimeFormatter yyyy_MM_dd_HH_mm = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");
    public static final DateTimeFormatter yyyy_MM_dd = DateTimeFormat.forPattern("yyyy-MM-dd");
    public static final DateTimeFormatter yyyyMMddHHmmss = DateTimeFormat.forPattern("yyyyMMddHHmmss");
    public static final DateTimeFormatter yyyyMMdd = DateTimeFormat.forPattern("yyyyMMdd");
    public static final DateTimeFormatter yyyyMM = DateTimeFormat.forPattern("yyyyMM");
    public static final DateTimeFormatter yyyy = DateTimeFormat.forPattern("yyyy");
    public static final DateTimeFormatter CSV_DATE = DateTimeFormat.forPattern("yyyy/MM/dd");

    /**
     * time unit
     **/
    public static final String MILLS = "MILLS";
    public static final String SECOND = "SECOND";
    public static final String MINUTE = "MINUTE";
    public static final String HOUR = "HOUR";
    public static final String DAY = "DAY";
    public static final String WEAK = "WEAK";
    public static final String MONTH = "MONTH";
    public static final String YEAR = "YEAR";

    public static final Map<String, DateTimeFormatter> dateFormatterCache = Maps.newHashMap();

    private static final DateTime DEFAULT_DATE_BOUND = DateTime.parse("1050-01-01", yyyy_MM_dd);

    public static final String INITTIME = "1001-01-01 00:00:00";

    public static final String CN_TIME_PATTERN_1 = "^昨天$";
    public static final String CN_TIME_PATTERN_2 = "^周[一二三四五六天日]$";
    public static final String CN_TIME_PATTERN_3 = "^(下午|晚上|早上|凌晨|中午|上午)\\d\\d?:\\d\\d?$";
    public static final String CN_TIME_PATTERN_4 = "^\\d\\d?月\\d\\d?日$";
    public static final String CN_TIME_PATTERN_5 = "^\\d{4}年\\d\\d?月\\d\\d?日$";
    public static final String CN_TIME_PATTERN = "^(昨天|周[一二三四五六天日]|(下午|晚上|早上|凌晨|中午|上午)\\d\\d?:\\d\\d?|\\d\\d?月\\d\\d?日|\\d{4}年\\d\\d?月\\d\\d?日)$";
    public static final Pattern CN_TIME_REG = Pattern.compile("(昨天|周[一二三四五六天日]|(下午|晚上|早上|凌晨|中午|上午)\\d\\d?:\\d\\d?|\\d\\d?月\\d\\d?日|\\d{4}年\\d\\d?月\\d\\d?日)");

    private static String getDatePattern(String date) {
        return date
                .replaceFirst("\\d{4}", "yyyy")
                .replaceFirst("\\d{2}", "MM")
                .replaceFirst("\\d{2}", "dd")
                .replaceFirst("\\d{2}", "HH")
                .replaceFirst("\\d{2}", "mm")
                .replaceFirst("\\d{2}", "ss");
    }

    public static String toString(Date date, DateTimeFormatter format) {
        if (date == null || new DateTime(date).isBefore(DEFAULT_DATE_BOUND)) {
            return "";
        }
        return new DateTime(date).toString(format);
    }

    public static Date convertDate(String dateStr, DateTimeFormatter format) {
        try {
            return format.parseDateTime(dateStr).toDate();
        } catch (IllegalInstantException e) {
            return format.withZoneUTC().parseDateTime(dateStr).toLocalDateTime().toDate();
        }
    }

    public static Date convertDate(String dateStr) {
        if (StringUtils.isBlank(dateStr)) {
            logger.info("convertDate empty date failed");
            return null;
        }
        String pattern = getDatePattern(dateStr);
        if (StringUtils.equals(pattern, dateStr)) {
            logger.info("convertDate illegal date failed, {}", dateStr);
            return null;
        }
        DateTimeFormatter formatter = dateFormatterCache.get(pattern);
        if (formatter == null) {
            formatter = DateTimeFormat.forPattern(pattern);
            dateFormatterCache.put(pattern, formatter);
        }
        try {
            return formatter.parseDateTime(dateStr).toDate();
        } catch (IllegalInstantException e) {
            return formatter.withZoneUTC().parseDateTime(dateStr).toLocalDateTime().toDate();
        }
    }

    public static Date beginTimeOfDay(Date date) {
        return new DateTime(date).millisOfDay().withMinimumValue().toDate();
    }

    public static Date endTimeOfDay(Date date) {
        return new DateTime(date).millisOfDay().withMaximumValue().toDate();
    }


    public static Date beginTimeOfMonth(Date date) {
        return new DateTime(date).dayOfMonth().withMinimumValue().millisOfDay().withMinimumValue().toDate();
    }

    public static Date endTimeOfMonth(Date date) {
        return new DateTime(date).dayOfMonth().withMaximumValue().millisOfDay().withMaximumValue().toDate();
    }

    public static Date addOffset(Date date, int offset, String unit) {
        DateTime dateTime = new DateTime(date);
        switch (unit) {
            case MILLS:
                dateTime = dateTime.plusMillis(offset);
                break;
            case SECOND:
                dateTime = dateTime.plusSeconds(offset);
                break;
            case MINUTE:
                dateTime = dateTime.plusMinutes(offset);
                break;
            case HOUR:
                dateTime = dateTime.plusHours(offset);
                break;
            case DAY:
                dateTime = dateTime.plusDays(offset);
                break;
            case WEAK:
                dateTime = dateTime.plusWeeks(offset);
                break;
            case MONTH:
                dateTime = dateTime.plusMonths(offset);
                break;
            case YEAR:
                dateTime = dateTime.plusYears(offset);
                break;
            default:
                throw new RuntimeException("unknown unit : " + unit);
        }
        return dateTime.toDate();
    }

    public static boolean isAfter(Date later, Date early) {
        if (later == null || early == null) {
            return false;
        }
        DateTime laterTime = new DateTime(later);
        DateTime earlyTime = new DateTime(early);
        return laterTime.isAfter(earlyTime);
    }

    public static boolean isBefore(Date early, Date later) {
        if (early == null || later == null) {
            return false;
        }
        DateTime earlyTime = new DateTime(early);
        DateTime laterTime = new DateTime(later);
        return earlyTime.isBefore(laterTime);
    }

    public static boolean isValid(Date date) {
        if (date == null) {
            return false;
        }
        if (DEFAULT_DATE_BOUND.toDate().compareTo(date) > 0) {
            return false;
        }
        return true;
    }

    public static String formatLong(Date date) {
        if (date == null) {
            return null;
        }
        return new DateTime(date).toString(yyyy_MM_dd_HH_mm_ss);
    }

    public static String formatYYMMDD(Date date) {
        if (date == null) {
            return null;
        }
        return new DateTime(date).toString(yyyy_MM_dd);
    }

    public static boolean isCnTime(String str) {
        return str.matches(CN_TIME_PATTERN);
    }

    public static Date convertCnDate(String str) {
        if (str.matches(CN_TIME_PATTERN_1)) {
            return addOffset(beginTimeOfDay(new Date()), -1, DAY);
        }
        if (str.matches(CN_TIME_PATTERN_2)) {
            WeekDay weekDay = WeekDay.parse(str);
            DateTime today = new DateTime(beginTimeOfDay(new Date()));
            return today.withDayOfWeek(weekDay.num).toDate();
        }
        if (str.matches(CN_TIME_PATTERN_3)) {
            str = str.replace("凌晨", "AM");
            str = str.replace("早上", "AM");
            str = str.replace("上午", "AM");
            str = str.replace("晚上", "PM");
            str = str.replace("下午", "PM");
            str = str.replace("中午", "PM");
            DateTime dateTime = DateTimeFormat.forPattern("ah:mm").parseDateTime(str);
            DateTime today = new DateTime(beginTimeOfDay(new Date()));
            return today.withHourOfDay(dateTime.hourOfDay().get()).withMinuteOfHour(dateTime.minuteOfHour().get()).toDate();
        }
        if (str.matches(CN_TIME_PATTERN_4)) {
            DateTime dateTime = DateTimeFormat.forPattern("M月d日").parseDateTime(str);
            DateTime today = new DateTime(beginTimeOfDay(new Date()));
            return today.withMonthOfYear(dateTime.getMonthOfYear()).withDayOfMonth(dateTime.dayOfMonth().get()).toDate();
        }
        if (str.matches(CN_TIME_PATTERN_5)) {
            return DateTimeFormat.forPattern("yyyy年M月d日").parseDateTime(str).toDate();
        }
        logger.error("不是有效的日志格式:{}", str);
        return null;
    }

    public static String extractCnTime(String text) {
        Matcher matcher = CN_TIME_REG.matcher(text);
        if (matcher.find()) {
            return matcher.group(0);
        }
        return null;
    }

    public enum WeekDay {
        /**
         * 周
         */
        NONE(0, "未知"),
        MO(1, "周一"),
        TU(2, "周二"),
        WE(3, "周三"),
        TH(4, "周四"),
        FR(5, "周五"),
        SA(6, "周六"),
        SU(7, "周日"),
        ;

        Integer num;

        String cn;

        WeekDay(Integer num, String cn) {
            this.num = num;
            this.cn = cn;
        }

        public static WeekDay parse(String numOrCn) {
            for (WeekDay day : values()) {
                if (StringUtils.equalsIgnoreCase(day.name(), numOrCn) || StringUtils.equalsIgnoreCase(day.cn, numOrCn)) {
                    return day;
                }
            }
            return NONE;
        }
    }
}
