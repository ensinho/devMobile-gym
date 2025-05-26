package com.example.devmobile_gym.domain.repository

import com.example.devmobile_gym.domain.model.UserWorkouts

interface UserWorkoutsRepositoryModel {
    suspend fun criarStreak(userId: String)
    suspend fun atualizarStreakDoAluno(userWorkouts: UserWorkouts)
    suspend fun obterStreakPorUserId(userId: String): UserWorkouts?
}