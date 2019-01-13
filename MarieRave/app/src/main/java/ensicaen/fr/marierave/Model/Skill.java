package ensicaen.fr.marierave.Model;

public class Skill
{
	private String _code;
	private String _name;
	private String _skillHeader;
	private String _result;
	
	public Skill(String code, String name, String skillHeader)
	{
		_code = code;
		_name = name;
		_skillHeader = skillHeader;
	}
	
	public Skill(String code, String name, String skillHeader, String result)
	{
		_code = code;
		_name = name;
		_skillHeader = skillHeader;
		_result = result;
	}
	
	public String getCode()
	{
		return _code;
	}
	
	public String getName()
	{
		return _name;
	}
	
	public String getSkillheader()
	{
		return _skillHeader;
	}
	
	public String getResult()
	{
		return _result;
	}
}
