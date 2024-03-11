package cambio.tltea.interpreter.nodes.logic.relational

import cambio.tltea.interpreter.connector.Brokers
import java.util.*

class EQRelationalLogic<T : Comparable<T>>(brokers: Brokers) : AbstractRelationalLogic<T>(brokers) {

    override fun evaluate(): Boolean {
        Objects.requireNonNull(left)
        Objects.requireNonNull(right)

        return left == right
    }
}