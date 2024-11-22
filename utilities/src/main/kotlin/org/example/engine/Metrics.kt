package org.example.engine

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger

object Metrics {
    val taskExecutionTime: MutableMap<String, Long> = ConcurrentHashMap()
    val completedTasks: AtomicInteger = AtomicInteger(0)
    val failedTasks: AtomicInteger = AtomicInteger(0)
}