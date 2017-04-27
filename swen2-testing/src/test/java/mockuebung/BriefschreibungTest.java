package mockuebung;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

public class BriefschreibungTest {

	private Arzt arzt;
	private GeoData geoData;
	private String adresse = "Hummelbach 3\r\n78462 Konstanz";

	// Spy anstatt Mock, damit vorhandene Funktionalität erhalten bleibt (z.B.
	// Methode .getEMailAdress(...) für testEmailHeader())
	@Spy
	private AerzteService aerzteService;

	@Mock
	private AdressService adressService;

	@InjectMocks
	private Briefschreibung briefschreibung;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		arzt = new Arzt(12345, "Franz", "Kaiser");
		geoData = new GeoData(47.66033, 9.17582);
	}

	@Test
	public void testEmailHeader() {

		String[] resultLines = briefschreibung.emailHeader(arzt).split("\\r?\\n");

		// ...dass der E-Mail-Header richtige Daten liefert
		assertEquals(3, resultLines.length);
		assertEquals("From: my.email@guhgel.de", resultLines[0]);
		assertEquals("To: Franz.Kaiser@aerzteservice.de", resultLines[1]);
		assertEquals("Subject: Arztbrief", resultLines[2]);

		// ...dass beim E-Mail-Header keine Verbindung zum Aerzteservice
		// aufgebaut wird
		verify(aerzteService, never()).connect();

		// ein paar weitere Tests:
		verify(aerzteService, times(1)).getEMailAdress(arzt);
		verify(aerzteService, never()).getGeoData(arzt);

		verify(adressService, never());
	}

	@Test
	public void testBriefHeader() {

		doReturn(true).when(aerzteService).connect();
		doReturn(geoData).when(aerzteService).getGeoData(arzt);
		doNothing().when(aerzteService).disconnect();
		when(adressService.connect(anyObject())).thenReturn(true);
		when(adressService.getAdresse(geoData)).thenReturn(adresse);

		String[] resultLines = briefschreibung.briefHeader(arzt).split("\\r?\\n");

		// ...dass der Brief-Header richtige Daten liefert
		assertEquals(3, resultLines.length);
		assertEquals("Franz Kaiser", resultLines[0]);
		assertEquals("Hummelbach 3", resultLines[1]);
		assertEquals("78462 Konstanz", resultLines[2]);

		// ...dass die Aktionen connect - abrufen - disconnect in der richtigen
		// Reihenfolge ausgeführt werden
		InOrder orderAerzteservice = inOrder(aerzteService);
		// ...dass beim Brief-Header eine Verbindung zum Aerzteservice aufbaut
		// und diese auch wieder schließt.
		orderAerzteservice.verify(aerzteService, times(1)).connect();
		orderAerzteservice.verify(aerzteService, times(1)).getGeoData(arzt);
		orderAerzteservice.verify(aerzteService, times(1)).disconnect();

		// ...dass die Aktionen connect - abrufen - disconnect in der richtigen
		// Reihenfolge ausgeführt werden
		InOrder orderAdressService = inOrder(adressService);
		orderAdressService.verify(adressService, times(1)).connect(anyObject());
		orderAdressService.verify(adressService, times(1)).getAdresse(geoData);
		orderAdressService.verify(adressService, times(1)).disconnect();

		// weitere Tests:
		verify(aerzteService, never()).getEMailAdress(arzt);
	}

	@Test
	public void testAdressSerivceMaxConnectAttempts() {

		String adressServiceUrl = "invalidPath";
		briefschreibung.setAdressServiceHostUrl(adressServiceUrl);
		doReturn(true).when(aerzteService).connect();
		doReturn(geoData).when(aerzteService).getGeoData(arzt);
		doNothing().when(aerzteService).disconnect();

		briefschreibung.briefHeader(arzt);

		// ...dass bei einem falschen Host für den AdressService der
		// Verbindungsaufbau insgesamt 5x versucht wird, bevor der
		// Verbindungsaufbau abgebrochen wird.
		verify(adressService, times(5)).connect(adressServiceUrl);

	}

}
