import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.dispatcher.Dispatcher
import handlers.commandAuth
import handlers.commandDiary
import org.jetbrains.exposed.sql.Database

class TelegramService(
    private val botToken: String,
    dbHost: String,
    dbUser: String,
    dbPassword: String
) {
    private val bot: Bot = bot {
        token = botToken
        dispatch { dispatcher() }
    }

    init {
        bot.startPolling()
        Database.connect(
            url = dbHost,
            user = dbUser,
            password = dbPassword,
            driver = "org.postgresql.Driver"
        )
    }

    context(Dispatcher)
    private fun dispatcher() {
        commandAuth()
        commandDiary()
    }

}