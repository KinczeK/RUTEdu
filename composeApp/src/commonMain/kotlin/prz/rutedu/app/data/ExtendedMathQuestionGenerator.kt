package prz.rutedu.app.data

import androidx.compose.ui.graphics.Color
import prz.rutedu.app.math.MathShape
import prz.rutedu.app.math.MathViewport
import prz.rutedu.app.math.Pt
import prz.rutedu.app.models.Hint
import prz.rutedu.app.models.Question
import prz.rutedu.app.models.Question.FractionAnswer
import prz.rutedu.app.models.Question.GraphSelectFromList
import prz.rutedu.app.models.Question.GraphTypeAnswer
import prz.rutedu.app.models.Question.SelectFromList
import prz.rutedu.app.models.Question.TypeAnswer
import kotlin.math.abs
import kotlin.random.Random

/**
 * Procedural question set for "Matematyka rozszerzona".
 */
object ExtendedMathQuestionGenerator {

    fun generateFor(
        lessonId: String,
        seed: Long,
        excludeIds: Set<Int> = emptySet()
    ): List<Question> {
        val all = when (lessonId) {
            "mat_roz_1_1" -> functionsWithParameter(seed)
            "mat_roz_1_2" -> graphTransformations(seed)
            "mat_roz_1_3" -> absoluteValueFunctions(seed)
            "mat_roz_2_1" -> polynomialFactorization(seed)
            "mat_roz_2_2" -> polynomialEquations(seed)
            "mat_roz_2_3" -> rationalFunctions(seed)
            "mat_roz_3_1" -> trigonometricIdentities(seed)
            "mat_roz_3_2" -> trigonometricEquations(seed)
            "mat_roz_3_3" -> trigonometricGraphs(seed)
            "mat_roz_4_1" -> lineAndCircle(seed)
            "mat_roz_4_2" -> pointLineDistance(seed)
            "mat_roz_4_3" -> vectors(seed)
            "mat_roz_5_1" -> sequences(seed)
            "mat_roz_5_2" -> sequenceLimits(seed)
            "mat_roz_5_3" -> geometricSeries(seed)
            "mat_roz_6_1" -> combinatorics(seed)
            "mat_roz_6_2" -> bernoulli(seed)
            "mat_roz_6_3" -> conditionalProbability(seed)
            "mat_roz_7_1" -> derivativeRate(seed)
            "mat_roz_7_2" -> extrema(seed)
            "mat_roz_7_3" -> tangent(seed)
            else -> emptyList()
        }
        return if (excludeIds.isEmpty()) all else all.filter { it.id !in excludeIds }
    }

    fun totalFor(lessonId: String): Int = generateFor(lessonId, seed = 0L).size

    private fun select(
        id: Int,
        prompt: String,
        correct: String,
        wrong: List<String>,
        rng: Random,
        hint: Hint = Hint("")
    ): SelectFromList {
        val options = (listOf(correct) + wrong).distinct().shuffled(rng)
        return SelectFromList(id, prompt, options, setOf(options.indexOf(correct)), hint = hint)
    }

    private fun functionsWithParameter(seed: Long): List<Question> {
        val rng = Random(seed)
        val questions = mutableListOf<Question>()

        repeat(8) { i ->
            val a = rng.nextInt(-4, 5).let { if (it == 0) 2 else it }
            val b = rng.nextInt(-6, 7)
            val x = rng.nextInt(-3, 4)
            val value = a * x + b
            questions += TypeAnswer(
                10100 + i,
                "Dla f(x) = ${a}x + ($b) oblicz f($x).",
                value,
                hint = Hint(
                    "Podstaw $x w miejsce x.",
                    steps = listOf("f($x) = $a * $x + ($b)", "f($x) = $value")
                )
            )
        }

        return questions.shuffled(rng)
    }

    private fun graphTransformations(seed: Long): List<Question> {
        val rng = Random(seed)
        val questions = mutableListOf<Question>()

        repeat(6) { i ->
            val p = rng.nextInt(-3, 4)
            val q = rng.nextInt(-3, 4)
            val shapes = listOf(
                MathShape.FunctionPlot(
                    f = { x -> (x - p) * (x - p) + q },
                    color = Color(0xFFE53935)
                ),
                MathShape.PointMark(
                    Pt(p.toDouble(), q.toDouble()),
                    label = "W",
                    color = Color(0xFFE53935)
                )
            )
            questions += GraphSelectFromList(
                10200 + i,
                "Wskaż wierzchołek paraboli y = (x - $p)^2 + ($q).",
                shapes,
                MathViewport(xMin = -6.0, xMax = 6.0, yMin = -4.0, yMax = 10.0),
                listOf("($p, $q)", "(${-p}, $q)", "($p, ${-q})").distinct().shuffled(rng),
                emptySet(),
                hint = Hint("Postać y = (x - p)^2 + q ma wierzchołek w punkcie (p, q).")
            ).let { qn -> qn.copy(correctIndices = setOf(qn.options.indexOf("($p, $q)"))) }
        }

        return questions.shuffled(rng)
    }

