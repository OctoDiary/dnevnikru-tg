fun main() {
    val env = System.getenv()
    val requiredVariables = listOf(
        "token",
        "db-host",
        "db-user",
        "db-password"
    )
    if (false !in requiredVariables.map { env.containsKey(it) }) {
        TelegramService(
            botToken = env["token"]!!,
            dbHost = env["db-host"]!!,
            dbUser = env["db-user"]!!,
            dbPassword = env["db-password"]!!,
        )
    } else {
        println(
            "No required environment variables. Aborting.\n" +
                    "Currently required variables: ${requiredVariables.joinToString(", ")}."
        )
    }
}