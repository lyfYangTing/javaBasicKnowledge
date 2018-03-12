package util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateConverterUtils {

    /**
     * 转换日期为字符串
     *
     * @param date
     * @param format
     * @return
     */
    public static String formatDateToString(Date date, String format) {
        if (date != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat(format);
            return dateFormat.format(date);
        } else {
            return "";
        }
    }


    /**
     * 转换字符串为日期
     *
     * @param dateStr
     * @param format
     * @return
     * @throws ParseException
     */
    public static Date formatStringToDate(String dateStr, String format) {
        if (dateStr != null && !"".equals(dateStr)) {
            SimpleDateFormat dateFormat = new SimpleDateFormat(format);
            try {
                return dateFormat.parse(dateStr);
            } catch (ParseException e) {
                return null;
            }
        } else {
            return null;
        }
    }

    public static Date formatTimestampToDate(String timestamp) {
        try {
            return new Date(Long.parseLong(timestamp));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 转换日期为字符串
     *
     * @param date
     * @param format
     * @return
     */
    public static String formatCalendarToString(Calendar date, String format) {
        if (date != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat(format);
            return dateFormat.format(date.getTime());
        } else {
            return "";
        }
    }

    /**
     * String转Calendar
     */
    public static Calendar formatStringToCalendar(String date, String format) {
        try {
            if (date.isEmpty() || date == null)
                return null;
            SimpleDateFormat dateFormat = new SimpleDateFormat(format);
            Date dDate = dateFormat.parse(date);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dDate);
            return calendar;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * String转Calendar
     */
    public static String getCalendarDifference(Date startDate, Date endDate) {
        Calendar startTime = Calendar.getInstance();
        Calendar endTime = Calendar.getInstance();
        startTime.setTime(startDate);
        endTime.setTime(endDate);
        long minutes = getMinutesInterval(startTime, endTime);
        long day = (minutes / 60 / 24);
        minutes -= day * 60 * 24;
        long hour = minutes / 60;
        minutes -= hour * 60;
        String result = "";
        if (day > 0) {
            result = String.valueOf(day) + "天";
        }
        if (hour > 0) {
            result += String.valueOf(hour) + "小时";
        }
        result += String.valueOf(minutes) + "分钟";

        return result;
    }

    /**
     * 两个Calendar赋值；
     */
    public static Calendar getCalendar(Calendar calendar) {
        Calendar calendarInfo = Calendar.getInstance();
        calendarInfo.setTime(calendar.getTime());
        return calendarInfo;
    }

    /**
     * 得到日期
     */
    public static Date toStartTimeOfDay(Date datetime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(datetime);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static Date toEndTimeOfDay(Date datetime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(datetime);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.MILLISECOND, 59);
        return calendar.getTime();
    }

    public static Calendar getStartTimeOfDay(Calendar calendar) {
        Calendar startTimeOfDay = (Calendar) calendar.clone();
        startTimeOfDay.set(Calendar.HOUR_OF_DAY, 0);
        startTimeOfDay.set(Calendar.SECOND, 0);
        startTimeOfDay.set(Calendar.MINUTE, 0);
        startTimeOfDay.set(Calendar.MILLISECOND, 0);
        return startTimeOfDay;
    }

    /**
     * 判断未来日期B是否与日期A相差整数月
     */
    public static Boolean isSameDayOfMonth(Calendar startDate, Calendar endDate) {
        int startDayOfMonth = startDate.get(Calendar.DAY_OF_MONTH);
        int endDayOfMonth = endDate.get(Calendar.DAY_OF_MONTH);
        int maxDayOfMonth = endDate.getActualMaximum(Calendar.DAY_OF_MONTH);
        int dayOfMonth;
        if (startDayOfMonth < maxDayOfMonth)
            dayOfMonth = startDayOfMonth;
        else
            dayOfMonth = maxDayOfMonth;
        return dayOfMonth == endDayOfMonth;
    }

    public static int getMonthsInterval(Calendar startDate, Calendar endDate) {
        int startMonth = startDate.get(Calendar.YEAR) * 12
                + startDate.get(Calendar.MONTH);
        int endMonth = endDate.get(Calendar.YEAR) * 12
                + endDate.get(Calendar.MONTH);
        return endMonth - startMonth;
    }

    /**
     * 日期累加天数
     *
     * @param startDate
     * @param days
     * @return
     */
    public static Date getDaysInterval(Date startDate, int days) {
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(startDate);
        endCalendar.add(Calendar.DAY_OF_YEAR, days);
        return endCalendar.getTime();
    }

    /**
     * 日期加几个小时
     * @param startDate
     * @param hours
     * @return
     */
    public static Date getHoursAdd(Date startDate, int hours) {
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(startDate);
        endCalendar.add(Calendar.HOUR_OF_DAY, hours);
        return endCalendar.getTime();
    }

    public static int getDaysInterval(Calendar startDate, Calendar endDate) {
        long startTime = startDate
                .getTimeInMillis();
        long endTime = endDate.getTimeInMillis();
        return (int) ((endTime - startTime) / (1000 * 60 * 60 * 24));
    }

    public static int getHoursInterval(Calendar startDate, Calendar endDate) {
        long startTime = startDate
                .getTimeInMillis();
        long endTime = endDate.getTimeInMillis();
        return (int) ((endTime - startTime) / (1000 * 60 * 60));
    }

    public static int getMinutesInterval(Calendar startDate, Calendar endDate) {
        long startTime = startDate
                .getTimeInMillis();
        long endTime = endDate.getTimeInMillis();
        return (int) ((endTime - startTime) / (1000 * 60));
    }

    public static Boolean isBetweenTimeSpanInDay(Calendar time,
                                                 Calendar startTime, Calendar endTime) {
        long sd = startTime.get(Calendar.YEAR) * 365
                + startTime.get(Calendar.DAY_OF_YEAR);
        long ed = endTime.get(Calendar.YEAR) * 365
                + endTime.get(Calendar.DAY_OF_YEAR);
        if (ed > sd) {
            return true;
        } else if (ed < sd) {
            return false;
        } else {
            Calendar calendar = getCalendar(time);
            calendar.set(startTime.get(Calendar.YEAR),
                    startTime.get(Calendar.MONTH), startTime.get(Calendar.DATE));
            long s = startTime.getTimeInMillis();
            long e = endTime.getTimeInMillis();
            long t = calendar.getTimeInMillis();
            if (s <= t && t <= e)
                return true;
            return false;
        }
    }


    public static Date getStartTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static Date getEndTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }


    public static String getYesterdayStartTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.add(Calendar.DATE, -1);
        calendar.set(Calendar.MILLISECOND, 0);
        return sdf.format(calendar.getTime());
    }

    public static String getYesterdayEndTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        calendar.add(Calendar.DATE, -1);
        return sdf.format(calendar.getTime());
    }

    public static Date getYesterdayStartDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.add(Calendar.DATE, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static Date getYesterdayEndDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        calendar.add(Calendar.DATE, 0);
        return calendar.getTime();
    }

    public static Date getEndDateOfYesterday() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        calendar.add(Calendar.DATE, -1);
        return calendar.getTime();
    }

    public static String getCurrentTime() {
        SimpleDateFormat sdFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SS");
        return sdFormatter.format(new Date());
    }

    public static String getTimeOfminute() {
        SimpleDateFormat sdFormatter = new SimpleDateFormat("yyyyMMddHHmmss");
        return sdFormatter.format(new Date()).substring(0, 12);
    }

    public static Date String2Date(String string) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = sdf.parse(string);
        return date;
    }

    public static Date getCustomTime(int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY)-hour);
        calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE)-minute);
        return calendar.getTime();
    }
}