    private fun absoluteValueFunctions(seed: Long): List<Question> {
        val rng = Random(seed)
        val questions = mutableListOf<Question>()

        repeat(8) { i ->
            val a = rng.nextInt(-5, 6)
            val b = rng.nextInt(-3, 4)
            val x = rng.nextInt(-6, 7)
            val value = abs(x - a) + b
            questions += TypeAnswer(
                10300 + i,
                "Dla f(x) = |x - ($a)| + ($b) oblicz f($x).",
                value,
                hint = Hint(
                    "Najpierw oblicz wartość w module, potem dodaj przesunięcie.",
                    steps = listOf("|$x - ($a)| = ${abs(x - a)}", "f($x) = $value")
                )
            )
        }

        return questions.shuffled(rng)
    }

    private fun polynomialFactorization(seed: Long): List<Question> {
        val rng = Random(seed)
        val questions = mutableListOf<Question>()

        repeat(8) { i ->
            val p = rng.nextInt(1, 7)
            val q = rng.nextInt(1, 7)
            val b = p + q
            val c = p * q
            val correct = "(x + $p)(x + $q)"
            questions += select(
                20100 + i,
                "Rozłóż na czynniki: x^2 + ${b}x + $c",
                correct,
                listOf("(x - $p)(x - $q)", "(x + $b)(x + $c)", "(x + $p)(x - $q)"),
                rng,
                Hint("Szukamy dwóch liczb o sumie $b i iloczynie $c.")
            )
        }

        return questions.shuffled(rng)
    }

    private fun polynomialEquations(seed: Long): List<Question> {
        val rng = Random(seed)
        val questions = mutableListOf<Question>()

        repeat(8) { i ->
            val r1 = rng.nextInt(-5, 0)
            val r2 = (1..5).filter { it != -r1 }.random(rng)
            val b = -(r1 + r2)
            val c = r1 * r2
            val middle = if (b >= 0) "+ ${b}x" else "- ${-b}x"
            val end = if (c >= 0) "+ $c" else "- ${-c}"
            val correct = "x = $r1 lub x = $r2"
            questions += select(
                20200 + i,
                "Rozwiąż równanie: x^2 $middle $end = 0",
                correct,
                listOf(
                    "x = ${-r1} lub x = ${-r2}",
                    "x = ${r1 + r2}",
                    "brak rozwiązań rzeczywistych"
                ),
                rng,
                Hint("Równanie ma postać (x - r1)(x - r2) = 0.")
            )
        }

        return questions.shuffled(rng)
    }

    private fun rationalFunctions(seed: Long): List<Question> {
        val rng = Random(seed)
        val questions = mutableListOf<Question>()

        repeat(6) { i ->
            val a = rng.nextInt(-4, 5).let { if (it == 0) 1 else it }
            val shapes = listOf(
                MathShape.FunctionPlot(
                    f = { x -> 1.0 / (x - a) },
                    color = Color(0xFFE53935)
                )
            )
            questions += GraphSelectFromList(
                20300 + i,
                "Dla f(x) = 1/(x - ($a)) wskaż asymptotę pionową.",
                shapes,
                MathViewport(xMin = -7.0, xMax = 7.0, yMin = -6.0, yMax = 6.0),
                listOf("x = $a", "y = $a", "x = ${-a}", "y = 0").distinct().shuffled(rng),
                emptySet(),
                hint = Hint("Mianownik zeruje się dla x = $a, więc tam jest asymptota pionowa.")
            ).let { qn -> qn.copy(correctIndices = setOf(qn.options.indexOf("x = $a"))) }
        }

        return questions.shuffled(rng)
    }

