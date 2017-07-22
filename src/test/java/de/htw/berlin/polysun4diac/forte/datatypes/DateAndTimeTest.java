package de.htw.berlin.polysun4diac.forte.datatypes;

import static org.junit.Assert.*;

//import java.time.LocalDateTime;  // TODO: Re-add this when Polysun updates to Java 8 SE

import org.junit.Before;
import org.junit.Test;

import de.htw.berlin.polysun4diac.forte.datatypes.DateAndTime;


/**
 * JUnit test cases for DateAndTime class
 * @author Marc Jakobi
 *
 */
public class DateAndTimeTest {
	
	private static final float TEST_TOLERANCE = 0.0f; 
	private static final String CHECKTIME = "01.01.2017 00:00:00";
	
	DateAndTime dt1;
	DateAndTime dt2;
	DateAndTime dt3;
//	DateAndTime dt4;  // TODO: Re-add this when Polysun updates to Java 8 SE
	
	@Before
	public void setUp() throws Exception {
		dt1 = new DateAndTime();
		dt2 = new DateAndTime(2017);
		dt3 = new DateAndTime(2017, 1, 1, 0, 0, 0, 0);
//		dt4 = new DateAndTime(LocalDateTime.of(2017, 1, 1, 0, 0, 0, 0));  // TODO: Re-add this when Polysun updates to Java 8 SE
	}
	
	@Test 
	public void validateInitializations() {
		assertEquals("Default initialization", dt1.getForteTime(), 0, TEST_TOLERANCE);
		assertEquals("Non-default initializations", dt2.getForteTime(), dt3.getForteTime(), TEST_TOLERANCE);
		assertEquals("Initialized time should be the same", dt2.compareTo(dt1), 0);
		assertTrue("Simulation start of non-default should be greater", dt2.getForteSimulationStart() > dt1.getForteSimulationStart());
		assertEquals("Simulation start times should be the same", dt2.getForteSimulationStart(), dt3.getForteSimulationStart());
//		assertEquals("Calendar and LocalDateTime initialisations", dt3.getForteTime(), dt4.getForteTime());  // TODO: Re-add this when Polysun updates to Java 8 SE
	}
	
	@Test
	public void testSetSimulationTimeS() {
		setSimulationTimes();
		assertEquals("Simulation time of -1 should increase internal clock", dt1.compareTo(dt2), -1);
		assertEquals("Simulation time of 0 should increase internal clock", dt2.compareTo(dt3), -1);
	}
	
	@Test
	public void testStringRepresentation() {
		setSimulationTimes();
		assertEquals("toString() output", CHECKTIME, dt3.toString());
	}
	
	
	
	private void setSimulationTimes() {
		dt2.setSimulationTimeS(-10);
		dt3.setSimulationTimeS(0);
	}
}
