package cambio.tltea.interpreter.nodes.logic.temporal

import cambio.tltea.interpreter.connector.Brokers
import cambio.tltea.parser.core.temporal.TemporalInterval

/**
 * This is just a approximation of the Next Operator for step sizes of exactly 1 time unit, and it will only work in this context.
 * In fact, it should fail when no data is provided. However, this implementation assumes there is a continuous signal and cannot distinguish this case.
 * Given that, the operator does actually not make much sense on a continuous signal.
 */
class NextTemporalLogic(
    brokers: Brokers
) : EventuallyTemporalLogic(TemporalInterval(1.0, 1.0), brokers) {
}