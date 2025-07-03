package com.languageApp.wengu.data

import java.time.Instant

abstract class TestQuestion(
    val vocab : Vocab,
){
    private var timeStarted : Long = 0L
    var type : AnswerType  = if(RANDOM.nextBoolean()) AnswerType.TRANSLATION else AnswerType.PRONUNCIATION
    private var timeFinished = 0L
    fun start(){
        if(timeStarted == 0L){
            timeStarted = Instant.now().epochSecond
        }
    }
    fun finish(){
        timeFinished = Instant.now().epochSecond
    }
    fun getTimeStarted() : Long {
        return timeStarted
    }

    fun getTimeFinished() : Long {
        return if(timeFinished < timeStarted) timeStarted else timeFinished
    }
    abstract fun answerIsCorrect() : Boolean

    enum class AnswerType {
        PRONUNCIATION,
        TRANSLATION
    }
    class TrueFalse(vocab : Vocab, val other : Vocab) : TestQuestion(vocab) {
        var entry : Boolean? = null
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

        private fun checkPool(pool : List<Vocab>){
            if(pool.isEmpty()){
                throw NullPointerException("Pool of vocab is not filled.")
            }
        }
        fun getTestQuestion(pool : List<Vocab>) : TestQuestion {
            checkPool(pool)
            val target = pool.random()
            return getTestQuestion(pool, target)
        }
        fun getTestQuestion(pool : List<Vocab>, targetVocab : Vocab, setType : AnswerType? = null) : TestQuestion {
            checkPool(pool)
            return when(RANDOM.nextInt(2)){
                0 -> TrueFalse(targetVocab, if(RANDOM.nextBoolean()) pool.filter{it.id != targetVocab.id}.random() else targetVocab)
                else -> MultipleChoice(targetVocab, MultipleChoice.getMultipleAnswers(targetVocab, pool) + targetVocab)
            }.apply { if(setType!=null) this.type = setType }
        }
        fun getTestQuestions(pool : List<Vocab>, numQuestions : Int?) : List<TestQuestion> {
            checkPool(pool)
            val questionPool =
                pool.map { getTestQuestion(pool, it, AnswerType.TRANSLATION) } +
                pool.map { getTestQuestion(pool, it, AnswerType.PRONUNCIATION) }
            return questionPool.shuffled().take(numQuestions ?: questionPool.size)
        }
    }
}