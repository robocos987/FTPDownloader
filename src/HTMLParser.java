import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Document.OutputSettings;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;

public class HTMLParser {
//	Lines of the parsed file and the file object itself
	List<String> lines;
	File file;
	
//Constructor
	public HTMLParser(File file) throws Exception {
		this.file = file;
		removeTags(file);
		getLines(file);
	}

/*
 * Parses HTML
 * Removes all html tags and keeps the values of <a>(link) tags.
 * Requires the HTML file to read from
 */
	@SuppressWarnings("unused")
	private void removeTags(File file)
	{
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(Main.id + ".html"));
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();
			System.out.println("reading..");
			while (line != null) {
				sb.append(line);
				sb.append(System.lineSeparator());
				line = br.readLine();
			}
			String everything = sb.toString();

			Document doc = Jsoup.parse(everything);
			PrintWriter writer = new PrintWriter(Main.id + ".html");

			String text = doc.text().replaceAll(" ", System.getProperty("line.separator"));

			writer.write(text);
			writer.close();

			System.out.println(doc.text());
		}catch(Exception e)
		{
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

/*
 * Reads the document after being parsed. 
 * Downloads files by reading the file name on each line.
 */
	private void getLines(File file)
	{

		try {
			List<String> lines = FileUtils.readLines(file);
			this.lines = lines;
			for(int i = 0; i < lines.size(); i++)
			{
				file = new File(lines.get(i));
				if(file.getName().contains("."))
				{
					file.createNewFile();
					String link = Main.dl + file.getName();					
					System.out.println("Downloading: " + file.getName());
					Main.file(link, file);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}