package rs.poc.main;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.FastVector;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;

public class ModelBuilder {

	public static void main(String[] args) throws Exception {

		Attribute id = new Attribute("id", (FastVector) null);
		Attribute creatorId = new Attribute("creatorId", (FastVector) null);
		Attribute startTime = new Attribute("startTime",
				"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

		FastVector classVal = new FastVector(2);
		classVal.addElement("interested");
		classVal.addElement("not interested");
		Attribute classAttribute = new Attribute("the class", classVal);

		FastVector fvWekaAttributes = new FastVector(4);
		fvWekaAttributes.addElement(id);
		fvWekaAttributes.addElement(creatorId);
		fvWekaAttributes.addElement(startTime);
		fvWekaAttributes.addElement(classAttribute);

		Instances isTrainingSet = new Instances("event", fvWekaAttributes, 0);
		isTrainingSet.setClassIndex(3);

		// setup one training instance
		double[] attValues = new double[isTrainingSet.numAttributes()];
		attValues[0] = isTrainingSet.attribute(0).addStringValue("684921758");
		attValues[1] = isTrainingSet.attribute(1).addStringValue("3647864012");
		attValues[2] = isTrainingSet.attribute(2).parseDate(
				"2012-10-31T00:00:00.001Z");
		attValues[3] = isTrainingSet.attribute(3).addStringValue("interested");
		isTrainingSet.add(new DenseInstance(1.0, attValues));

		// setup one test instance
		Instances isTestingSet = new Instances("event", fvWekaAttributes, 10);
		isTestingSet.setClassIndex(3);

		attValues = new double[isTrainingSet.numAttributes()];
		attValues[0] = isTestingSet.attribute(0).addStringValue("684921758");
		attValues[1] = isTestingSet.attribute(1).addStringValue("3647864012");
		attValues[2] = isTestingSet.attribute(2).parseDate(
				"2012-10-31T00:00:00.001Z");
		attValues[3] = isTestingSet.attribute(3).addStringValue("interested");
		isTestingSet.add(new DenseInstance(1.0, attValues));

		StringToWordVector filter = new StringToWordVector();
		filter.setInputFormat(isTrainingSet);
		isTrainingSet = Filter.useFilter(isTrainingSet, filter);
		
		filter = new StringToWordVector();
		filter.setInputFormat(isTestingSet);
		isTestingSet = Filter.useFilter(isTestingSet, filter);

		Classifier cModel = (Classifier) new NaiveBayes();
		cModel.buildClassifier(isTrainingSet);
		
		Evaluation eTest = new Evaluation(isTrainingSet);
		eTest.evaluateModel(cModel, isTestingSet);

		String strSummary = eTest.toSummaryString();
		System.out.println(strSummary);

	}

}
