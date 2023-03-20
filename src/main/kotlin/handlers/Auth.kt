package handlers

import Server
import Utils.answer
import Utils.f
import Utils.isMine
import Utils.userState
import com.github.kotlintelegrambot.dispatcher.Dispatcher
import com.github.kotlintelegrambot.dispatcher.command
import com.github.kotlintelegrambot.dispatcher.message
import com.github.kotlintelegrambot.entities.KeyboardReplyMarkup
import db_models.State

context(Dispatcher)
fun commandAuth() {
    val serversKeyboardReplyMarkup = KeyboardReplyMarkup.createSimpleKeyboard(
        listOf(
            Server.values().map { it.serverName }
        )
    )

    command("auth") {
        if (userState != null) return@command

        answer(
            "Выберите дневник", replyMarkup = serversKeyboardReplyMarkup
        )
        return@command
    }

    message(f { userState == State.Authenticating }) {
        if (message.replyToMessage?.isMine(bot) != true) return@message
        if (message.text !in Server.values().map { it.serverName }) {
            answer("Неверное имя сервера!", replyMarkup = serversKeyboardReplyMarkup)
            return@message
        }
        answer("auth...")
    }
}
