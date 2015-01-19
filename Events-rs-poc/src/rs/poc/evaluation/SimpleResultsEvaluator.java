package rs.poc.evaluation;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.HashSet;

import rs.poc.utils.Constants;

public class SimpleResultsEvaluator {

	public static void main(String[] args) throws Exception {
		BufferedReader benchmark = new BufferedReader(new FileReader(Constants.BENCHMARK_DATA));
		HashMap<String, HashSet<String>> benchPairs = new HashMap<String, HashSet<String>>();
		
		benchmark.readLine();
		String line = null;
		String[] parts = null;
		HashSet<String> predictions = null;
		while((line=benchmark.readLine())!=null){
			line = line.replace("\"", "")
					.replace("[","")
					.replace("]", "")
					.replace("L", "")
					.replace(" ", "");
			parts = line.split(",");
			predictions = new HashSet<String>();
			for (int i = 0; i < parts.length; i++) {
				if(i==0){
					continue;
				} else {
					if(!predictions.contains(parts[i])){
						predictions.add(parts[i]);
					}
				}
			}
			benchPairs.put(parts[0], predictions);
		}
		
		benchmark.close();
		
		BufferedReader solution = new BufferedReader(new FileReader(Constants.RESULT_DATA));

		double sum = 0.0;
		int howMany = 0;
		
		HashSet<String> benchmarkPredictions = null;
		while((line=solution.readLine())!=null){
			parts = line.split(",");
			String userId = parts[0];
			System.out.print("for user id# " + userId + " ");
			line = parts[1];
			if(line.equals("missing")){
				sum += 0;
				continue;
			}
			howMany = 0;
			benchmarkPredictions = benchPairs.get(userId);
			if(benchmarkPredictions == null){
				continue;
			}
			int numOfBenchmarkPredictions = benchmarkPredictions.size();
			for (int i = 1; i < parts.length; i++) {
				if(benchmarkPredictions.contains(parts[i])){
					howMany++;
					benchmarkPredictions.remove(parts[i]);
				}
			}
			double precision =((double) howMany) / numOfBenchmarkPredictions;
			sum += precision;
			System.out.println(String.format("%.2f", precision*100) + "%");
			benchPairs.put(parts[0], predictions);
		}
		solution.close();
		System.out.println(String.format("%.2f", (sum /	benchPairs.size())*100) + "%");
	}
	
}
