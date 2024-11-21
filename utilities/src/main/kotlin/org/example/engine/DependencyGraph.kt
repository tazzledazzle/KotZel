package org.example.engine

class DependencyGraph {
    private val adjacencyList: MutableMap<String, MutableList<String>> = mutableMapOf()
    private val tasks: MutableMap<String, Task> = mutableMapOf()

    fun addTask(task: Task) {
        tasks[task.id] = task
        adjacencyList.putIfAbsent(task.id, mutableListOf())
        task.dependencies.forEach { dep ->
            adjacencyList.putIfAbsent(dep, mutableListOf())
            adjacencyList[dep]?.add(task.id)
        }
    }


}