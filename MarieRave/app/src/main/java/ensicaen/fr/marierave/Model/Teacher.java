package ensicaen.fr.marierave.Model;

import java.util.ArrayList;
import java.util.List;

public class Teacher
{
	private Integer _id;
	private String _imagePath;
	private String _name;
	private String _firstname;
	private String _idConnection;
	private String _password;
	
	private List<Classroom> _classrooms = new ArrayList<>();
	
	public Teacher(String name, String firstname)
	{
		_name = name;
		_firstname = firstname;
	}
	
	public Teacher(String name, String firstname, String idConnection, String password)
	{
		_name = name;
		_firstname = firstname;
		_idConnection = idConnection;
		_password = password;
	}
	
	public Teacher(Integer id, String name, String firstname, String idConnection, String password)
	{
		_id = id;
		_name = name;
		_firstname = firstname;
		_idConnection = idConnection;
		_password = password;
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
	
	public String getIdConnection()
	{
		return _idConnection;
	}
	
	public String getPassword()
	{
		return _password;
	}
}
