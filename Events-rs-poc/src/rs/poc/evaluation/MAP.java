package rs.poc.evaluation;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;

import rs.poc.utils.Constants;

/**
 * Mean average precision (MAP) at K
 * @author Gorjan
 *
 */
public class MAP {

	public static double meanAveragePrecisionAtK(String solutionLoc, int k) throws Exception {
		
		//TODO
		// for each user in benchmark
		// find the user in solution
		// compare the indexes of the recommended events
		// based on the places of the recommended events calculate mean precision
		// sum-up all the mean precisions and divide it with the number of users

		BufferedReader benchmark = new BufferedReader(new FileReader(Constants.BENCHMARK_DATA));
		BufferedReader solution = new BufferedReader(new FileReader(solutionLoc));
		
		HashMap<String, String> benchmarkPairs = new HashMap<String, String>();
		HashMap<String, String> solutionPairs = new HashMap<String, String>();
		
		ArrayList<Double> avgs = new ArrayList<Double>();
		String benchmarkPredictionLine;
		String solutionPredictionLine;
		
		String[] userBenchmarkPredictions;
		String[] userSolutionPredictions;
		
		while((benchmarkPredictionLine = benchmark.readLine()) != null){
			String userId = benchmarkPredictionLine.split(",")[0];
			String eventPredictions = benchmarkPredictionLine.split("\"")[1];
			eventPredictions = eventPredictions
					.replace("[","")
					.replace("]", "")
					.replace("L", "")
					.replace(" ", "");
			benchmarkPairs.put(userId, eventPredictions);
		}
		
		while((solutionPredictionLine = solution.readLine()) != null){
			String userId = solutionPredictionLine.split("|")[0];
			String eventPredictions = solutionPredictionLine.split("|")[1];
			solutionPairs.put(userId, eventPredictions);
		}
		
		benchmark.close();
		solution.close();
		
		for (String id : benchmarkPairs.keySet()) {
			benchmarkPredictionLine = benchmarkPairs.get(id);
			solutionPredictionLine = solutionPairs.get(id);
			
			if(benchmarkPredictionLine == null || solutionPredictionLine == null){
				avgs.add(0.0);
				continue;
			}

			userBenchmarkPredictions = benchmarkPredictionLine.split(",");
			userSolutionPredictions = solutionPredictionLine.split(",");
			
			int min = Math.min(userBenchmarkPredictions.length, userSolutionPredictions.length);
			String benchmarkEventId;
			String solutionEventId;
			int correctPredictions = 0;
			double averagePrecision = 0.0;
			
			for (int i = 0; i < min; i++) {
			
				benchmarkEventId = userBenchmarkPredictions[i];
				solutionEventId = userSolutionPredictions[i];
				for(int j=0; j<i; j++){
					if(solutionEventId.equals(userBenchmarkPredictions[j])){
						correctPredictions++;
					}
				}
				double recall = 0.0;
				if(solutionEventId.equals(benchmarkEventId)){
					recall = 1/min;
				}
				double precision = (double) correctPredictions / i;
				averagePrecision += precision * recall;
			}
			
			avgs.add(averagePrecision);
			
		}
		
		double map = 0.0;
		for (int i = 0; i < avgs.size(); i++) {
			map += avgs.get(i);
		}
		
		return map/avgs.size();
	}
}
	
