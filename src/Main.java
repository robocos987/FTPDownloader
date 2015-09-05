import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;



public class Main {

	//	Client and download link
	private static HttpClient client;
	public static String dl = "http://orangememes.com/images/";

	//each download is given an id
	public static int id;
	Random rand;

	//	File which the HTML will be stored on for parsing
	private File doc;

	/*
	 * Constructor
	 */
	public Main(String baseUrl, String encoder)
	{
		//setup id and html file
		rand = new Random();
		id = rand.nextInt(2000);
		doc = new File(id + ".html");

		createClient();
		if(!doc.exists())
			downloadHTML(dl);
		else
			try {
				parseHTML(doc);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	/*
	 * Creates the client on startup or if an exception occurs
	 */
	private static void createClient()
	{
		System.getProperties().setProperty("httpclient.useragent",
				"Mozilla/4.0");
		client = new HttpClient(new MultiThreadedHttpConnectionManager());
	}

	/*
	 * Downloads the HTML file of the index page
	 */
	private void downloadHTML(String link)
	{
		URL url;
		InputStream is = null;
		BufferedReader br;
		String line;

		try {
			url = new URL(link);
			GetMethod get = new GetMethod(url.toString());
			is = url.openStream();  // throws an IOException
			br = new BufferedReader(new InputStreamReader(is));
			client.executeMethod(get);
			//Read HTML
			while ((line = br.readLine()) != null) {
				System.out.println(line);
			}
			//Write HTML to given file
			file(url.toString(), doc);
		} catch (MalformedURLException mue) {
			mue.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			try {
				if (is != null) is.close();
			} catch (IOException ioe) {
				// nothing to see here
			}
		}
	}

	/*
	 * Downloads the file of choice, given a link and a file object
	 */
	public static boolean file(String link, File file) {
		URL url;
		GetMethod get = null;
		boolean result = true;

		try {
			url = new URL(link);
			get = new GetMethod(link);
			get.setRequestHeader("User-Agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows 8.1)");

			get.setFollowRedirects(true);

			client.executeMethod(get);
			InputStream in = get.getResponseBodyAsStream();
			FileOutputStream out = new FileOutputStream(file);
			byte[] buffer = new byte[1024];
			int count = -1;
			while ((count = in.read(buffer)) != -1) {
				out.write(buffer, 0, count);
			}
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
			createClient();
			result = false;
		} finally {
			get.releaseConnection();
		}
		if(file.getName().contains(".html"))
		{
			try {
				parseHTML(file);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return result;
	}

	/*
	 * Intiates parsing the HTML file
	 */
	private static void parseHTML(File file) throws Exception
	{
		new HTMLParser(file);
	}

	public static void main(String[]args)
	{
		new Main(dl, "UTF-8");
	}

}
