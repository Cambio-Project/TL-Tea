package cambio.tltea.parser.core

import java.io.PrintStream
import kotlin.math.min

object ASTNodePrinter {

    private const val DEFAULT_CONSOLE_COLUMN_COUNT = 80

    fun print(root: ASTNode) {
        print(root, System.out)
    }

    fun print(root: ASTNode, out: PrintStream) {
        val treeWidth = root.treeWidth * 3 // *3 to reserve width of <->
        if (treeWidth > DEFAULT_CONSOLE_COLUMN_COUNT) {
            out.printf(
                "[TL-Tea] Warning: Tree might be to large (~%d columns) to be printed properly on a default console (80 columns)%n",
                treeWidth
            )
        }
        val dataMap1 = mutableMapOf<ASTNode, Pair<Int, Boolean>>()
        dataMap1[root] = Pair(treeWidth, true)

        val dataMap = createNodeInfo(root, treeWidth, dataMap1)
        val sb = StringBuilder()
        print(sb, root, dataMap)
        out.println(sb.toString())
    }

    private fun createNodeInfo(
        root: ASTNode,
        totalWidth: Int,
        dataMap: MutableMap<ASTNode, Pair<Int, Boolean>>
    ): Map<ASTNode, Pair<Int, Boolean>> {
        when (root) {
            is UnaryOperationASTNode -> {
                dataMap[root.child] = Pair(totalWidth, true)
                createNodeInfo(root.child, totalWidth, dataMap)
            }
            is BinaryOperationASTNode -> {
                dataMap[root.leftChild] = Pair(totalWidth/2, false)
                dataMap[root.rightChild] = Pair(totalWidth/2, true)
                createNodeInfo(root.leftChild, totalWidth / 2, dataMap)
                createNodeInfo(root.rightChild, totalWidth / 2, dataMap)
            }
        }
        return dataMap
    }

    private fun print(sb: StringBuilder, node: ASTNode, widthData: Map<ASTNode, Pair<Int, Boolean>>) {
        var currentDepth = 0
        val stack = ArrayDeque<ASTNode>()
        val lastDepthNodes = mutableListOf<ASTNode>()
        stack.addFirst(node)

        while (stack.isNotEmpty()) {
            val current = stack.removeFirst()
            if (current.depth() > currentDepth) {
                sb.append("\n")
                currentDepth = current.depth()

                for (node in lastDepthNodes) {
                    when (node) {
                        is UnaryOperationASTNode -> {
                            printWithPadding(sb, widthData[node]!!.first, "|")
                        }
                        is BinaryOperationASTNode -> {
                            val quaterLength = widthData[node]!!.first / 4
                            printWithPadding(sb, quaterLength + 1, "")
                            printWithPadding(sb, quaterLength, "/")
                            sb.append(" ")
                            printWithPadding(sb, quaterLength, "\\", false)
                            printWithPadding(sb, quaterLength + 1, "")//
                        }
                    }
                }
                sb.append("\n")
                lastDepthNodes.clear()
            }
            lastDepthNodes.add(current)
            printWithPadding(sb, widthData[current]!!.first, current.toFormulaString(), widthData[current]!!.second)

            when (current) {
                is UnaryOperationASTNode -> {
                    stack.addLast(current.child)
                }
                is BinaryOperationASTNode -> {
                    stack.addLast(current.leftChild)
                    stack.addLast(current.rightChild)
                }
            }
        }
    }


    private fun printWithPadding(sb: StringBuilder, totalLength: Int, s: String, preferRightPadding: Boolean = true) {
        var leftPadding = (totalLength - s.length) / 2
        var rightPadding = leftPadding
        if ((totalLength % 2 == 0) xor (s.length % 2 == 0)) {
            if (preferRightPadding) {
                rightPadding++
            } else {
                leftPadding++
            }
        }
        if (rightPadding > 0 || leftPadding > 0) {
            for (i in 0 until rightPadding) {
                sb.append(" ")
            }
            sb.append(s)
            for (i in 0 until leftPadding) {
                sb.append(" ")
            }
        } else {
            sb.append(s.substring(0, min(totalLength, s.length)))
        }
    }


}