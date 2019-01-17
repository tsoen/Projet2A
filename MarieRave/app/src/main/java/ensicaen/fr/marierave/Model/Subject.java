package ensicaen.fr.marierave.Model;

public class Subject
{
	private String _name;
	
	public Subject(String name)
	{
		_name = name;
	}
	
	public String getName()
	{
		return _name;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof Subject)) {
			return false;
		}
		
		Subject o = (Subject) obj;
		return o._name.equals(_name);
	}
}
