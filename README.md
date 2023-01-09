# OOP_Assignment_2

The getNumOfLines method processes the files sequentially, meaning that it reads one file completely before moving on to the next. This means that the running time of this method will be approximately the sum of the running times of each individual file.

The getNumOfLinesThreads method uses a separate thread to process each file, which allows the files to be processed in parallel. This means that the running time of this method will be approximately the running time of the longest-running file, rather than the sum of the running times of all the files. 

The getNumOfLinesThreadPool method also uses a thread pool to process the files in parallel, but it creates a thread pool with the same size as the number of files being processed. This means that each file will be processed by a separate thread, which is similar to the behavior of the getNumOfLinesThreads method. However, the getNumOfLinesThreadPool method has the added overhead of creating and shutting down the thread pool, which may make it slightly less efficient than the getNumOfLinesThreads method.

Overall, the running time of each method will depend on the number and size of the files being processed, as well as the system's resources and the specific implementation of the method. In general, using multiple threads or a thread pool can be more efficient than processing the files sequentially if the system has sufficient resources and the workload is well-suited to parallelization.
