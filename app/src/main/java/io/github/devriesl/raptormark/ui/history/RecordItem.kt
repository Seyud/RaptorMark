package io.github.devriesl.raptormark.ui.history

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.github.devriesl.raptormark.R
import io.github.devriesl.raptormark.data.BenchmarkTest
import io.github.devriesl.raptormark.data.TestRecord
import io.github.devriesl.raptormark.ui.benchmark.TestItemList
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun RecordItem(
    testRecord: TestRecord
) {
    var expandState by rememberSaveable { mutableStateOf(false) }
    val date = SimpleDateFormat(
        stringResource(R.string.test_record_date_format),
        Locale.getDefault()
    ).format(Date(Timestamp(testRecord.timestamp).time))

    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clickable { expandState = !expandState }
                .height(64.dp)
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = date,
                    color = if (expandState) MaterialTheme.colorScheme.primary else LocalContentColor.current,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = stringResource(R.string.calculated_scores_result_format, testRecord.score),
                    color = LocalContentColor.current.copy(0.7f),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            val rotate by animateFloatAsState(targetValue = if (expandState) 180f else 0f)
            Icon(
                imageVector = Icons.Outlined.KeyboardArrowDown,
                tint = LocalContentColor.current.copy(0.6f),
                contentDescription = null,
                modifier = Modifier.graphicsLayer {
                    rotationZ = rotate
                }
            )
        }

        AnimatedVisibility(expandState) {
            TestItemList(testRecord.results.mapValues { (_, result) ->
                BenchmarkTest.parseResult(result)
            })
        }
    }
}