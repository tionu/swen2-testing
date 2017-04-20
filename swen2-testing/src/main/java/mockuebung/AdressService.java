package mockuebung;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class AdressService {
	
	private int hostPort = 80;
	private Socket socket;

	public boolean connect(String host) {
		try {
			socket = new Socket(host, hostPort);
		} catch (IOException e) {
			return false;
		}
		return true;
	}
	
	public String getAdresse(GeoData geoData) {
		String httpString = "GET /adresse?lat=" + geoData.getLatitude() + "&lon=" + geoData.getLongitude() + " HTTP/1.1\r\n\r\n";
		try {
			socket.getOutputStream().write(httpString.getBytes());
			Scanner scanner = new Scanner(socket.getInputStream());
			String line = scanner.nextLine();
			while(!line.equals(""))
				line = scanner.nextLine();
			String strasseHausnummer = scanner.nextLine();
			String plzOrt = scanner.nextLine();
			scanner.close();
			return strasseHausnummer + "\r\n" + plzOrt;
		} catch (IOException e) {
		}
		return "";
	}

	public void disconnect() {
		try {
			socket.close();
		} catch (IOException e) {
		}
	}
}
