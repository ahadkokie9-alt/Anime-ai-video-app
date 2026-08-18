package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface AnimeProjectDao {
    @Query("SELECT * FROM anime_projects ORDER BY createdAtTimestamp DESC")
    fun getAllProjects(): Flow<List<AnimeProjectEntity>>

    @Query("SELECT * FROM anime_projects WHERE id = :id")
    fun getProjectById(id: Long): Flow<AnimeProjectEntity?>

    @Query("SELECT * FROM anime_projects WHERE id = :id")
    suspend fun getProjectByIdDirect(id: Long): AnimeProjectEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProject(project: AnimeProjectEntity): Long

    @Update
    suspend fun updateProject(project: AnimeProjectEntity)

    @Query("DELETE FROM anime_projects WHERE id = :id")
    suspend fun deleteProject(id: Long)

    // Scenes
    @Query("SELECT * FROM anime_scenes WHERE projectId = :projectId ORDER BY sceneIndex ASC")
    fun getScenesForProject(projectId: Long): Flow<List<AnimeSceneEntity>>

    @Query("SELECT * FROM anime_scenes WHERE projectId = :projectId ORDER BY sceneIndex ASC")
    suspend fun getScenesForProjectDirect(projectId: Long): List<AnimeSceneEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScenes(scenes: List<AnimeSceneEntity>)

    @Query("DELETE FROM anime_scenes WHERE projectId = :projectId")
    suspend fun deleteScenesForProject(projectId: Long)

    // Character Voices
    @Query("SELECT * FROM anime_character_voices WHERE projectId = :projectId")
    fun getCharacterVoicesForProject(projectId: Long): Flow<List<AnimeCharacterVoiceEntity>>

    @Query("SELECT * FROM anime_character_voices WHERE projectId = :projectId")
    suspend fun getCharacterVoicesForProjectDirect(projectId: Long): List<AnimeCharacterVoiceEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharacterVoices(voices: List<AnimeCharacterVoiceEntity>)

    @Query("DELETE FROM anime_character_voices WHERE projectId = :projectId")
    suspend fun deleteCharacterVoicesForProject(projectId: Long)
}
