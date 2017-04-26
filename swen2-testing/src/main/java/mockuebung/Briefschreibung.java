package mockuebung;

public class Briefschreibung {
	
	private String eMailFrom = "my.email@guhgel.de";
	private AerzteService aerzteService;
	private AdressService adressService;
	private String adressServiceHostUrl;
	
	public String emailHeader(Arzt arzt) {
		String eMailTo = aerzteService.getEMailAdress(arzt);
		String header = "From: " + eMailFrom + "\r\n";
		header += "To: " + eMailTo + "\r\n";
		header += "Subject: Arztbrief\r\n";
		return header;
	}

	public String briefHeader(Arzt arzt) {
		String header = arzt.getVorname() + " " + arzt.getNachname() + "\r\n";
		boolean connectedAerzte = connectToAerzteService();
		if(!connectedAerzte)
			return "";
		GeoData geoData = aerzteService.getGeoData(arzt);
		aerzteService.disconnect();
		boolean connectedAdresse = connectToAdressService();
		if(!connectedAdresse)
			return "";
		header += adressService.getAdresse(geoData);
		adressService.disconnect();
		return header;
	}
	
	private boolean connectToAerzteService() {
		int maxCount = 3;
		int count = 0; 
		boolean connected = false;
		while (!connected && count < maxCount) {
			connected = aerzteService.connect();
			count++;
		}
		return count < maxCount;
	}
	
	private boolean connectToAdressService() {
		int maxCount = 3;
		int count = 0;
		boolean connected = false;
		while(!connected && count < maxCount) {
			connected = adressService.connect(adressServiceHostUrl);
			count++;
		}
		return count < maxCount;
	}
	
	public void setAdressServiceHostUrl(String url) {
		this.adressServiceHostUrl = url;
	}
	
	public void setAerzteService(AerzteService aerzteService) {
		this.aerzteService = aerzteService;
	}
	
	public void setAdressService(AdressService adressService) {
		this.adressService = adressService;
	}
	
}
