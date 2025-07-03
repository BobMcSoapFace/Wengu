package com.languageApp.wengu.data

import java.time.Instant

data class TestState(
    val numQuestions : Int,
    val vocabs : List<Vocab>,
    val language : String,
    val useRecent : Boolean,
    val testIndex : Int,
){
    var submittable  = true
    val questions : List<TestQuestion> =
        if(!useRecent) TestQuestion.getTestQuestions(pool = vocabs, numQuestions = numQuestions)
        else vocabs.sortedByDescending { it.dateCreated }.take(numQuestions).map { TestQuestion.getTestQuestion(vocabs, it) }
    fun submit(
        onDataAction : (DataAction) -> Unit
    ) : Test {
        submittable = false
        questions.forEach{question ->
            onDataAction(DataAction.Upsert(TestResult(
                id = 0,
                dateTaken = question.getTimeStarted(),
                secondsTaken = (question.getTimeFinished()-question.getTimeStarted()).toInt(),
                correct = question.answerIsCorrect(),
                vocabId = question.vocab.id,
                testId = testIndex
            )))
        }
        val test = Test(
            id = testIndex,
            dateTaken = Instant.now().epochSecond,
            language = questions.first().vocab.vocabLanguage
        )
        onDataAction(DataAction.Upsert(test))
        return test
    }
}