package com.example.calculator1

object FuelCalculator2 {

    data class FuelInput2(
        val H: Double,
        val C: Double,
        val S: Double,
        val O: Double,
        val Qdaf: Double, // теплота горючої маси
        val W: Double,    // волога робочої маси
        val A: Double,    // зола сухої маси
        val V: Double     // ванадій
    )

    data class FuelResult2(
        val Hr: Double,
        val Cr: Double,
        val Sr: Double,
        val Or: Double,
        val Ar: Double,
        val Vr: Double,
        val Qr: Double
    )

    fun calculate(input: FuelInput2): FuelResult2 {
        // коефіцієнт переходу горюча → робоча
        val k = (100 - input.W - input.A) / 100.0

        val Hr = input.H * k
        val Cr = input.C * k
        val Sr = input.S * k
        val Or = input.O * k
        val Ar = input.A * (100 - input.W) / 100.0
        val Vr = input.V * (100 - input.W) / 100.0

        val Qr = input.Qdaf * (100 - input.W - input.A) / 100.0

        return FuelResult2(Hr, Cr, Sr, Or, Ar, Vr, Qr)
    }
}
