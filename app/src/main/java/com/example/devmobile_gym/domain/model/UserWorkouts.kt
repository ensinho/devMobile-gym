package com.example.devmobile_gym.domain.model

data class UserWorkouts(
    val userId: String = "",
    val id: String = "",
    val workoutDates: List<String> = emptyList(),  // armazenado no Firestore como List<String>
    val currentStreak: Int = 0,
    val maxStreak: Int = 0,
    val lastWorkoutDate: String? = null  // formato "yyyy-MM-dd"
)