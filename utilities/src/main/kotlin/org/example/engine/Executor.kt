package org.example.engine

import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Semaphore

class Executor(
    private val graph: DependencyGraph,
    private val cache: Cache,
    private val maxConcurrency: Int = Runtime.getRuntime().availableProcessors(),

) {
    private val coroutineScope = CoroutineScope(Dispatchers.Default)
    private val semaphore = Semaphore(maxConcurrency)

    suspend fun executeBuild() {
        val scheduler = Scheduler(graph)
        val orderedTasks = scheduler.schedule()
        val taskMap = orderedTasks.associateBy { it.id }

        val jobMap = mutableMapOf<String, Job>()

        for (task in orderedTasks) {
            jobMap[task.id] = coroutineScope.launch {
                semaphore.acquire()
                try {
                    executeTask(task)
                } finally {
                    semaphore.release()
                }
            }

            task.dependencies.forEach { depId ->
                jobMap[depId]?.join()
            }
        }

        jobMap.values.forEach { it.join() }
    }

    // error handling and recovery
    private suspend fun executeTask(task: Task) {
        if (task.status != TaskStatus.PENDING) return

        val cacheKey = generateCacheKey(task)
        val cachedArtifact = cache.retrieve(cacheKey)
        if (cachedArtifact != null) {
            task.status = TaskStatus.RUNNING
        }
    }

    private fun generateCacheKey(task: Task): String {
        TODO("Not yet implemented")
    }
}