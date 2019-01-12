package ensicaen.fr.marierave.Model;

import java.util.ArrayList;
import java.util.List;

public class Teacher
{
	private Integer _id;
	private String _imagePath;
	private String _name;
	private String _firstname;
	
	private List<Classroom> _classrooms = new ArrayList<>();
	
	public Teacher(String name, String firstname)
	{
		_name = name;
		_firstname = firstname;
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
	
	public String getFirstname()
	{
		return _firstname;
	}
	
	public List<Classroom> getClassrooms()
	{
		return _classrooms;
	}
}
