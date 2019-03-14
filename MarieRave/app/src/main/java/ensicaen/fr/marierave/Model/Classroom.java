package ensicaen.fr.marierave.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
	
	@Override
	public String toString(){
		return _name;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Classroom classroom = (Classroom) o;
		return Objects.equals(_name, classroom._name);
	}
}
