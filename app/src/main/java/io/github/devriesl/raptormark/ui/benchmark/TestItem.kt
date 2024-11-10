package io.github.devriesl.raptormark.ui.benchmark

import androidx.compose.runtime.Composable
import io.github.devriesl.raptormark.data.*

@Composable
fun TestItem(
    testCase: TestCases,
    result: String?
) {
    when {
        testCase.isMBW() -> {
            val testResult = result?.let { MBWTest.parseResult(it) }
            MBWTestItem(
                title = testCase.title,
                bandwidth = testResult?.bandwidth,
                vectorBandwidth = testResult?.vectorBandwidth,
                isAppPerf = testCase.isMBWApp()
            )
        }
        testCase.isCoreLatency() -> {
            val testResult = result?.let { CoreLatencyTest.parseResult(it) }
            CoreLatencyTestItem(
                title = testCase.title,
                latencies = testResult?.latenciesMap,
                progress = testResult?.progress
            )
        }
        testCase.isFIO() -> {
            val testResult = result?.let { FIOTest.parseResult(it) }
            FIOTestItem(
                title = testCase.title,
                bandwidth = testResult?.bandwidth,
                showLatency = testCase.isFIORand(),
                latency = testResult?.latency
            )
        }
    }
}
