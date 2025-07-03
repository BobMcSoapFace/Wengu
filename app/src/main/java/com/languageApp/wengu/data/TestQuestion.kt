package com.languageApp.wengu.data

import java.time.Instant

abstract class TestQuestion(
    val vocab : Vocab,
    val timeStarted : Long = Instant.now().epochSecond,
){
    val type : AnswerType  = if(RANDOM.nextBoolean()) AnswerType.TRANSLATION else AnswerType.PRONUNCIATION
    private var timeFinished = 0L
    fun finish(){
        timeFinished = Instant.now().epochSecond
    }
    fun getFinished() : Boolean {
        return timeFinished >= timeStarted
    }
    fun getTimeFinished() : Long {
        return timeFinished
    }
    abstract fun answerIsCorrect() : Boolean

    enum class AnswerType {
        PRONUNCIATION,
        TRANSLATION
    }
    class TrueFalse(vocab : Vocab, private val other : Vocab) : TestQuestion(vocab) {
        var entry : Boolean? = false
        private fun getAnswer() : Boolean{
            return (when(type){
                AnswerType.PRONUNCIATION -> other.pronunciation
                AnswerType.TRANSLATION -> other.translation
            }).trim().lowercase() == (when(type){
                AnswerType.PRONUNCIATION -> vocab.pronunciation
                AnswerType.TRANSLATION -> vocab.translation
            }).trim().lowercase()
        }
        override fun answerIsCorrect() : Boolean {
            return entry == getAnswer()
        }
    }
    class MultipleChoice(vocab : Vocab, val answers : List<Vocab>) : TestQuestion(vocab){
        var entry : String? = null
        private fun getAnswer() : String {
            return when(type){
                AnswerType.PRONUNCIATION -> vocab.pronunciation
                AnswerType.TRANSLATION -> vocab.translation
            }
        }
        override fun answerIsCorrect() : Boolean {
            return entry == getAnswer()
        }
        companion object {
            const val MULTIPLE_CHOICE_NUM = 4
            fun getMultipleAnswers(target : Vocab, pool : List<Vocab>) : List<Vocab>{
                return pool.filter { it.id != target.id }.shuffled().take(MULTIPLE_CHOICE_NUM-1)
            }
        }
    }
    companion object  {
        private val RANDOM = java.util.Random(Instant.now().epochSecond)

        fun getTestQuestion(pool : List<Vocab>) : TestQuestion {
            if(pool.isEmpty()){
                throw NullPointerException("Pool of vocab is not filled.")
            }
            val target = pool.random()
            return when(RANDOM.nextInt(2)){
                0 -> TrueFalse(target, pool.filter{it.id != target.id}.random())
                else -> MultipleChoice(target, MultipleChoice.getMultipleAnswers(target, pool) + target)
            }
        }
        fun getTestQuestion(pool : List<Vocab>, targetVocab : Vocab) : TestQuestion {
            if(pool.isEmpty()){
                throw NullPointerException("Pool of vocab is not filled.")
            }
            return when(RANDOM.nextInt(2)){
                0 -> TrueFalse(targetVocab, pool.filter{it.id != targetVocab.id}.random())
                else -> MultipleChoice(targetVocab, MultipleChoice.getMultipleAnswers(targetVocab, pool))
            }
        }
    }
}