    private fun trigonometricIdentities(seed: Long): List<Question> {
        val rng = Random(seed)

        return listOf(
            select(
                30100,
                "Która tożsamość jest prawdziwa?",
                "sin^2(x) + cos^2(x) = 1",
                listOf("sin(x) + cos(x) = 1", "tg(x) = cos(x)/sin(x)", "sin^2(x) - cos^2(x) = 1"),
                rng
            ),
            select(
                30101,
                "Czemu równa się tg(x)?",
                "sin(x)/cos(x)",
                listOf("cos(x)/sin(x)", "sin(x)*cos(x)", "1/sin(x)"),
                rng
            ),
            select(
                30102,
                "Jeśli sin(x)=3/5 i x jest ostry, to cos(x) wynosi:",
                "4/5",
                listOf("3/4", "5/4", "2/5"),
                rng
            ),
            select(
                30103,
                "W trójkącie prostokątnym sin kąta ostrego to:",
                "przyprostokątna naprzeciw / przeciwprostokątna",
                listOf(
                    "przyprostokątna przy kącie / przeciwprostokątna",
                    "przeciwprostokątna / przyprostokątna",
                    "suma przyprostokątnych"
                ),
                rng
            )
        ).shuffled(rng)
    }

    private fun trigonometricEquations(seed: Long): List<Question> {
        val rng = Random(seed)

        return listOf(
            select(
                30200,
                "Rozwiązania równania sin(x)=0 w przedziale <0, 2π> to:",
                "0, π, 2π",
                listOf("π/2", "0, π/2", "π/2, 3π/2"),
                rng
            ),
            select(
                30201,
                "Rozwiązania równania cos(x)=0 w przedziale <0, 2π> to:",
                "π/2, 3π/2",
                listOf("0, π", "π", "0, 2π"),
                rng
            ),
            select(
                30202,
                "Rozwiązania równania sin(x)=1 w przedziale <0, 2π> to:",
                "π/2",
                listOf("0", "π", "3π/2"),
                rng
            ),
            select(
                30203,
                "Rozwiązania równania cos(x)=1 w przedziale <0, 2π> to:",
                "0, 2π",
                listOf("π/2", "π", "π/2, 3π/2"),
                rng
            )
        ).shuffled(rng)
    }

    private fun trigonometricGraphs(seed: Long): List<Question> {
        val rng = Random(seed)

        return listOf(
            GraphSelectFromList(
                30300,
                "Który wykres jest narysowany?",
                listOf(MathShape.FunctionPlot({ x -> kotlin.math.sin(x) }, Color(0xFFE53935))),
                MathViewport(xMin = -6.5, xMax = 6.5, yMin = -1.5, yMax = 1.5, gridStep = 1.0),
                listOf("sin(x)", "cos(x)", "tg(x)"),
                setOf(0),
                hint = Hint("Sinus przechodzi przez punkt (0, 0).")
            ),
            GraphSelectFromList(
                30301,
                "Który wykres jest narysowany?",
                listOf(MathShape.FunctionPlot({ x -> kotlin.math.cos(x) }, Color(0xFFE53935))),
                MathViewport(xMin = -6.5, xMax = 6.5, yMin = -1.5, yMax = 1.5, gridStep = 1.0),
                listOf("cos(x)", "sin(x)", "tg(x)"),
                setOf(0),
                hint = Hint("Cosinus dla x=0 ma wartość 1.")
            )
        ).shuffled(rng)
    }

    private fun lineAndCircle(seed: Long): List<Question> {
        val rng = Random(seed)
        val questions = mutableListOf<Question>()

        repeat(8) { i ->
            if (rng.nextBoolean()) {
                val a = rng.nextInt(-4, 5).let { if (it == 0) 1 else it }
                val b = rng.nextInt(-5, 6)
                questions += TypeAnswer(
                    40100 + i,
                    "Jaki jest współczynnik kierunkowy prostej y = ${a}x + ($b)?",
                    a,
                    hint = Hint("W równaniu y = ax + b współczynnik kierunkowy to a.")
                )
            } else {
                val r = rng.nextInt(2, 8)
                questions += TypeAnswer(
                    40100 + i,
                    "Okrąg ma równanie (x-a)^2 + (y-b)^2 = ${r * r}. Podaj promień.",
                    r,
                    hint = Hint("Prawa strona równania okręgu to r^2.")
                )
            }
        }

        return questions.shuffled(rng)
    }

    private fun pointLineDistance(seed: Long): List<Question> {
        val rng = Random(seed)
        val questions = mutableListOf<Question>()

        repeat(8) { i ->
            val point = rng.nextInt(-6, 7)
            val line = rng.nextInt(-4, 5)
            val vertical = rng.nextBoolean()
            val distance = abs(point - line)
            val prompt = if (vertical) {
                "Oblicz odległość punktu P($point, 2) od prostej x = $line."
            } else {
                "Oblicz odległość punktu P(2, $point) od prostej y = $line."
            }
            questions += TypeAnswer(
                40200 + i,
                prompt,
                distance,
                hint = Hint("Dla prostej pionowej lub poziomej wystarczy różnica odpowiednich współrzędnych.")
            )
        }

        return questions.shuffled(rng)
    }

