package de.htw.berlin.polysun4diac.forte.datatypes;

import static de.htw.berlin.polysun4diac.CommonFunctionsAndConstants.*;
import static java.util.Calendar.*;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
//import java.time.LocalDateTime; // Polysun is not yet compatible with LocalDateTime. TODO: Re-add this feature when Polysun updates to Java 8 SE
//import java.time.temporal.ChronoUnit; // TODO: Re-add this feature when Polysun updates to Java 8 SE
import java.util.Calendar;

/**
 * Class for representing time stamps in the IEC 61499 DATE_AND_TIME format as represented in 4diac-RTE (FORTE).
 * Intended as an adapter between the Polysun simulation time [see IPluginController.control()] and the FORTE DATE_AND_TIME format.
 * The simulation time (seconds since the beginning of the simulation) can be passed to a DateAndTime object to set the object's value.
 * Sending a DateAndTime object to a function block on FORTE will output a DATE_AND_TIME value.
 * </p>
 * Example for the conversion of a Polysun simulationTime IPluginController.control() parameter using a DateAndTime object: </p>
 * <pre>
 * {@code
 * //Initialize DateAndTime object with the simulation start (simulationTime = 0) set to 01/01/2017 00:00:00
 * DateAndTime dt = new DateAndTime(2017);
 * dt.setSimulationTimeS(simulationTime); // Set the current simulation time.
 * //send dt to FORTE using an IForteSocket object
 * }
 * </pre>
 * 
 * @author Marc Jakobi</p>HTW Berlin</p>July 2017
 * @see de.htw.berlin.polysun4diac.forte.comm.IForteSocket
 */
public class DateAndTime implements Serializable, Comparable<DateAndTime> {
	

	private static final long serialVersionUID = -3488176424672774842L;
	/** 
	 * Minimum allowed value for {@link #mForteTime} 
	 * Represents the number of ms since 1st January 1970, 01:00:00.000
	 * This is in accordance with the numeric representation of the DATE_AND_TIME data type in FORTE
	 * */
	public static final long MINTIME = 0;
	/** Reference year **/
	public static final int REFYEAR = 1970;
	/** Reference month **/
	public static final int REFMONTH = 1;
	/** Reference day **/
	public static final int REFDAY = 1;
	/** Reference hour **/
	public static final int REFHOUR = 1;
	/** Default hour **/
	public static final int DEFHOUR = 0;
	/** Reference minute **/
	public static final int REFMIN = 0;
	/** Reference second **/
	public static final int REFSEC = 0;
	/** Reference millisecond **/
	public static final int REFMS = 0;
	/** String representing the date and time format */
	public static final String DATEFORMATSTR = "dd.MM.yyyy HH:mm:ss";
	/** Date format used for this class */
	public static final SimpleDateFormat DATEFORMAT = new SimpleDateFormat(DATEFORMATSTR);
	
	/** Reference time as LocalDateTime */
	private Calendar refCalendar = Calendar.getInstance(); //LocalDateTime.of(REFYEAR, REFMONTH, REFDAY, REFHOUR, REFMIN, REFSEC, REFNS);
	/** 
	 * Internal FORTE representation of a DateAndTime object's time value.
	 * Number of ms since {@link #MINTIME}
	 */
	private long mForteTimeInms = MINTIME;
	/** FORTE time at beginning of simulation in ms since {@link #MINTIME} */
	private long mSimulationStartInms;
	
	/**
	 * Default constructor
	 */
	public DateAndTime() {
		mSimulationStartInms = MINTIME;
	}
	
	/**
	 * Constructs a DateAndTime object and initializes the simulation start value with the number of ms since 1st January 1970, 01:00:00.000
	 */
	public DateAndTime(long simulationStartInms) {
		mSimulationStartInms = simulationStartInms;
	}
	
	/**
	 * Constructs a DateAndTime object and initializes the simulation start (simulationTime = 0) to the beginning of the year
	 */
	public DateAndTime(int year) {
		this(year, REFMONTH, REFDAY, DEFHOUR, REFMIN, REFSEC, REFMS);
	}
	
	/**
	 * Constructs a DateAndTime object and initializes the simulation start (simulationTime = 0) to the beginning of the month in a given year.
	 */
	public DateAndTime(int year, int month) {
		this(year, month, REFDAY, DEFHOUR, REFMIN, REFSEC, REFMS);
	}
	
	/**
	 * Constructs a DateAndTime object and initializes the simulation start (simulationTime = 0) according to the inputs.
	 */
	public DateAndTime(int year, int month, int day) {
		this(year, month, day, DEFHOUR, REFMIN, REFSEC, REFMS);
	}
	
	/**
	 * Constructs a DateAndTime object and initializes the simulation start (simulationTime = 0) according to the inputs.
	 */
	public DateAndTime(int year, int month, int day, int hour) {
		this(year, month, day, hour, REFMIN, REFSEC, REFMS);
	}
	
	/**
	 * Constructs a DateAndTime object and initializes the simulation start (simulationTime = 0) according to the inputs.
	 */
	public DateAndTime(int year, int month, int day, int hour, int minute) {
		this(year, month, day, hour, minute, REFSEC, REFMS);
	}
	
	/**
	 * Constructs a DateAndTime object and initializes the simulation start (simulationTime = 0) according to the inputs.
	 */
	public DateAndTime(int year, int month, int day, int hour, int minute, int second) {
		this(year, month, day, hour, minute, second, REFMS);
	}
	
