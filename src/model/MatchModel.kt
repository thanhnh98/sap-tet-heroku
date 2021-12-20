package com.tlife.model

import java.lang.Exception

data class MatchModel(
    var playground: MutableList<MutableList<String>> = mutableListOf(
        mutableListOf("", "", ""),
        mutableListOf("", "", ""),
        mutableListOf("", "", ""),
    ),
    var isWon: Boolean = false
){
    fun checkIn(stepModel: StepModel): MatchModel{
        if (stepModel.value.isEmpty())
            return this

        playground[stepModel.x][stepModel.y] = stepModel.value
        isWon = hadWonOnLastStep(stepModel)
        return this
    }

    fun hadWonOnLastStep(stepModel: StepModel): Boolean{
       return validateColumn(stepModel.value) || validateRow(stepModel.value) || validateDiagonal(stepModel.value)
    }

    private fun validateRow(value: String): Boolean{
        return (playground[0][0] == value && playground[0][1] == value && playground[0][2] == value)
                || (playground[1][0] == value && playground[1][1] == value && playground[1][2] == value)
                || (playground[2][0] == value && playground[2][1] == value && playground[2][2] == value)
    }

    private fun validateColumn(value: String): Boolean{
        return (playground[0][0] == value && playground[1][0] == value && playground[2][0] == value)
                || (playground[0][1] == value && playground[1][1] == value && playground[2][1] == value)
                || (playground[0][2] == value && playground[1][2] == value && playground[2][2] == value)
    }

    private fun validateDiagonal(value: String): Boolean{
        return (playground[0][0] == value && playground[1][1] == value && playground[2][2] == value)
                || (playground[2][0] == value && playground[1][1] == value && playground[0][2] == value)
    }
}

data class StepModel(
    val x: Int,
    val y: Int,
    var value: String = "",
    val name: String
)