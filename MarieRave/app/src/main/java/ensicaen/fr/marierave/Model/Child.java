package ensicaen.fr.marierave.Model;

public class Child
{
	public static Integer childId = 0;
	
	private Integer _id;
	private Integer _classroomId;
	private String _imagePath;
	private String _name;
	private String _firstname;
	
	public Child(String imagePath, String name, String firstname)
	{
		_imagePath = imagePath;
		_name = name;
		_firstname = firstname;
	}
	
	public Child(String imagePath, String name, String surname, Integer classroomId)
	{
		_imagePath = imagePath;
		_name = name;
		_firstname = surname;
		_classroomId = classroomId;
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
	
	public Integer getClassroomId()
	{
		return _classroomId;
	}
}
