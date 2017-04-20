package mockuebung;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class AerzteService {
	
	private String restHostUrl = "aerzteservice.de";
	private int restHostPort = 80;
	private Socket socket;
	
	public boolean connect() {
		try {
			socket = new Socket(restHostUrl, restHostPort);
		} catch (IOException e) {
			return false;
		}
		return true;
	}
	
	public GeoData getGeoData(Arzt arzt) {
		String httpString = "GET /arzt/" + arzt.getLan() + "/GeoData HTTP/1.1\r\n\r\n";
		try {
			socket.getOutputStream().write(httpString.getBytes());
			Scanner scanner = new Scanner(socket.getInputStream());
			String line = scanner.nextLine();
			while(!line.equals(""))
				line = scanner.nextLine();
			line = scanner.nextLine();
			String[] geoDataString = line.split(" ");
			double latitude = Double.valueOf(geoDataString[0]);
			double longitude = Double.valueOf(geoDataString[1]);
			GeoData geoData = new GeoData(latitude, longitude);
			scanner.close();
			return geoData;
		} catch (IOException e) {
		}
		return new GeoData(0.0, 0.0);
	}
	
	public String getEMailAdress(Arzt arzt) {
		return arzt.getVorname() + "." + arzt.getNachname() + "@" + restHostUrl;
	}
	
	public void disconnect() {
		try {
			socket.close();
		} catch (IOException e) {
		}
	}

}
