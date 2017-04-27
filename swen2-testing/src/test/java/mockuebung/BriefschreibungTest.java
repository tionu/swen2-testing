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

		assertEquals(3, resultLines.length);
		assertEquals("From: my.email@guhgel.de", resultLines[0]);
		assertEquals("To: Franz.Kaiser@aerzteservice.de", resultLines[1]);
		assertEquals("Subject: Arztbrief", resultLines[2]);
		verify(aerzteService, times(1)).getEMailAdress(arzt);
		verify(aerzteService, never()).getGeoData(arzt);
		verify(aerzteService, never()).connect();
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

		assertEquals(3, resultLines.length);
		assertEquals("Franz Kaiser", resultLines[0]);
		assertEquals("Hummelbach 3", resultLines[1]);
		assertEquals("78462 Konstanz", resultLines[2]);
		InOrder orderAerzteservice = inOrder(aerzteService);
		orderAerzteservice.verify(aerzteService, times(1)).connect();
		orderAerzteservice.verify(aerzteService, times(1)).getGeoData(arzt);
		orderAerzteservice.verify(aerzteService, times(1)).disconnect();
		verify(aerzteService, never()).getEMailAdress(arzt);
		InOrder orderAdressService = inOrder(adressService);
		orderAdressService.verify(adressService, times(1)).connect(anyObject());
		orderAdressService.verify(adressService, times(1)).getAdresse(geoData);
		orderAdressService.verify(adressService, times(1)).disconnect();
	}

	@Test
	public void testAdressSerivceMax3ConnectAttempts() {

		String adressServiceUrl = "invalidPath";
		briefschreibung.setAdressServiceHostUrl(adressServiceUrl);
		doReturn(true).when(aerzteService).connect();
		doReturn(geoData).when(aerzteService).getGeoData(arzt);
		doNothing().when(aerzteService).disconnect();

		briefschreibung.briefHeader(arzt);

		verify(adressService, times(3)).connect(adressServiceUrl);

	}

}
