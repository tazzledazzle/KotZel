import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import org.example.engine.DependencyGraph
import org.example.engine.Scheduler

class SchedulerTests : FunSpec({

    test("can schedule") {
        Scheduler(DependencyGraph()).schedule()
    }
})
