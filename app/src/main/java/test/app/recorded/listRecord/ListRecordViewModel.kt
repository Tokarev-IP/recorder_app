package test.app.recorded.listRecord

import androidx.lifecycle.ViewModel
import test.app.recorded.database.RecordDatabaseDao

class ListRecordViewModel(dataSource: RecordDatabaseDao): ViewModel() {

    val database = dataSource
    val records = database.getAllRecords()
}