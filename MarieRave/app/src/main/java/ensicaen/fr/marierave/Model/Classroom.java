package ensicaen.fr.marierave.Model;

import java.util.ArrayList;
import java.util.List;

public class Classroom
{
	private String _name;
	private List<Child> _childs = new ArrayList<>();
	
	public Classroom(String name)
	{
		_name = name;
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
