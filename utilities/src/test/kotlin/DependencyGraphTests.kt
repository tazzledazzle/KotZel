import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.ints.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import org.example.engine.DependencyGraph
import org.example.engine.task.Task

class DependencyGraphTests : FunSpec({
    val graph = DependencyGraph()

    test("can create a graph") {
        graph::class.java shouldBe DependencyGraph::class.java
    }


    test("can add task"){
        graph.addTask(Task())
        graph.size shouldBe 1
    }

    test("can get tasks") {
        graph.addTask(Task("number-two"))
        graph.getTask("number-two")
    }

    test("can get dependents") {
        val test = Task("one")
        val test2 = Task("two")
        test.dependencies.add("two")

        graph.addTask(test)
        graph.addTask(test2)

        // dep of one should be two
        graph.getDependents("two").first() shouldBe test
    }

    test("can get all tasks") {
        val test = Task("one")
        val test2 = Task("two")
        test.dependencies.add("two")

        graph.addTask(test)
        graph.addTask(test2)
        graph.getAllTasks().size shouldBeGreaterThan 2
    }
})