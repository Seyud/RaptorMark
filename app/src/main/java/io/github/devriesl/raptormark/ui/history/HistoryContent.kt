package io.github.devriesl.raptormark.ui.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.github.devriesl.raptormark.R
import io.github.devriesl.raptormark.viewmodels.HistoryViewModel

@Composable
fun HistoryContent(
    historyViewModel: HistoryViewModel,
    isWidthCompact: Boolean
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column {
            // Sort button
            Row(
                modifier = Modifier.padding(
                    horizontal = if (isWidthCompact) 8.dp else 16.dp,
                    vertical = 8.dp
                ),
                horizontalArrangement = Arrangement.End
            ) {
                Spacer(modifier = Modifier.weight(1f))
                FilledTonalButton(
                    onClick = { historyViewModel.toggleSortOrder() }
                ) {
                    Text(
                        text = if (historyViewModel.sortByScore) {
                            stringResource(R.string.sort_by_score)
                        } else {
                            stringResource(R.string.sort_by_date)
                        }
                    )
                }
            }

            LazyColumn(
                contentPadding = PaddingValues(
                    vertical = 8.dp,
                    horizontal = if (isWidthCompact) {
                        8.dp
                    } else {
                        16.dp
                    }
                ),
                verticalArrangement = if (isWidthCompact) {
                    Arrangement.Top
                } else {
                    Arrangement.spacedBy(8.dp)
                },
                modifier = Modifier.fillMaxHeight()
            ) {
                items(historyViewModel.testRecords) { testRecord ->
                    if (isWidthCompact) {
                        RecordItem(testRecord)
                        HorizontalDivider()
                    } else {
                        Card {
                            RecordItem(testRecord)
                        }
                    }
                }
            }
        }
    }
}
