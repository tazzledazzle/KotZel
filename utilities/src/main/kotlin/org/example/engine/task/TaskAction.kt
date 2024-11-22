package org.example.engine.task

import org.example.engine.Artifact

interface TaskAction {
    suspend fun execute(): Result<Artifact>
}

class CustomCompileAction(/* parameters */) : TaskAction {
    override suspend fun execute(): Result<Artifact> {
        // Custom compile logic
        TODO("HAS TO execute the action")
    }
}