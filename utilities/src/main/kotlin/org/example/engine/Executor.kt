package org.example.engine

import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Semaphore
import org.example.engine.task.Task
import org.example.engine.task.TaskStatus

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
            task.status = TaskStatus.COMPLETED
            return
        }

        task.status = TaskStatus.RUNNING
        var attempt = 0
        val maxRetries = 3
        var success = false

        while ((attempt < maxRetries) && !success)  {
            try {
                val startTime = System.currentTimeMillis()
                val result = withContext(Dispatchers.IO) { task.action() }
                val endTime = System.currentTimeMillis()
                Metrics.taskExecutionTime[task.id] = endTime - startTime
                when  {
                    result.isSuccess -> {
                        cache.store(cacheKey, result.getOrElse { Artifact(path = task.id) }) //todo: passing the id here seems wrong, but I go fast
                        task.status = TaskStatus.COMPLETED
                        Metrics.completedTasks.incrementAndGet()
                        success = true
                    }
                    result.isFailure -> {
                        Metrics.failedTasks.incrementAndGet()
                        throw Exception("badd")
                    }
                }
            } catch (e: Exception) {
                attempt += 1
                if (attempt >= maxRetries) {
                    println("Task ${task.id} failed after $attempt attempts: ${e.message}")
                    throw e
                } else {
                    println("Task ${task.id} failed on attempt $attempt: ${e.message}")
                }

            }
        }
    }

    private fun generateCacheKey(task: Task): String {
        val inputHashes = task.inputs.joinToString("") { it.hash }
        val dependenciesHashes = task.dependencies.mapNotNull { depId ->
            graph.getTask(depId)?.outputs?.joinToString("") { it.hash }
        }.joinToString("")
        return "${task.id}:${inputHashes}:${dependenciesHashes}"
    }
}