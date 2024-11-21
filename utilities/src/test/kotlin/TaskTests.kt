import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe


class TaskTests : FunSpec( {
    xtest("This thing can pass") {
        "".length shouldBe 0
    }


})