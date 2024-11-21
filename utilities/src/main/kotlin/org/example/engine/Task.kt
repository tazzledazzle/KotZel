package org.example.engine

data class Task (
    val id: String,
    val dependencies: List<String>,
    val action: () -> Result<Artifact>,
    val inputs: List<Artifact>,
    val outputs: List<Artifact>,
    val status: TaskStatus = TaskStatus.PENDING
)

enum class TaskStatus {
    PENDING,
    RUNNING,
    COMPLETED,
    FAILED
}