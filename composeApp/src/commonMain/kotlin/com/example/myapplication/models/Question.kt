package com.example.myapplication.models

enum class MathOperator(val symbol: String) {
    ADD("+"), SUBTRACT("−"), MULTIPLY("×"), DIVIDE("÷"), POWER("^")
}

/**
 * Hint shown in the bottom sheet.
 * @param mainText  text in the left-border box (may contain [boldPart] in bold)
 * @param boldPart  substring of [mainText] to render bold
 * @param sectionTitle  small-caps label above [items]
 * @param items     bullet rows below [sectionTitle]
 * @param steps     "Krok po kroku" list
 */
data class Hint(
    val mainText: String,
    val boldPart: String? = null,
    val sectionTitle: String? = null,
    val items: List<String> = emptyList(),
    val steps: List<String> = emptyList()
)

sealed class Question(open val id: Int) {
    /** User types the numeric answer */
    data class FindAnswer(
        override val id: Int,
        val operand1: Int,
        val operand2: Int,
        val operator: MathOperator,
        val hint: Hint = Hint("")
    ) : Question(id) {
        val correctAnswer: Int = when (operator) {
            MathOperator.ADD      -> operand1 + operand2
            MathOperator.SUBTRACT -> operand1 - operand2
            MathOperator.MULTIPLY -> operand1 * operand2
            MathOperator.DIVIDE   -> operand1 / operand2
            MathOperator.POWER    -> {
                var r = 1
                repeat(operand2) { r *= operand1 }
                r
            }
        }
    }

    /** User drags the correct operator into the blank */
    data class FindOperator(
        override val id: Int,
        val operand1: Int,
        val operand2: Int,
        val result: Int,
        val correctOperator: MathOperator,
        val hint: Hint = Hint("")
    ) : Question(id)

    /**
     * User selects one or multiple correct options from a list.
     * @param multiSelect  false = radio (exactly one correct), true = checkbox (one or more correct)
     */
    data class SelectFromList(
        override val id: Int,
        val prompt: String,
        val options: List<String>,
        val correctIndices: Set<Int>,
        val multiSelect: Boolean = false,
        val hint: Hint = Hint("")
    ) : Question(id)

    /**
     * User types a numeric answer. Optionally shows a triangle diagram.
     * @param triangleAngles  pair of known angles; unknown is 180 - a - b
     * @param inlineHint      always-visible hint row beneath the input
     */
    data class TypeAnswer(
        override val id: Int,
        val prompt: String,
        val correctAnswer: Int,
        val unit: String = "",
        val triangleAngles: Pair<Int, Int>? = null,
        val inlineHint: String? = null,
        val hint: Hint = Hint("")
    ) : Question(id)
}
