package org.example.engine

data class Task (
    val id: String = "",
    val dependencies: MutableList<String> = mutableListOf(),
    val action: () -> Result<Artifact> = { defaultAction() },
    val inputs: List<Artifact> = listOf(),
    val outputs: List<Artifact> = listOf(),
    val status: TaskStatus = TaskStatus.PENDING
)

fun defaultAction(): Result<Artifact> {
    return Result.success(Artifact())
}

enum class TaskStatus {
    PENDING,
    RUNNING,
    COMPLETED,
    FAILED
}