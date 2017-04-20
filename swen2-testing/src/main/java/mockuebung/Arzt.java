package mockuebung;

public class Arzt {
	
	private int lan;
	private String vorname;
	private String nachname;
	
	public Arzt(int lan, String vorname, String nachname) {
		this.lan = lan;
		this.vorname = vorname;
		this.nachname = nachname;
	}

	public int getLan() {
		return lan;
	}

	public String getVorname() {
		return vorname;
	}

	public String getNachname() {
		return nachname;
	}

}
