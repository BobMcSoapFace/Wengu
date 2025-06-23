package com.languageApp.wengu.data

import androidx.compose.runtime.compositionLocalOf

data class Sort(
    val vocabSortType : SortType.Vocab,
    val testSortType : SortType.Test,
    val testResultSortType : SortType.TestResult,
){
    constructor() : this(
        vocabSortType = SortType.Vocab.DATE,
        testSortType = SortType.Test.DATE,
        testResultSortType = SortType.TestResult.DATE,
    )
    companion object {
        val localSort = compositionLocalOf { Sort() }
    }
}