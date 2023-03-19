import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.bot

class TelegramService(
    private val botToken: String
) {
    private var bot: Bot = bot {
        token = botToken
    }

    init {
        bot.startPolling()
    }

}