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
	
	@Override
	public boolean equals(Object obj)
	{
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof Skill)) {
			return false;
		}
		
		Skill o = (Skill) obj;
		return o._code.equals(_code);
	}
}
