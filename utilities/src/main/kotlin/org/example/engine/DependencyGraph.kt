package org.example.engine

class DependencyGraph {
    private val adjacencyList: MutableMap<String, MutableList<String>> = mutableMapOf()
    private val tasks: MutableMap<String, Task> = mutableMapOf()

    val size: Int
        get() = adjacencyList.size

    fun addTask(task: Task) {
        tasks[task.id] = task
        adjacencyList.putIfAbsent(task.id, mutableListOf())
        task.dependencies.forEach { dep ->
            adjacencyList.putIfAbsent(dep, mutableListOf())
            adjacencyList[dep]?.add(task.id)
        }
    }

    fun getTask(id: String): Task? = tasks[id]

    fun getDependents(id: String): List<Task> = adjacencyList[id]?.mapNotNull { tasks[it] } ?: emptyList()

    fun getAllTasks(): Collection<Task> = tasks.values

}