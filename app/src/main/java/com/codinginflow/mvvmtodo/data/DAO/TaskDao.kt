package com.codinginflow.mvvmtodo.data.DAO

import androidx.room.*
import com.codinginflow.mvvmtodo.data.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Query(" SELECT * FROM Task WHERE name LIKE '%' || :searchQuery || '%' ORDER BY isImportant DESC ")
    fun getAllTasks(searchQuery: String): Flow<List<Task>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun Insert(task: Task)

    @Update
    suspend fun Update(task: Task)

    @Delete
    suspend fun Delete(task: Task)

}