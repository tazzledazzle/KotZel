package org.example.engine

import java.nio.file.Files
import java.nio.file.Path

interface Cache {
    fun retrieve(key: String): Artifact?
    fun store(key: String, artifact: Artifact)
}


class LocalCache(private val cacheDir: Path): Cache {
    override fun retrieve(key: String): Artifact? {
        val artifactPath = cacheDir.resolve(key)
        return if (Files.exists(artifactPath)) {
            val hash = Files.readAllBytes(artifactPath).hashCode().toString()
            Artifact(path = artifactPath.toString(), hash = hash)
        }
        else {
            null
        }
    }

    override fun store(key: String, artifact: Artifact) {
        val artifactPath = cacheDir.resolve(key)
        Files.createDirectories(artifactPath)
    }
}

class RemoteCache(private val endpoint: String) : Cache {
    override fun store(key: String, artifact: Artifact) {
        TODO("Not yet implemented")
    }

    override fun retrieve(key: String): Artifact? {
        TODO("Not yet implemented")
    }
}