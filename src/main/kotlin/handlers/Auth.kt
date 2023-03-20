package handlers

import Server
import Utils.answer
import Utils.f
import Utils.getField
import Utils.updateField
import Utils.userState
import com.github.kotlintelegrambot.dispatcher.*
import com.github.kotlintelegrambot.entities.InlineKeyboardMarkup
import com.github.kotlintelegrambot.entities.KeyboardReplyMarkup
import com.github.kotlintelegrambot.entities.ReplyKeyboardRemove
import com.github.kotlintelegrambot.entities.inlinequeryresults.InlineQueryResult
import com.github.kotlintelegrambot.entities.inlinequeryresults.InputMessageContent
import com.github.kotlintelegrambot.entities.keyboard.InlineKeyboardButton
import db_models.State
import db_models.Users

context(Dispatcher)
fun commandAuth() {
    val serversKeyboardReplyMarkup = KeyboardReplyMarkup.createSimpleKeyboard(
        listOf(
            Server.values().map { it.serverName }
        )
    )

    command("auth") {
        if (userState != null) return@command

        userState = State.AuthSelectServer
        answer(
            "Выберите дневник", replyMarkup = serversKeyboardReplyMarkup
        )
        return@command
    }

    message(f { userState == State.AuthSelectServer }) {
        if (message.text !in Server.values().map { it.serverName }) {
            answer("Неверное имя сервера!", replyMarkup = serversKeyboardReplyMarkup)
            return@message
        }
        answer("Хорошо! Введите имя пользователя.", replyMarkup = ReplyKeyboardRemove())
        userState = State.AuthEnterUsername
    }

    message(f { userState == State.AuthEnterUsername }) {
        updateField(Users.tempUsername, message.text)
        answer(
            "Введите пароль (Вы также можете начать вводить @${
                bot.getMe().get().username
            }, а затем следовать инструкциям, чтобы пароль не остался в истории сообщений)"
        )
        userState = State.AuthEnterPassword
    }

    fun startAuth(username: String, password: String) {
        // ...
    }

    message(f { userState == State.AuthEnterPassword }) {
        startAuth(getField(Users.tempUsername)!!, message.text!!)
    }

    inlineQuery {
        if (userState != State.AuthEnterPassword) {
            // other inline implementations

            return@inlineQuery
        }
        val result = InlineQueryResult.Article(
            id = "password",
            title = "Ввести пароль",
            description = "Введите пароль в строку набора сообщения. Таким образом, он не появится в истории сообщений",
            inputMessageContent = InputMessageContent.Text("Нажмите кнопку внизу:"),
            replyMarkup = InlineKeyboardMarkup.create(
                listOf(
                    InlineKeyboardButton.CallbackData(
                        "Войти",
                        "password-${inlineQuery.query}"
                    )
                )
            )
        )
        bot.answerInlineQuery(inlineQuery.id, result)
    }

    callbackQuery {
        if (userState != State.AuthEnterPassword) return@callbackQuery
        startAuth(getField(Users.tempUsername)!!, callbackQuery.data.removePrefix("password-"))
    }
}
