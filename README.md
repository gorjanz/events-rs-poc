<img href="http://github.com/gorjanz/smartcal/blob/master/SmartCal/WebContent/resources/images/event128px.png" />
events-rs-poc
=============

Proof of concept application for recommendation of events using hybrid collaborative filtering methods.
<hr/>
The dataset can be found at <a href="http://www.kaggle.com/c/event-recommendation-engine-challenge">Kaggle - Event Recommendation System Challenge</a>
<hr/>
The system uses K-Nearest Neighbours classifier.
<hr/>
The source code is divided into five packages:

- preprocessing - classes that handle the raw data, dividing the data into user/event specific files
- main - classes that are used to experiment with the classifier, and to generate .arff files
- models - classes that represent the feature vectors
- utils - classes that encapsulate utility methods used throughout the system
- evaluation - classes that are used to generate and compare the results
