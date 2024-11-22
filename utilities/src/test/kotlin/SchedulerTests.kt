import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import org.example.engine.Artifact
import org.example.engine.DependencyGraph
import org.example.engine.Scheduler
import org.example.engine.task.Task
import org.junit.jupiter.api.Assertions.assertEquals

class SchedulerKoTests : FunSpec({

    test("can schedule") {
        Scheduler(DependencyGraph()).schedule()
    }
})


class SchedulerTests {
    @AnnotationSpec.Test
    fun `should schedule tasks respecting dependencies`() {
        val graph = DependencyGraph()
        val taskA = Task(id = "A", dependencies = mutableListOf(), action = { Result.success(Artifact())}, inputs = emptyList(), outputs = emptyList())
        val taskB = Task(id = "B", dependencies = mutableListOf("A"), action = { Result.success(Artifact()) }, inputs = emptyList(), outputs = emptyList())
        graph.addTask(taskA)
        graph.addTask(taskB)

        val scheduler = Scheduler(graph)
        val ordered = scheduler.schedule()

        assertEquals(listOf(taskA, taskB), ordered)
    }

    @AnnotationSpec.Test(expected = IllegalStateException::class)
    fun `should detect cyclic dependencies`() {
        val graph = DependencyGraph()
        val taskA = Task(id = "A", dependencies = mutableListOf("B"), action = { Result.success(Artifact("")) }, inputs = emptyList(), outputs = emptyList())
        val taskB = Task(id = "B", dependencies = mutableListOf("A"), action = {Result.success(Artifact(""))}, inputs = emptyList(), outputs = emptyList())
        graph.addTask(taskA)
        graph.addTask(taskB)

        val scheduler = Scheduler(graph)
        scheduler.schedule() // Should throw exception
        //TODO("This needs to actually pass the block() action in")

    }
}