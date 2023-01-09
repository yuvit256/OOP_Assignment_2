# OOP_Assignment_2

The getNumOfLines method processes the files sequentially, meaning that it reads one file completely before moving on to the next. This means that the running time of this method will be approximately the sum of the running times of each individual file.

The getNumOfLinesThreads method uses a separate thread to process each file, which allows the files to be processed in parallel. This means that the running time of this method will be approximately the running time of the longest-running file, rather than the sum of the running times of all the files. This can be more efficient if the files are of roughly similar size and the system has sufficient resources (e.g., CPU cores) to process them concurrently.

The getNumOfLinesThreadPool method also uses a thread pool to process the files in parallel, but it limits the number of threads that are used to 8. This can be more efficient than using a separate thread for each file, because it allows you to reuse threads and avoid the overhead of creating and starting a new thread for each task. However, if the number of threads in the pool is too small, it may not fully utilize the available resources and could potentially run more slowly than the getNumOfLinesThreads method.

Overall, the running time of each method will depend on the number and size of the files being processed, as well as the system's resources and the specific implementation of the method. In general, using multiple threads or a thread pool can be more efficient than processing the files sequentially if the system has sufficient resources and the workload is well-suited to parallelization.
