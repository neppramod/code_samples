package ...util.customdatatype;

import java.util.Calendar;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;

/**
 * Used to encapsulate date<br/>
 * All timestamp generated or used in this class are divided by 1000 in unix
 * timestamp.<br/>
 * <br/>
 * Unless we specify all timestamp in this project are of this type<br/>
 * This was done because our data is represented in this type of timestamp
 * 
 * @author pramod
 * 
 */
public class Date {
	private int day;
	private int month;
	private int year;
	private int hour;
	private int minute;

	public Date() {
		this.day = 0;
		this.month = 0;
		this.year = 0;
		this.hour = 0;
		this.minute = 0;
	}

	public Date(int year, int month, int day) {
		this.day = day;
		this.month = month;
		this.year = year;
	}

	public Date(long timeStamp) {
		java.util.Date date = new java.util.Date(timeStamp * 1000L);

		DateTime dateTime = new DateTime(date);
		this.day = dateTime.getDayOfMonth();
		this.month = dateTime.getMonthOfYear();
		this.year = dateTime.getYear();
		this.hour = dateTime.getHourOfDay();
		this.minute = dateTime.getMinuteOfHour();
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getHour() {
		return hour;
	}

	public int getMinute() {
		return minute;
	}

	public long toUnixTimeStamp() {
		Calendar c = Calendar.getInstance();
		c.clear();

		c.set(year, month - 1, day, hour, minute);

		return (c.getTimeInMillis() / 1000L);
	}

	// Date always comes without giving hour and minute (so we don't have to
	// calculate for 00:00 hours)
	// E.g 2012/2/3
	public long startTimeStampOfWeek() {
		DateTime input = new DateTime(toUnixTimeStamp() * 1000L);
		DateTime startOfThisWeek = input.minusDays(input.getDayOfWeek() - 1);

		return (startOfThisWeek.getMillis() / 1000L);
	}

	/*
	 * // Get 7 days = 6 days + 23 hours + 59 seconds (*60) // To get beginning
	 * of next day add 60 seconds
	 */
	public long endTimeStampOfWeek() {
		DateMidnight first = new DateMidnight(toUnixTimeStamp() * 1000L);
		DateMidnight startOfThisWeek = first
				.minusDays(first.getDayOfWeek() - 1);
		DateMidnight endOfThisWeek = startOfThisWeek.plusDays(7);

		DateTime endDateTime = endOfThisWeek.toDateTime().minusMinutes(1);

		return (endDateTime.getMillis() / 1000L);
	}

	public long startTimeStampOfYear() {
		DateMidnight first = new DateMidnight(toUnixTimeStamp() * 1000L)
				.withMonthOfYear(1).withDayOfMonth(1);
		return (first.getMillis() / 1000L);
	}

	public long endTimeStampOfYear() {
		DateMidnight first = new DateMidnight(toUnixTimeStamp() * 1000L);
		DateMidnight last = first.plusYears(1).withMonthOfYear(1)
				.withDayOfMonth(1);

		DateTime lastDateTime = last.toDateTime().minusMinutes(1);

		return (lastDateTime.getMillis() / 1000L);
	}

	public long startTimeStampOfMonth() {
		DateMidnight first = new DateMidnight(toUnixTimeStamp() * 1000L)
				.withDayOfMonth(1);

		return (first.getMillis() / 1000L);
	}

	public long endTimeStampOfMonth() {
		DateMidnight first = new DateMidnight(toUnixTimeStamp() * 1000L)
				.withDayOfMonth(1);

		DateMidnight lastMidnight = first.plusMonths(1);

		DateTime last = lastMidnight.toDateTime().minusMinutes(1);

		// What ever you do you can't go past 11:59 PM, because our Date does
		// not have seconds
		long endTimeStamp = (last.getMillis() / 1000L);

		return endTimeStamp;
	}

	public int dayOfWeek() {
		DateTime input = new DateTime(toUnixTimeStamp() * 1000L);
		return input.getDayOfWeek();
	}

	public int dayOfMonth() {
		DateTime input = new DateTime(toUnixTimeStamp() * 1000L);
		return input.getDayOfMonth();
	}

	public int monthOfYear() {
		DateTime input = new DateTime(toUnixTimeStamp() * 1000L);
		return input.getMonthOfYear();
	}

	@Override
	public String toString() {

		String monthString = this.month + "";
		String dayString = this.day + "";

		if (this.month < 10)
			monthString = "0" + this.month;

		if (this.day < 10)
			dayString = "0" + this.day;

		return this.year + monthString + dayString;
	}
}

