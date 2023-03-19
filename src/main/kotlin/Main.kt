fun main() {
    val env = System.getenv()
    if (env.containsKey("token")) {
        TelegramService(env["token"]!!)
    } else {
        println("No token provided in env! Create env variable 'token'.")
    }
}