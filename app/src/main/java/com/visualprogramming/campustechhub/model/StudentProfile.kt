package com.visualprogramming.campustechhub.model

import kotlinx.serialization.Serializable

@Serializable
data class StudentProfile(
    val name: String = "Aditya Pratama",
    val major: String = "Teknik Informatika",
    val batch: String = "Angkatan 2022",
    val nim: String = "22001045",
    val statusBadge: String = "MAHASISWA AKTIF",
    val avatarInitials: String = "AP"
)

