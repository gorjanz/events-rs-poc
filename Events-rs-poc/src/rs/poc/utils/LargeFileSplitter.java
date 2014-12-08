package rs.poc.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class LargeFileSplitter {

	public static void main(String[] args) throws IOException {
		
		BufferedReader bf = new BufferedReader(new FileReader(Constants.EVENTS_DATA));

		int fileNum = 0;
		int lineReadNum = 0;
		
		File f = new File("events" + fileNum + ".csv");
		PrintWriter pw = new PrintWriter(f);
		
		String line = null;
		
		while((line = bf.readLine()) != null){
			lineReadNum++;
			
			if(lineReadNum > 300000){
				pw.flush();
				pw.close();
				
				fileNum++;
				f = new File("events" + fileNum + ".csv");
				pw = new PrintWriter(f);
				lineReadNum = 0;
				System.out.println(fileNum + "th file finished");
			}
			
			pw.write(line + "\n");
		}
		
		pw.flush();
		pw.close();
		bf.close();
		
	}
	
}
