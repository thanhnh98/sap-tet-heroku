package com.tlife.model

import java.lang.Exception

data class MatchModel(
    var playground: MutableList<MutableList<Boolean>> = mutableListOf(
        mutableListOf(false, false, false),
        mutableListOf(false, false, false),
        mutableListOf(false, false, false),
    ),
    var isWon: Boolean = false
){
    fun checkIn(stepModel: StepModel): MatchModel{
        playground[stepModel.x][stepModel.y] = stepModel.value
        isWon = hadWonOnLastStep()
        return this
    }

    fun hadWonOnLastStep(): Boolean{
       return validateColumn() || validateRow() || validateDiagonal()
    }

    private fun validateRow(): Boolean{
        return (playground[0][0] && playground[0][1] && playground[0][2])
                || (playground[1][0] && playground[1][1] && playground[1][2])
                || (playground[2][0] && playground[2][1] && playground[2][2])
    }

    private fun validateColumn(): Boolean{
        return (playground[0][0] && playground[1][0] && playground[2][0])
                || (playground[0][1] && playground[1][1] && playground[2][1])
                || (playground[0][2] && playground[1][2] && playground[2][2])
    }

    private fun validateDiagonal(): Boolean{
        return (playground[0][0] && playground[1][1] && playground[2][2])
                || (playground[2][0] && playground[1][1] && playground[0][2])
    }
}

data class StepModel(
    val x: Int,
    val y: Int,
    var value: Boolean = false
)