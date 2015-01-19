package rs.poc.main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;

import rs.poc.utils.Constants;

public class Test2ArffConvertor {

	public static void main(String[] args) throws IOException, ParseException {

		BufferedReader br = new BufferedReader(new FileReader(Constants.TRAIN_FILES_SOURCE_DIRECTORY + "testMFVWithoutLocation.csv"));
		String line;
		int index;
		int lastIndex;
		
		FileWriter fw = new FileWriter(Constants.TRAIN_FILES_SOURCE_DIRECTORY + "test.arff");
		
		while((line = br.readLine()) != null){
			index = line.indexOf(",");
			lastIndex = line.lastIndexOf(",");
			line = line.substring(index+1,lastIndex);
			index = line.indexOf(",");
			line = line.substring(index);
			fw.append(line + ",?" + "\n");
		}
		br.close();		
		fw.flush();
		fw.close();
	}
}
