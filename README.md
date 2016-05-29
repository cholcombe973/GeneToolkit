# GeneToolkit
Part of the what I did in college series.  Ignore the fact that this says GeneToolkit.  This project is essentially a cluster 
that performs MapReduce jobs.  The [Worker.java](https://github.com/cholcombe973/GeneToolkit/blob/master/src/parallelComputingLib/Worker.java)
class performs work that is given to it and sends it back to the central server.

* Description:
 * The [Server.java](https://github.com/cholcombe973/GeneToolkit/blob/master/src/parallelComputingLib/Server.java) class acts 
like an orchestrator, while not doing any calculations itself it deals out work accordingly.
 * Workers are started up first individually.  When the server is started it broadcasts for workers to connect.
 * Server is started last it tells each worker to open up a connection to it and starts transferring data to them.
 * A network callback model was used and is detailed more in NetworkEvent and the NetworkListener class.
 * The individual method calls work like this. All the following methods are abstract and need to have code provided
 * by you the programmer.  First create a class that extends server.  
 * Then implement each of the methods as follows:
 * getWorkerData - This method can be called at any point but it's easiest to call it when a tcp event occurs. This method should 
return a bytebuffer formatted like your workers will expect.
 * handleData - This method is called on a read event which is assumed to have worker results.  This method should be used to 
store results, but anything can be done with the data.
 * identifyTCPDataAcceptEvent - An accept event is assumed to be a new worker asking for a connection and data. And with the 
callback channel that is provided getWorkerData is called to get data to write back to the worker.
 * identifyTCPDataReadEvent - A read event is assumed to have worker results contained in it.  handleData is called 
with the data and then getWorkerData is called to send new data back to the worker.
 * identifyUDPData - This method gets called each time a udp event occurs while the server is listening.