	/**
	 * Constructs a DateAndTime object and initializes the simulation start (simulationTime = 0) according to the inputs.
	 */
	public DateAndTime(int year, int month, int day, int hour, int minute, int second, int millisecond) {
		Calendar thisTime = Calendar.getInstance();
		thisTime.set(year, month, day, hour, minute, second);
		thisTime.set(MILLISECOND, millisecond);
		setSimulationTime(thisTime);
	}
	
	/**
	 * Constructs a DateAndTime object and initializes the simulation start (simulationTime = 0) according to a Calendar object.
	 */
	public DateAndTime(Calendar inCalendar) {
		setSimulationTime(inCalendar);
	}
	
	 // TODO: Re-add this feature when Polysun updates to Java 8 SE
//	/**
//	 * Constructs a DateAndTime object and initializes the simulation start (simulationTime = 0) according to a LocalDateTime object.
//	 */
//	public DateAndTime(LocalDateTime inDateTime) {
//		setSimulationTimeMS((long) (ChronoUnit.MILLIS.between(getRefLocalDateTime(), (inDateTime.isBefore(getRefLocalDateTime()) ? getRefLocalDateTime() : inDateTime))));
//	}
	
	/**
	 * Constructs a DateAndTime object from a string of the format dd.MM.yyyy HH:mm:ss.
	 * @param dateStr String representing the format
	 * @throws ParseException 
	 */
	public DateAndTime(String dateStr) throws ParseException {
		Calendar cal = Calendar.getInstance();
		cal.setTime(DATEFORMAT.parse(dateStr));
		setSimulationTime(cal);
	}
	
	/**
	 * Constructs a DateAndTime object from a string of the specified format.
	 * @param dateStr String representing the format
	 * @param format Format of the date
	 * @throws ParseException 
	 * @see SimpleDateFormat
	 */
	public DateAndTime(String dateStr, String format) throws ParseException {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat f = new SimpleDateFormat(format);
		cal.setTime(f.parse(dateStr));
		setSimulationTime(cal);
	}

	/**
	 * Sets the DateAndTime obect's internal clock according to simulationTime.
	 * @param simulationTime number of seconds since the beginning of the simulation.
	 */
	public void setSimulationTimeS(int simulationTime) {
		setForteTime(getForteSimulationStart() + (long) simulationTime * KILOTOSI);
	}
	
	/**
	 * @return the time since the beginning of the simulation in seconds 
	 */
	public int getSimulationTimeS() {
		return (int) ((getForteTime() - getForteSimulationStart()) / KILOTOSI);
	}
	
	/** @return Internal FORTE representation of a DateAndTime object's time value. Number of ms since 1st January 1970, 01:00:00.000 */
	public long getForteTime() {
		return mForteTimeInms;
	}
	
	/** Sets the internal FORTE representation of a DateAndTime object's time value: The number of ms since 1st January 1970, 01:00:00.000 */
	public void setForteTime(long value) {
		mForteTimeInms = (value > MINTIME) ? value : MINTIME;
	}
	
	/**
	 * @return FORTE time at beginning of simulation in ms since 1st January 1970, 01:00:00.000
	 */
	public long getForteSimulationStart() {
		return mSimulationStartInms;
	}
	
	/** 
	 * @return Calendar object that represents the current set value of the internal FORTE clock.
	 * @see {@link #getForteSimulationStart()}
	 */
	public Calendar toCalendar() {
		Calendar cal = Calendar.getInstance();
		refCalendar.set(MONTH, 0); // If this is not done, the month is offset for some reason
		cal.setTimeInMillis(refCalendar.getTimeInMillis() + getForteTime());
		return cal;
	}
	
	 // TODO: Re-add this feature when Polysun updates to Java 8 SE
//	/** 
//	 * @return LocalDateTime object that represents the current set value of the internal FORTE clock.
//	 * @see {@link #getForteSimulationStart()}
//	 */
//	public LocalDateTime toLocalDateTime() {
//		// For some reason, plusNanos() has an offset if power is used
//		return getRefLocalDateTime().plusNanos(getForteTime() * (long) KILOTOSI * (long) KILOTOSI);
//	}

	/** Sets the internal simulation time */
	private void setSimulationTime(Calendar inCalendar) {
		refCalendar.set(REFYEAR,  REFMONTH, REFDAY, REFHOUR, REFMIN, REFSEC);
		refCalendar.set(MILLISECOND, REFMS);
		setSimulationTimeMS((inCalendar.before(refCalendar) ? 0 : inCalendar.getTimeInMillis() - refCalendar.getTimeInMillis()));
	}
	
	/** Sets the internal simulation time */
	private void setSimulationTimeMS(long simulationStartInms) {
		mSimulationStartInms = simulationStartInms;
	}
	
	 // TODO: Re-add this feature when Polysun updates to Java 8 SE
//	/** Returns a reference LocalDateTime object */
//	private LocalDateTime getRefLocalDateTime() {
//		return LocalDateTime.of(REFYEAR, REFMONTH, REFDAY, REFHOUR, REFMIN, REFSEC, REFMS/KILOTOSI/KILOTOSI);
//	}
	
	@Override
	public int compareTo(DateAndTime arg0) {
		if (getForteTime() > arg0.getForteTime()) {
			return 1;
		}
		else if (getForteTime() < arg0.getForteTime()) {
			return -1;
		}
		return 0;
	}
	
	@Override
	public String toString() {
		return DATEFORMAT.format(toCalendar().getTime()).toString();
	}
}
