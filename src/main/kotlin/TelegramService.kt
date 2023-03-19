import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.dispatcher.Dispatcher
import com.github.kotlintelegrambot.dispatcher.command
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.entities.KeyboardReplyMarkup

class TelegramService(
    private val botToken: String
) {
    private var bot: Bot = bot {
        token = botToken
        dispatch { dispatcher() }
    }

    init {
        bot.startPolling()
    }

    context(Dispatcher)
    private fun dispatcher() {
        command("auth") {
            bot.sendMessage(ChatId.fromId(message.chat.id), "Выберите дневник", replyMarkup = KeyboardReplyMarkup.createSimpleKeyboard(
                listOf(
                    Server.values().map { it.serverName }
                )
            ))
        }
    }

}