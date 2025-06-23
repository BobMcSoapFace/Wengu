package com.languageApp.wengu.data

interface SortType {
    enum class Vocab : SortType {
        ID,
        DATE,
        TRANSLATION,
        VOCAB,
        VOCAB_LENGTH,
        TYPE,
    }
    enum class Test : SortType {
        ID,
        DATE,
        LANGUAGE,
    }
    enum class TestResult : SortType {
        ID,
        DATE,
        CORRECT,
        SECONDS_TAKEN,
        BY_TEST,
    }
}