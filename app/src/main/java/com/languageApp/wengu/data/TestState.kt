package com.languageApp.wengu.data

import java.time.Instant

data class TestState(
    val numQuestions : Int,
    val vocabs : List<Vocab>,
    val language : String,
    val useRecent : Boolean,
){
    val questions : List<TestQuestion> =
        if(!useRecent) arrayOfNulls<TestQuestion>(numQuestions).map{ TestQuestion.getTestQuestion(vocabs) }
        else vocabs.sortedByDescending { it.dateCreated }.take(numQuestions).map { TestQuestion.getTestQuestion(vocabs, it) }
    fun submit(
        testIndex : Int,
        onDataAction : (DataAction) -> Unit
    ) : Test {
        questions.forEach{question ->
            onDataAction(DataAction.Upsert(TestResult(
                id = 0,
                dateTaken = question.getTimeFinished(),
                secondsTaken = (question.getTimeFinished()-question.timeStarted).toInt(),
                correct = question.answerIsCorrect(),
                vocabId = question.vocab.id,
                testId = testIndex
            )))
        }
        return Test(
            id = testIndex,
            dateTaken = Instant.now().epochSecond,
            language = questions.first().vocab.vocabLanguage
        )
    }
}