    private fun vectors(seed: Long): List<Question> {
        val rng = Random(seed)
        val questions = mutableListOf<Question>()

        repeat(8) { i ->
            val ax = rng.nextInt(-4, 5)
            val ay = rng.nextInt(-4, 5)
            val bx = rng.nextInt(-4, 5)
            val by = rng.nextInt(-4, 5)
            val askX = rng.nextBoolean()
            val answer = if (askX) ax + bx else ay + by
            val coord = if (askX) "pierwszą" else "drugą"
            questions += TypeAnswer(
                40300 + i,
                "Dane są wektory u=($ax,$ay), v=($bx,$by). Podaj $coord współrzędną u+v.",
                answer,
                hint = Hint("Wektory dodajemy współrzędna po współrzędnej.")
            )
        }

        return questions.shuffled(rng)
    }

    private fun sequences(seed: Long): List<Question> {
        val rng = Random(seed)
        val questions = mutableListOf<Question>()

        repeat(8) { i ->
            val a1 = rng.nextInt(1, 8)
            val r = rng.nextInt(2, 6)
            val n = rng.nextInt(4, 9)
            val value = a1 + (n - 1) * r
            questions += TypeAnswer(
                50100 + i,
                "Ciąg arytmetyczny: a1=$a1, r=$r. Oblicz a$n.",
                value,
                hint = Hint("Wzór: an = a1 + (n-1)r.")
            )
        }

        return questions.shuffled(rng)
    }

    private fun sequenceLimits(seed: Long): List<Question> {
        val rng = Random(seed)

        return listOf(
            select(
                50200,
                "Granica ciągu (3n+1)/(n-2) wynosi:",
                "3",
                listOf("1", "0", "n"),
                rng,
                Hint("Dla ilorazu wielomianów tego samego stopnia bierzemy iloraz współczynników przy n.")
            ),
            select(50201, "Granica ciągu (n+5)/(2n+1) wynosi:", "1/2", listOf("2", "5", "0"), rng),
            select(50202, "Granica ciągu 4/n wynosi:", "0", listOf("4", "1", "n"), rng),
            select(
                50203,
                "Granica ciągu (2n^2+1)/(n^2+3) wynosi:",
                "2",
                listOf("1/2", "0", "3"),
                rng
            )
        ).shuffled(rng)
    }

    private fun geometricSeries(seed: Long): List<Question> {
        val rng = Random(seed)

        return listOf(
            TypeAnswer(
                50300,
                "Suma nieskończonego szeregu geometrycznego: a1=1, q=1/2.",
                2,
                hint = Hint("S = a1/(1-q) = 1/(1-1/2) = 2.")
            ),
            FractionAnswer(
                50301,
                "Suma nieskończonego szeregu geometrycznego: a1=3, q=1/3.",
                9,
                2,
                hint = Hint("S = 3/(1-1/3) = 3/(2/3) = 9/2.")
            ),
            TypeAnswer(50302, "Suma szeregu: a1=2, q=0.5.", 4, hint = Hint("S = 2/(1-0.5) = 4.")),
            select(
                50303,
                "Kiedy istnieje suma nieskończonego szeregu geometrycznego?",
                "|q| < 1",
                listOf("q > 1", "q = 1", "|q| > 1"),
                rng
            )
        ).shuffled(rng)
    }

    private fun combinatorics(seed: Long): List<Question> {
        val rng = Random(seed)

        return listOf(
            TypeAnswer(
                60100,
                "Ile jest permutacji 5 różnych elementów?",
                120,
                hint = Hint("5! = 5*4*3*2*1.")
            ),
            TypeAnswer(60101, "Ile sposobów wyboru 2 osób z 5?", 10, hint = Hint("C(5,2)=5*4/2.")),
            TypeAnswer(
                60102,
                "Ile jest trzycyfrowych kodów z cyfr 1,2,3 bez powtórzeń?",
                6,
                hint = Hint("3! = 6.")
            ),
            TypeAnswer(
                60103,
                "Ile sposobów ustawienia 4 książek na półce?",
                24,
                hint = Hint("4! = 24.")
            )
        ).shuffled(rng)
    }

