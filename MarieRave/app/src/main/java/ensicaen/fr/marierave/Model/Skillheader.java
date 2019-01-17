package ensicaen.fr.marierave.Model;

public class Skillheader
{
	private String _name;
	private String _subject;
	
	public Skillheader(String name)
	{
		_name = name;
	}
	
	public Skillheader(String name, String subject)
	{
		_name = name;
		_subject = subject;
	}
	
	public String getName()
	{
		return _name;
	}
	
	public String getSubject()
	{
		return _subject;
	}
	
	@Override
	public String toString()
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
		if (!(obj instanceof Skillheader)) {
			return false;
		}
		
		Skillheader o = (Skillheader) obj;
		return o._name.equals(_name);
	}
}
