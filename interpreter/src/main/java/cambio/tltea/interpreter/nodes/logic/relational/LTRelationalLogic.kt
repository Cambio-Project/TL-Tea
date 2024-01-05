package cambio.tltea.interpreter.nodes.logic.relational

import cambio.tltea.interpreter.connector.Brokers
import java.util.*

class LTRelationalLogic<T : Comparable<T>>(brokers: Brokers) : AbstractRelationalLogic<T>(brokers) {

    override fun evaluate(): Boolean {
        Objects.requireNonNull(left)
        Objects.requireNonNull(right)

        val compareValue = left.compareTo(right)

        return compareValue < 0
    }

}