    private fun bernoulli(seed: Long): List<Question> {
        val rng = Random(seed)

        return listOf(
            FractionAnswer(
                60200,
                "Rzucamy monetą 2 razy. Jakie jest prawdopodobieństwo dokładnie jednego orła?",
                1,
                2,
                hint = Hint("Są dwa sprzyjające wyniki: OR, RO z czterech możliwych.")
            ),
            FractionAnswer(
                60201,
                "Rzucamy monetą 3 razy. Prawdopodobieństwo dokładnie trzech orłów wynosi:",
                1,
                8,
                hint = Hint("Jedna sekwencja OOO z ośmiu możliwych.")
            ),
            FractionAnswer(
                60202,
                "Rzucamy kostką 2 razy. Prawdopodobieństwo dwóch szóstek wynosi:",
                1,
                36,
                hint = Hint("Każdy rzut ma szansę 1/6, więc 1/6 * 1/6.")
            ),
            FractionAnswer(
                60203,
                "Rzucamy monetą 3 razy. Prawdopodobieństwo dokładnie jednego orła wynosi:",
                3,
                8,
                hint = Hint("Wybieramy miejsce dla jednego orła: C(3,1)=3.")
            )
        ).shuffled(rng)
    }

    private fun conditionalProbability(seed: Long): List<Question> {
        val rng = Random(seed)

        return listOf(
            FractionAnswer(
                60300,
                "W klasie jest 12 dziewczyn, 8 chłopaków. 6 dziewczyn lubi fizykę. Losujemy dziewczynę. Prawdopodobieństwo, że lubi fizykę:",
                1,
                2,
                hint = Hint("Warunek zawęża zbiór do 12 dziewczyn. 6/12 = 1/2.")
            ),
            FractionAnswer(
                60301,
                "W pudełku są 3 kule czerwone i 2 niebieskie. Losujemy jedną kulę. Prawdopodobieństwo wylosowania czerwonej wynosi:",
                3,
                5,
                hint = Hint("Przed losowaniem 3 z 5 kul są czerwone.")
            ),
            FractionAnswer(
                60302,
                "W grupie 10 osób 4 uczą się rozszerzonej matematyki. Losujemy osobę z tej grupy. Prawdopodobieństwo:",
                2,
                5,
                hint = Hint("4/10 skracamy do 2/5.")
            ),
            FractionAnswer(
                60303,
                "Z talii liczb 1-10 wylosowano liczbę parzystą. Jakie jest prawdopodobieństwo, że jest większa od 5?",
                3,
                5,
                hint = Hint(
                    "Parzyste: 2,4,6,8,10. Większe od 5: 6,8,10, czyli 3/5.",
                    steps = listOf(
                        "Tu warunkiem są liczby parzyste.",
                        "Sprzyjające: 6, 8, 10.",
                        "Wynik: 3/5."
                    )
                )
            )
        ).shuffled(rng)
    }

    private fun derivativeRate(seed: Long): List<Question> {
        val rng = Random(seed)
        val questions = mutableListOf<Question>()

        repeat(8) { i ->
            val a = rng.nextInt(1, 6)
            val x = rng.nextInt(-3, 4)
            val value = 2 * a * x
            questions += TypeAnswer(
                70100 + i,
                "Dla f(x) = ${a}x^2 oblicz f'($x).",
                value,
                hint = Hint("Pochodna ${a}x^2 to ${2 * a}x.")
            )
        }

        return questions.shuffled(rng)
    }

    private fun extrema(seed: Long): List<Question> {
        val rng = Random(seed)
        val questions = mutableListOf<Question>()

        repeat(8) { i ->
            val p = rng.nextInt(-4, 5)
            val q = rng.nextInt(-6, 7)
            questions += TypeAnswer(
                70200 + i,
                "Funkcja f(x) = (x - ($p))^2 + ($q). Podaj argument minimum.",
                p,
                hint = Hint("Wierzchołek paraboli ma współrzędne ($p, $q), więc minimum jest dla x=$p.")
            )
        }

        return questions.shuffled(rng)
    }

    private fun tangent(seed: Long): List<Question> {
        val rng = Random(seed)
        val questions = mutableListOf<Question>()

        repeat(8) { i ->
            val x = rng.nextInt(-4, 5).let { if (it == 0) 2 else it }
            val slope = 2 * x
            val shapes = listOf(
                MathShape.FunctionPlot({ t -> t * t }, Color(0xFFE53935)),
                MathShape.PointMark(
                    Pt(x.toDouble(), (x * x).toDouble()),
                    label = "P",
                    color = Color(0xFFE53935)
                )
            )
            questions += GraphTypeAnswer(
                70300 + i,
                "Dla f(x)=x^2 podaj współczynnik kierunkowy stycznej w punkcie x=$x.",
                shapes,
                MathViewport(xMin = -6.0, xMax = 6.0, yMin = -2.0, yMax = 18.0),
                slope,
                hint = Hint("Współczynnik stycznej to wartość pochodnej. f'(x)=2x.")
            )
        }

        return questions.shuffled(rng)
    }
}
