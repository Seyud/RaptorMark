package io.github.devriesl.raptormark.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.devriesl.raptormark.data.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BenchmarkViewModel @Inject constructor(
    private val settingSharedPrefs: SettingSharedPrefs,
    private val testRecordRepo: TestRecordRepo
) : ViewModel() {
    @Volatile
    private var forceStop = false

    var benchmarkState by mutableStateOf(BenchmarkState())
        private set

    var testItems: List<BenchmarkTest> = updateTestItems(false)

    fun onTestStart() {
        benchmarkState = benchmarkState.copy(
            running = true,
            score = 0
        )
        val testRecord = TestRecord()
        NativeHandler.postNativeThread {
            testItems.forEach {
                try {
                    if (forceStop) return@forEach

                    testRecord.setResult(it.testCase, it.runTest() ?: return@forEach)
                } catch (ex: Exception) {
                    Log.e(it.testCase.name, "Error running test", ex)
                    return@forEach
                }
            }
            val calculatedScore = testItems.sumOf { it.testCase.weight * (it.testResult?.calculateScore() ?: 0.0) }.toInt()
            val finalTestRecord = TestRecord(calculatedScore).apply {
                results = testRecord.results
            }
            viewModelScope.launch(Dispatchers.IO) {
                testRecordRepo.insertTestRecord(finalTestRecord)
            }
            forceStop = false
            benchmarkState = benchmarkState.copy(
                running = false,
                score = calculatedScore
            )
        }
    }

    fun onTestStop() {
        forceStop = true
    }

    private fun updateTestItems(isInitialized: Boolean = true): List<BenchmarkTest> {
        return TestCase.entries.map { testCase ->
            if (isInitialized) {
                testItems.find { it.testCase == testCase }
            } else {
                null
            } ?: BenchmarkTest(testCase, settingSharedPrefs)
        }
    }
}

data class BenchmarkState(
    val running: Boolean = false,
    val score: Int = 0
)
