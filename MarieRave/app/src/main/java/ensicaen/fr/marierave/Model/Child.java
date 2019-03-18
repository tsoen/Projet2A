package ensicaen.fr.marierave.Model;

public class Child
{
	private Integer _id;
	private String _classroom;
	private String _imagePath;
	private String _name;
	private String _firstname;
	
	public Child(String name, String firstname)
	{
		_name = name;
		_firstname = firstname;
	}
	
	public Child(Integer id, String name, String firstname)
	{
		_id = id;
		_name = name;
		_firstname = firstname;
	}
	
	public Child(String name, String surname, String classroom)
	{
		_name = name;
		_firstname = surname;
		_classroom = classroom;
	}
	
	public Child(Integer id, String name, String firstname, String classroom)
	{
		_id = id;
		_name = name;
		_firstname = firstname;
		_classroom = classroom;
	}
	
	public Integer getId()
	{
		return _id;
	}
	
	public String getImagePath()
	{
		return _imagePath;
	}
	
	public String getName()
	{
		return _name;
	}
	
	public void setName(String name)
	{
		_name = name;
	}
	
	public String getFirstname()
	{
		return _firstname;
	}
	
	public void setFirstname(String firstname)
	{
		_firstname = firstname;
	}
	
	public String getClassroom()
	{
		return _classroom;
	}
	
	public void setClassroom(String classroom)
	{
		_classroom = classroom;
	}
}
