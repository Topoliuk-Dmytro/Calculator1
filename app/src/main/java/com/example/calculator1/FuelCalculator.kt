package com.example.calculator1

object FuelCalculator {

    data class FuelInput(
        val H: Double, // водень
        val C: Double, // вуглець
        val S: Double, // сірка
        val N: Double, // азот
        val O: Double, // кисень
        val W: Double, // волога
        val A: Double  // зола
    )

    data class FuelResult(
        val krs: Double, // коеф. до сухої
        val krg: Double, // коеф. до горючої
        val dry: Map<String, Double>,
        val combustible: Map<String, Double>,
        val Qr: Double,   // теплота робоча (МДж/кг)
        val Qd: Double,   // теплота суха (МДж/кг)
        val Qdaf: Double  // теплота горюча (МДж/кг)
    )

    fun calculate(input: FuelInput): FuelResult {
        // коефіцієнти
        val krs = 100.0 / (100.0 - input.W)
        val krg = 100.0 / (100.0 - input.W - input.A)

        // склад сухої маси
        val dry = mapOf(
            "H" to input.H * krs,
            "C" to input.C * krs,
            "S" to input.S * krs,
            "N" to input.N * krs,
            "O" to input.O * krs,
            "A" to input.A * krs
        )

        // склад горючої маси
        val combustible = mapOf(
            "H" to input.H * krg,
            "C" to input.C * krg,
            "S" to input.S * krg,
            "N" to input.N * krg,
            "O" to input.O * krg
        )

        // теплота робочої маси (кДж/кг)
        val Qr_kJ = 339 * input.C + 1030 * input.H - 108.8 * (input.O - input.S) - 25 * input.W
        val Qr = Qr_kJ / 1000.0 // у МДж/кг

        // теплота сухої (табл. 1.2)
        val Qd = Qr * 100 / (100 - input.W)

        // теплота горючої (табл. 1.2)
        val Qdaf = Qr * 100 / (100 - input.W - input.A)

        return FuelResult(krs, krg, dry, combustible, Qr, Qd, Qdaf)
    }
}
