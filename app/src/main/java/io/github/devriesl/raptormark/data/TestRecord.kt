package io.github.devriesl.raptormark.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "test_records")
data class TestRecord(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Long,
    @ColumnInfo(name = "test_date") val timestamp: Long,
    @ColumnInfo(name = "score") val score: Int = 0
) {
    @ColumnInfo(name = "results")
    var results: Map<TestCase, String> = mapOf()

    fun setResult(testCase: TestCase, result: String) {
        results = results + mapOf(testCase to result)
    }

    constructor() : this(0L, System.currentTimeMillis(), 0)
    constructor(score: Int) : this(0L, System.currentTimeMillis(), score)
}
