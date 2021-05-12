package com.codinginflow.mvvmtodo.data.DAO

import androidx.room.*
import com.codinginflow.mvvmtodo.data.PreferencesManager
import com.codinginflow.mvvmtodo.data.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    fun getAllTasks(query: String, sortOrder: PreferencesManager.SortOrder, hideCompleted: Boolean): Flow<List<Task>> =
        when(sortOrder){
            PreferencesManager.SortOrder.BY_NAME -> getAllTaskSortedByName(query, hideCompleted)
            PreferencesManager.SortOrder.BY_DATE -> getAllTaskSortedByDate(query, hideCompleted)
        }

    @Query(" SELECT * FROM Task WHERE (isCompleted != :hideCompleted OR isCompleted = 0) AND name LIKE '%' || :searchQuery || '%' ORDER BY isImportant DESC, name ")
    fun getAllTaskSortedByName(searchQuery: String, hideCompleted: Boolean): Flow<List<Task>>

    @Query(" SELECT * FROM Task WHERE (isCompleted != :hideCompleted OR isCompleted = 0) AND name LIKE '%' || :searchQuery || '%' ORDER BY isImportant DESC, date ")
    fun getAllTaskSortedByDate(searchQuery: String, hideCompleted: Boolean): Flow<List<Task>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun Insert(task: Task)

    @Update
    suspend fun Update(task: Task)

    @Delete
    suspend fun Delete(task: Task)

}