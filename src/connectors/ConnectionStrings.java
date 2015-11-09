/**
 * @author florinb
 *
 */
package connectors;

import android.os.Environment;

public class ConnectionStrings {

	private static ConnectionStrings instance = new ConnectionStrings();

	private String myUrl;
	private String myNamespace;
	private String myDatabase;

	private ConnectionStrings() {
		myUrl = "http://10.1.0.58/AndroidWebServices/TESTService.asmx";
		myNamespace = "http://SFATest.org/";
		myDatabase = Environment.getExternalStorageDirectory().getPath() + "/download/AndroidLRTest";
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

	public String getDatabaseName() {
		return this.myDatabase;
	}

}
