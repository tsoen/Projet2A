package ensicaen.fr.marierave.Model;

import java.util.ArrayList;
import java.util.List;

public class Classroom
{
	public static Integer classroomId = 0;
	
	private Integer _id;
	private String _name;
	private List<Child> _childs = new ArrayList<>();
	
	public Classroom(Integer id, String name)
	{
		_id = id;
		_name = name;
	}
	
	public Integer getId()
	{
		return _id;
	}
	
	public String getName()
	{
		return _name;
	}
	
	public List<Child> getChilds(){
		return _childs;
	}
	
	public void setChilds(List<Child> childs){
		_childs = childs;
	}
}
