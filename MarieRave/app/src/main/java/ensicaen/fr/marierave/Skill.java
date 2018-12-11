package ensicaen.fr.marierave;

public class Skill
{
	private String _code;
	private String _result;
	private String _name;
	
	public Skill(String code, String name, String result)
	{
		_code = code;
		_result = result;
		_name = name;
	}
	
	public String getCode()
	{
		return _code;
	}
	
	public String getResult()
	{
		return _result;
	}
	
	public String getName()
	{
		return _name;
	}
}
