import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import org.example.engine.DependencyGraph
import org.example.engine.LocalCache
import org.example.engine.RemoteCache
import kotlin.io.path.Path

class CacheTests: FunSpec({
    test("local cache") {
        val cache = LocalCache(Path("."))
        cache::class.java shouldBe LocalCache::class.java
    }

    test("remote cache") {
        val cache = RemoteCache("//endpoint")
        cache::class.java shouldBe RemoteCache::class.java
    }

    test("cache key generation") {
        val cache = LocalCache(Path("."))

    }
})