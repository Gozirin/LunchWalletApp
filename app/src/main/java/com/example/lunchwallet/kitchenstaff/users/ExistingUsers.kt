package com.example.lunchwallet.kitchenstaff.users

data class ExistingUsers(
    val name: String,
    val stack: String,
    val location: String
) {
    companion object{
        val userList = arrayListOf<ExistingUsers>(
            ExistingUsers("John Doe", "Android", "Farah Park"),
            ExistingUsers("John Doe", "Android", "Farah Park"),
            ExistingUsers("John Doe", "Android", "Farah Park"),
            ExistingUsers("John Doe", "Android", "Farah Park"),
            ExistingUsers("John Doe", "Android", "Farah Park"),
            ExistingUsers("John Doe", "Android", "Farah Park"),
            ExistingUsers("John Doe", "Android", "Farah Park"),
            ExistingUsers("John Doe", "Android", "Farah Park"),
            ExistingUsers("John Doe", "Android", "Farah Park"),
            ExistingUsers("John Doe", "Android", "Farah Park"),
            ExistingUsers("John Doe", "Android", "Farah Park"),
            ExistingUsers("John Doe", "Android", "Farah Park")
        )
    }
}
