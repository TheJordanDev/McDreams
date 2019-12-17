package fr.theskinter.mcdreams.utils;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;

import lombok.AccessLevel;
import lombok.Getter;

public class Time {
	
	@Getter(value=AccessLevel.PUBLIC)
	public Integer year = 0;
	@Getter(value=AccessLevel.PUBLIC)
	public Integer mounth = 0;
	@Getter(value=AccessLevel.PUBLIC)
	public Integer day = 0;
	@Getter(value=AccessLevel.PUBLIC)
	public Integer hour = 0;
	@Getter(value=AccessLevel.PUBLIC)
	public Integer minutes = 0;
	@Getter(value=AccessLevel.PUBLIC)
	public Integer seconds = 0;
	
	public Time () {
	}

	public Time setYear(Integer num) {
		year = num;
		return this;
	}
	public Time setMounth(Integer num) {
		mounth = num;
		return this;
	}
	public Time setDay(Integer num) {
		day = num;
		return this;
	}

	public Time setHour(Integer num) {
		hour = num;
		return this;
	}
	
	public Time setMinutes(Integer num) {
		minutes = num;
		return this;
	}

	public Time setSeconds(Integer num) {
		seconds = num;
		return this;
	}	
	
	public static Time getNow() {
		Time time = new Time();
		LocalDateTime ltime = LocalDateTime.now();
		ltime.atZone(ZoneId.of("Europe/Paris"));
		time.setDay(ltime.getDayOfMonth());
		time.setMounth(ltime.getMonthValue());
		time.setYear(ltime.getYear());
		time.setHour(ltime.getHour());
		time.setMinutes(ltime.getMinute());
		time.setSeconds(ltime.getSecond());
		return time;
	}
	
	public long toTicks() {
		long ret = 18000;
		ret += (getHour()) * 1000;
		ret += (getMinutes() / 60.0) * 1000;
		ret %= 24000;
		return ret;
	}
	
	public static long convertTimeToTicks(Time time) {
		long ret = 18000;
		ret += (time.getHour()) * 1000;
		ret += (time.getMinutes() / 60.0) * 1000;
		ret %= 24000;
		return ret;
	}
	
	public static Time convertTickToTime(long tick) {
		long seconds = tick / 20L;
		LocalTime timeOfDay = LocalTime.ofSecondOfDay(seconds);
		Time time = new Time();
		time.setHour(timeOfDay.getHour());
		time.setMinutes(timeOfDay.getMinute());
		time.setSeconds(timeOfDay.getSecond());
		return time;
	}
	
	public static String repairTime(Integer value) {
		if (value<10) {
			return "0"+value;
		} else {
			return ""+value;
		}
	}
	
	public static Time localDateTimeToTime(LocalTime timeOfDay) {
		Time time = new Time();
		time.setHour(timeOfDay.getHour());
		time.setMinutes(timeOfDay.getMinute());
		time.setSeconds(timeOfDay.getSecond());
		return time;
	}
	
}
