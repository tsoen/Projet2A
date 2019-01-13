package ensicaen.fr.marierave.Model;

public class Skillheader
{
	private String _name;
	private String _subject;
	
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
}
