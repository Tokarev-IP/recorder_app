package test.app.recorded

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import junit.framework.Assert.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import test.app.recorded.database.RecordDatabase
import test.app.recorded.database.RecordDatabaseDao
import test.app.recorded.database.RecordingItem
import java.io.IOException
import java.lang.Exception

@RunWith(AndroidJUnit4::class)
// - используем библиотеку AndroidJUnit
class RecordDatabaseTest {

    private lateinit var recordDatabaseDao: RecordDatabaseDao
    private lateinit var database: RecordDatabase

    @Before
// - обеспечивает необходимую подготовку перед тестами
    fun createDb(){
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(context, RecordDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        recordDatabaseDao = database.recordDatabaseDao
    }

    @After
    // - закрытие бд
    @Throws(IOException::class)
    fun closeDb(){
        database.close()
    }

    @Test
    @Throws(Exception::class)
    fun testDatabase(){
        recordDatabaseDao.insert(RecordingItem())
        val getCount= recordDatabaseDao.getCount()
        assertEquals(getCount,1)
    }


}