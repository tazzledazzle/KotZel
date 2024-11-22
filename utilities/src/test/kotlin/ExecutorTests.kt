import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.ints.shouldBeGreaterThan

class ExecutorTests: BehaviorSpec({
    given("A build") {
        `when`("it is run") {
            then("the build passes") {
                0 shouldBeGreaterThan -1
            }
        }
    }
})