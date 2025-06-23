package com.languageApp.wengu.data

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.Transaction
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface DatabaseDao {

    //Vocab
    @Transaction
    @Query("SELECT * FROM Vocab ORDER BY :type ASC")
    fun getVocabAsc(type : String): Flow<List<Vocab>>
    @Transaction
    @Query("SELECT * FROM Vocab ORDER BY :type DESC")
    fun getVocabDesc(type : String): Flow<List<Vocab>>
    @Transaction
    @Query("SELECT * FROM Vocab WHERE :type LIKE '%' || :findBy || '%'")
    fun getVocabByKeyword(type : String, findBy : String): Flow<List<Vocab>>
    @Transaction
    @Query("SELECT * FROM Vocab WHERE type LIKE '%' || :type || '%' AND vocabLanguage LIKE :language")
    fun getVocabByLanguageAndType(language : String, type : String): Flow<List<Vocab>>
    @Transaction
    @Query("SELECT * FROM Vocab WHERE :type LIKE :findBy")
    fun findVocabBy(type : String, findBy : String): Flow<List<Vocab>>

    @Delete(entity = Vocab::class)
    suspend fun deleteVocab(vocab: Vocab)
    @Upsert(entity = Vocab::class)
    suspend fun upsertVocab(vocab: Vocab)

    // Tests
    @Transaction
    @Query("SELECT * FROM Test ORDER BY :type ASC")
    fun getTestAsc(type : String): Flow<List<Test>>
    @Transaction
    @Query("SELECT * FROM Test ORDER BY :type DESC")
    fun getTestDesc(type : String): Flow<List<Test>>
    @Transaction
    @Query("SELECT * FROM Test WHERE :type LIKE :findBy")
    fun findTestBy(type : String, findBy : String): Flow<List<Test>>

    @Delete(entity = Test::class)
    suspend fun deleteTest(test: Test)
    @Delete(entity = Test::class)
    suspend fun deleteTests(tests : List<Test>)
    @Upsert(entity = Test::class)
    suspend fun upsertTest(test: Test)

    // Test results
    @Transaction
    @Query("SELECT * FROM TestResult WHERE vocabId LIKE :vocabId")
    fun findTestResultsByVocabId(vocabId : Int): Flow<List<TestResult>>
    @Transaction
    @Query("SELECT * FROM TestResult WHERE testId LIKE :testId")
    fun findTestResultsByTestId(testId : Int): Flow<List<TestResult>>

    @Delete(entity = TestResult::class)
    suspend fun deleteTestResult(testResult: TestResult)
    @Delete(entity = TestResult::class)
    suspend fun deleteTestResults(testResults : List<TestResult>)
    @Upsert(entity = TestResult::class)
    suspend fun upsertTestResult(testResult: TestResult)
    @Upsert(entity = TestResult::class)
    suspend fun upsertTestResult(testResults: List<TestResult>)

}
@Database(
    entities = [Vocab::class, TestResult::class, Test::class],
    version = 1, //increase when database entities change
    exportSchema = true,
    /*autoMigrations = [
        AutoMigration (from = 3, to = 4)
    ]*/
)
abstract class AppDatabase : RoomDatabase() {
    abstract val dao : DatabaseDao
    companion object {
        const val DATABASE_NAME = "calendar-database"
    }
}
