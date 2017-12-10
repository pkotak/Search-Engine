CS6200-Project 

===============================================================================
SETUP:

There are two ways to run this application
1) Execute the runnable JAR file (GauravNGandhi_HardikRShah_PaarthJKotak.jar)<br/>
  	a) Open command prompt<br/>
  b) Go inside the folder GauravNGandhi_HardikRShah_PaarthJKotak/ExecutableJAR<br/>
  c) Execute the jar file (java -jar GauravNGandhi_HardikRShah_PaarthJKotak.jar)<br/>
  
2) Import the project into one of the IDEs (Eclipse, Intellij) and run Index.java:<br/>
  a) Import the project CS6200 into your IDE from GauravNGandhi_HardikRShah_PaarthJKotak/EclipseProject<br/>
  b) Run Index.java as a Java application.<br/>

===============================================================================
Flowchart of Index.java
1. Phase 1: Indexing, retrieval systems, Pseudo relevance feedback, baseline run
using stop word list and stemmed corpus.
	1. Task 1: Indexing and retrieval systems
		1. Parse the documents and create a word unigram inverted Index
		2. Perform baseline runs
			1. BM25 retrieval model
			2. TF-IDF retrieval model
			3. Smoothed Query likelihood retrieval model
			4. Lucene retrieval model
	
	2. Task 2: Pseudo Relevance feedback (Note: Smoothed query likelihood retrieval
	system is used).
	
	3. Task 3: Baseline runs using stop word list and stemmed corpus.
		1. Create word unigram inverted index using stop word list.
		2. Create word unigram inverted index using stemmed corpus.
		3. Run retrieval systems on stopped and stemmed inverted index
			1. Run BM25, Querylikelihood and TF-IDF on stopped inverted index.
			2. Run BM25, Querylikelihood and TF-IDF on stemmed corpus.

2. Phase 2: Snippet Generation (Note: Smoothed query likelihood retrieval model is used).

3. Phase 3: Evaluation on 4 baseline runs, 1 query refinement run (PRF) and 3 stopped runs