package cambio.tltea.interpreter.connector.value.predicates

import java.util.*

class PrimitivePredicate {
    class EQPredicate<T : Comparable<T>> : IBiOperatorPredicate<T> {
        private lateinit var left: T
        private lateinit var right: T

        override fun setLeftValue(value: T): IBiOperatorPredicate<T> {
            this.left = value
            return this
        }

        override fun setRightValue(value: T): IBiOperatorPredicate<T> {
            this.right = value
            return this
        }

        override fun evaluate(): Boolean {
            Objects.requireNonNull(left)
            Objects.requireNonNull(right)

            return left == right
        }
    }

    class NEQPredicate<T : Comparable<T>> : IBiOperatorPredicate<T> {
        private lateinit var left: T
        private lateinit var right: T

        override fun setLeftValue(value: T): IBiOperatorPredicate<T> {
            this.left = value
            return this
        }

        override fun setRightValue(value: T): IBiOperatorPredicate<T> {
            this.right = value
            return this
        }

        override fun evaluate(): Boolean {
            Objects.requireNonNull(left)
            Objects.requireNonNull(right)

            return left != right
        }

    }

    class LTPredicate<T : Comparable<T>> : IBiOperatorPredicate<T> {
        private lateinit var left: T
        private lateinit var right: T

        override fun setLeftValue(value: T): IBiOperatorPredicate<T> {
            this.left = value
            return this
        }

        override fun setRightValue(value: T): IBiOperatorPredicate<T> {
            this.right = value
            return this
        }

        override fun evaluate(): Boolean {
            Objects.requireNonNull(left)
            Objects.requireNonNull(right)

            val compareValue = left.compareTo(right)

            return compareValue < 0
        }

    }


    class LEQPredicate<T : Comparable<T>> : IBiOperatorPredicate<T> {
        private lateinit var left: T
        private lateinit var right: T

        override fun setLeftValue(value: T): IBiOperatorPredicate<T> {
            this.left = value
            return this
        }

        override fun setRightValue(value: T): IBiOperatorPredicate<T> {
            this.right = value
            return this
        }

        override fun evaluate(): Boolean {
            Objects.requireNonNull(left)
            Objects.requireNonNull(right)

            val compareValue = left.compareTo(right)

            return compareValue <= 0
        }

    }

    class GTPredicate<T : Comparable<T>> : IBiOperatorPredicate<T> {
        private lateinit var left: T
        private lateinit var right: T

        override fun setLeftValue(value: T): IBiOperatorPredicate<T> {
            this.left = value
            return this
        }

        override fun setRightValue(value: T): IBiOperatorPredicate<T> {
            this.right = value
            return this
        }

        override fun evaluate(): Boolean {
            Objects.requireNonNull(left)
            Objects.requireNonNull(right)

            val compareValue = left.compareTo(right)

            return compareValue > 0
        }
    }

    class GEQPredicate<T : Comparable<T>> : IBiOperatorPredicate<T> {
        private lateinit var left: T
        private lateinit var right: T

        override fun setLeftValue(value: T): IBiOperatorPredicate<T> {
            this.left = value
            return this
        }

        override fun setRightValue(value: T): IBiOperatorPredicate<T> {
            this.right = value
            return this
        }

        override fun evaluate(): Boolean {
            Objects.requireNonNull(left)
            Objects.requireNonNull(right)

            val compareValue = left.compareTo(right)

            return compareValue >= 0
        }


    }

}
