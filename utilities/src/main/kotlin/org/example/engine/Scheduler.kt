package org.example.engine

import org.example.engine.task.Task
import java.util.LinkedList
import java.util.Queue

class Scheduler(private val graph: DependencyGraph) {
    //todo: number of ordered tasks matches tasks in graph or cycle exists
    fun schedule(): List<Task> {
        val inDegree = mutableMapOf<String, Int>()
        graph.getAllTasks().forEach { task ->
            inDegree[task.id] = task.dependencies.size
        }

        val queue: Queue<String> = LinkedList()
        inDegree.filter { it.value == 0 }.keys.forEach { queue.add(it) }

        val orderedTasks = mutableListOf<Task>()
        while (queue.isNotEmpty()) {
            val current = queue.remove()!!
            val task = graph.getTask(current)!!
            orderedTasks.add(task)

            graph.getDependents(current).forEach { dependent ->
                inDegree[dependent.id] = inDegree[dependent.id]!! - 1
                if (inDegree[dependent.id] == 0) {
                    queue.add(dependent.id)
                }
            }

            if (orderedTasks.size != graph.getAllTasks().size) {
                throw IllegalStateException("Cyclic dependency detected in the build graph.")
            }
        }

        return orderedTasks
    }
}