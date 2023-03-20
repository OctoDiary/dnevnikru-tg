package db_models

import org.jetbrains.exposed.sql.Table

object Users : Table() {
    val telegramId = long("telegram_id")
    val accessToken = varchar("token", 32)
    val userId = long("user_id")
    val state = enumeration<State>("state")
}
