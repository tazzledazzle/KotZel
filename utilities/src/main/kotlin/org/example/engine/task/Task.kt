package org.example.engine.task

import org.example.engine.Artifact

data class Task (
    val id: String = "",
    var dependencies: MutableList<String> = mutableListOf(),
    var action: () -> Result<Artifact> = { defaultAction() },
    var inputs: List<Artifact> = listOf(),
    var outputs: List<Artifact> = listOf(),
    var status: TaskStatus = TaskStatus.PENDING
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