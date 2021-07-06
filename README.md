# Event Generator App
The project was created for the master module **" Event Processing"** of Humboldt University Berlin.  
The project explores the topic of **"Out-of-order handling in CEP (POG-based solution)"**.
We took as the basis the article *"Sequence Pattern Query Processing over Out-of-Order Event Streams"*[[1]](#1).

### General Setting
The project consists of an **Event Generator App** and **Query Engine App**[[2]](#2).  
To simplify the task, we decided to make the event processing off-line instead of generating and processing events in real-time.  
The **Event Generator App** produces a stream of events of different types (from A to J) and POG elements.
The produced stream then is saved using JSON format.
We can vary the number of negative and out-of-order events in the stream using the settings in the Generator App.
We can also vary the POG percentage in generated data.   
The **Query Engine App** imports a produced JSON file and process Stream Objects from it with a given query and time window.

References
<a id="1">[1]</a> 
M. Liu, M. Li, E. Rundensteiner, D. Golovnya and K. Claypool,  *" Sequence Pattern Query Processing over Out-of-Order Event Streams,"* in 2013 IEEE 29th International Conference on Data Engineering (ICDE), null, 2009 pp. 784-795.
DOI: 10.1109/ICDE.2009.95  
<a id="2">[2]</a>
*Query Engine App*: https://github.com/grushety/QueryEngine
