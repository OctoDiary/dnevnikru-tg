package db_models

import org.jetbrains.exposed.sql.Table

object Users : Table() {
    val telegramId = long("telegram_id")
    val accessToken = varchar("token", 32).nullable()
    val userId = long("user_id").nullable()
    val state = enumeration<State>("state").nullable()
    val tempUsername = varchar("temp_username", 255).nullable()
}
