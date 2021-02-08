package test.app.recorded.listRecord

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import test.app.recorded.database.RecordDatabaseDao

class ListRecordViewModelFactory(
        private val dataSource: RecordDatabaseDao
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ListRecordViewModel::class.java)) {
            return ListRecordViewModel(dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}