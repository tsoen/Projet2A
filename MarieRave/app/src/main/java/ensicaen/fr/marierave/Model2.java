package ensicaen.fr.marierave;

public class Model2 {
	
	private String path;
	private String name;
	private String surname;
	
	public Model2(String path, String name, String surname) {
		this.path = path;
		this.name = name;
		this.surname = surname;
	}
	
	public String getPath() {
		return path;
	}
	
	public String getName() {
		return name;
	}
	
	public String getSurname() {
		return surname;
	}
}
