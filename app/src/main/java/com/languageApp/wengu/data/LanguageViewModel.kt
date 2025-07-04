package com.languageApp.wengu.data

import android.app.Application
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.languageApp.wengu.data.settings.UserSettings
import com.languageApp.wengu.data.settings.UserSettingsData
import com.languageApp.wengu.ui.AnimateState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class LanguageViewModel(
    private val application : Application,
    private val db : AppDatabase,
) : ViewModel() {
    private val _vocabSortType = MutableStateFlow(SortType.Vocab.ID)
    private val _testSortType = MutableStateFlow(SortType.Test.ID)
    private val _testResultSortType = MutableStateFlow(SortType.TestResult.ID)
    private val _animateState = MutableStateFlow(AnimateState())
    private val _vocab : Flow<List<Vocab>> = _vocabSortType.flatMapLatest {
        when(_vocabSortType.value) {
            SortType.Vocab.VOCAB -> db.dao.getVocabAsc(type = Vocab.Properties.VOCAB.label)
            SortType.Vocab.TYPE -> db.dao.getVocabAsc(type = Vocab.Properties.TYPE.label)
            SortType.Vocab.DATE -> db.dao.getVocabDesc(type = Vocab.Properties.DATE.label)
            else -> db.dao.getVocabDesc(type = Vocab.Properties.ID.label)
        }
    }
    private val _tests : Flow<List<Test>> = _testSortType.flatMapLatest {
        when(_testSortType.value) {
            SortType.Test.ID -> db.dao.getTestDesc(type = Test.Properties.ID.label)
            SortType.Test.LANGUAGE -> db.dao.getTestAsc(type = Test.Properties.LANGUAGE.label)
            SortType.Test.DATE -> db.dao.getTestDesc(type = Test.Properties.DATE.label)
        }
    }
    private val _testResults : Flow<List<TestResult>> = _testSortType.flatMapLatest {
        when(_testResultSortType.value) {
            SortType.TestResult.BY_TEST -> db.dao.getTestResultsDesc(type = TestResult.Properties.TEST.label)
            SortType.TestResult.SECONDS_TAKEN -> db.dao.getTestResultsDesc(type = TestResult.Properties.SECONDS.label)
            SortType.TestResult.ID -> db.dao.getTestResultsDesc(type = TestResult.Properties.ID.label)
            else -> db.dao.getTestResultsDesc(type = TestResult.Properties.DATE.label)
        }
    }
    val activeTestState : MutableState<TestState?> = mutableStateOf(null)
    val userSettings: UserSettingsData = UserSettingsData(application.applicationContext)
    val userSettingsState = userSettings.getSettingsData().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UserSettings())
    val animationState =_animateState.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AnimateState())
    val vocabState = _vocab.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), listOf())
    val testState = _tests.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), listOf())
    val testResultsState = _testResults.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), listOf())
    val sortTypeState = _testResultSortType.combine(_testSortType){testResult, test ->
        Sort(
            vocabSortType = SortType.Vocab.DATE,
            testSortType = test,
            testResultSortType = testResult,
        )
    }.combine(_vocabSortType){sort, vocab ->
        sort.copy(vocabSortType = vocab)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000), Sort()
    )
    val editingVocab : MutableState<Vocab?> = mutableStateOf(null)
    val viewingVocab : MutableState<Vocab?> = mutableStateOf(null)

    suspend fun setAnimateState(animateState: AnimateState){
        _animateState.emit(animateState)
    }

    suspend fun getTestResults(data : DataEntry) : List<TestResult> {
        return when(data){
            is Test -> {
                db.dao.findTestResultsByTestId(data.id).first()
            }
            is Vocab -> {
                db.dao.findTestResultsByVocabId(data.id).first()
            }
            else -> {
                throw IllegalArgumentException("DataEntry class not accepted for getTestResults (only Test and Vocab)")
            }
        }
    }
    fun onDataAction(action : DataAction){
        viewModelScope.launch {
            when(action){
                is DataAction.Upsert -> {
                    when(action.dataObj){
                        is Vocab  -> {
                            db.dao.upsertVocab(action.dataObj)
                        }
                        is TestResult -> {
                            db.dao.upsertTestResult(action.dataObj)
                        }
                        is Test -> {
                            db.dao.upsertTest(action.dataObj)
                        }
                    }
                }
                is DataAction.Delete -> {
                    when(action.dataObj){
                        is Vocab  -> {
                            db.dao.deleteVocab(action.dataObj)
                        }
                        is TestResult -> {
                            db.dao.deleteTestResult(action.dataObj)
                        }
                        is Test -> {
                            db.dao.deleteTest(action.dataObj)
                        }
                    }
                }
                is DataAction.Sort -> {
                    when(action.type){
                        is SortType.Vocab -> {
                            _vocabSortType.emit(action.type)
                        }
                        is SortType.Test -> {
                            _testSortType.emit(action.type)
                        }
                        is SortType.TestResult -> {
                            _testResultSortType.emit(action.type)
                        }
                    }
                }
                else -> {}
            }
        }
    }
}