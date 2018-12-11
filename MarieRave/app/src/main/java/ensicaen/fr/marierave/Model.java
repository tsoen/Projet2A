package ensicaen.fr.marierave;

public class Model
{
	
	private String sNo;
	private String product;
	private String price;
	
	public Model(String sNo, String product, String price)
	{
		this.sNo = sNo;
		this.product = product;
		this.price = price;
	}
	
	public String getsNo()
	{
		return sNo;
	}
	
	public String getProduct()
	{
		return product;
	}
	
	public String getPrice()
	{
		return price;
	}
	
}
