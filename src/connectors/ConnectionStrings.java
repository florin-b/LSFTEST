/**
 * @author florinb
 *
 */
package connectors;


public class ConnectionStrings {

	private static ConnectionStrings instance = new ConnectionStrings();

	private String myUrl;
	private String myNamespace;
	

	private ConnectionStrings() {
		
		//ARBSQ
		myUrl = "http://10.1.0.58/AndroidWebServices/TESTService.asmx";
		
		//IIS Test
		//myUrl = "http://10.1.0.57/AndroidWebServices/TESTService.asmx";
		
		//DR
		//myUrl = "http://172.17.18.24/AndroidWebServices/TESTService.asmx";
		
		myNamespace = "http://SFATest.org/";
		
	}

	public static ConnectionStrings getInstance() {
		return instance;
	}

	public String getUrl() {
		return this.myUrl;
	}

	public String getNamespace() {
		return this.myNamespace;
	}

	

}
