# OOP_Assignment_2

The getNumOfLines method processes the files sequentially, meaning that it reads one file completely before moving on to the next. This means that the running time of this method will be approximately the sum of the running times of each individual file.

The getNumOfLinesThreads method uses a separate thread to process each file, which allows the files to be processed in parallel. This means that the running time of this method will be approximately the running time of the longest-running file, rather than the sum of the running times of all the files. 

The getNumOfLinesThreadPool method also uses a thread pool to process the files in parallel, but it creates a thread pool with the same size as the number of files being processed. This means that each file will be processed by a separate thread, which is similar to the behavior of the getNumOfLinesThreads method. However, the getNumOfLinesThreadPool method has the added overhead of creating and shutting down the thread pool, which may make it slightly less efficient than the getNumOfLinesThreads method.

Overall, the running time of each method will depend on the number and size of the files being processed, as well as the system's resources and the specific implementation of the method. In general, using multiple threads or a thread pool can be more efficient than processing the files sequentially if the system has sufficient resources and the workload is well-suited to parallelization.

## The UML:
![Alt text](/home/yuval/Documents/Lightshot)

## Part2:

In this assignment, we worked on the Interfaces Callable and Runnable and on the ways to implement them.(Class, Lambda)

In summary:
Callable: Asynchronous command which can run in a different course than the main program, Callable returns a generic variable. 
Runnable: an asynchronous which does not return any value.
We implemented the Class Task with private builders, Factory Methods, Comparable and Callable Class Implementation , they are static which means they belong to a specific Class. 
We used Constructor Chaining.
We implemented the submit method which sends a task to the Threadpool in which we call execute and return Future type (it's a variable we get from the submit function when the task we sent gets to the end).


