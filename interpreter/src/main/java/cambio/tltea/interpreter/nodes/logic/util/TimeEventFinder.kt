package cambio.tltea.interpreter.nodes.logic.util

import kotlin.math.max

/**
 * An optimized method for finding time events. The assumption is that most events are searched at the end of the (sorted) event list.
 * Thus, a linear search from the end of the list is the best choice. In other cases, a binary search is performed.
 */
abstract class TimeEventFinder(private val events: List<TimeEvent>) {
    /**
     * The number of events necessary to switch to the dynamic search algorithms
     */
    private val dynamicOptimizedSearchBorder = 10

    /**
     * The maximum number of events from the end of the list for that the searched event is considered close to the end.
     */
    private val maxTotalEventsFromEnd = 20

    /**
     * If the searched event is higher than this boundary (but lower than maxTotalEventsFromEnd),
     * it is considered close to the end.
     */
    private val relativeEventBoundary = 0.95

    protected fun find(): Pair<Int, TimeEvent> {
        return if (events.size >= dynamicOptimizedSearchBorder) {
            val (almostEndIndex, almostEndEvent) = determineCloseToEndEvent()
            prepareEvaluation(almostEndIndex, almostEndEvent)
            if (searchHigher(almostEndIndex, almostEndEvent)) {
                performLinearSearch()
            } else {
                performBinarySearch()
            }
        } else {
            performLinearSearch()
        }
    }

    private fun determineCloseToEndEvent(): Pair<Int, TimeEvent> {
        val absolutCloseToEndBorderIndex = events.lastIndex - maxTotalEventsFromEnd
        val relativeCloseToEndBorderIndex = (relativeEventBoundary * events.size).toInt()
        val almostEndIndex =
            max(absolutCloseToEndBorderIndex, relativeCloseToEndBorderIndex)
        return Pair(almostEndIndex, events[almostEndIndex])
    }

    protected abstract fun prepareEvaluation(index: Int, event: TimeEvent)

    protected abstract fun searchLower(index: Int, event: TimeEvent): Boolean

    protected abstract fun searchHigher(index: Int, event: TimeEvent): Boolean

    protected abstract fun isSearchEvent(index: Int, event: TimeEvent): Boolean

    protected abstract fun returnNotFoundDefault(): TimeEvent

    private fun performLinearSearch(): Pair<Int, TimeEvent> {
        for ((reverseIndex, currentEvent) in events.asReversed().withIndex()) {
            val index = events.lastIndex - reverseIndex
            prepareEvaluation(index, currentEvent)
            if (isSearchEvent(index, currentEvent)) {
                return Pair(index, currentEvent)
            } else if (searchHigher(index, currentEvent)) {
                break
            }
        }
        return Pair(-1, returnNotFoundDefault())
    }


    private fun performBinarySearch(): Pair<Int, TimeEvent> {
        var low = 0
        var high = events.size - 1
        var mid: Int
        if (high >= 0) {
            while (low <= high) {
                mid = low + ((high - low) / 2)
                val event = events[mid]
                prepareEvaluation(mid, event)
                when {
                    searchLower(mid, event) -> high = mid - 1
                    searchHigher(mid, event) -> low = mid + 1
                    isSearchEvent(mid, event) -> return Pair(mid, event)
                }
            }
        }
        return Pair(-1, returnNotFoundDefault())
    }
}