import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class assn1checker
{
	public static void main ( String args [])
	{
		BufferedReader br = null;
		RoutingMapTree r = new RoutingMapTree();

		try {
			String actionString;
			br = new BufferedReader(new FileReader("actions2.txt"));

			while ((actionString = br.readLine()) != null) {
				String s = r.performAction(actionString);
				if(s.equals(""))
					continue;
				System.out.println(s);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

	}
}
