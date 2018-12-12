package ensicaen.fr.marierave.Model;

public class Enfant
{
	private String _class;
	private String path;
	private String name;
	private String surname;
	
	public Enfant(String path, String name, String surname)
	{
		this.path = path;
		this.name = name;
		this.surname = surname;
	}
	
	public Enfant(String path, String name, String surname, String classR)
	{
		this.path = path;
		this.name = name;
		this.surname = surname;
		_class = classR;
	}
	
	public String getPath()
	{
		return path;
	}
	
	public String getName()
	{
		return name;
	}
	
	public String getSurname()
	{
		return surname;
	}
	
	public String getClassR()
	{
		return _class;
	}
}
