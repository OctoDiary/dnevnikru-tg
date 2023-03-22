package handlers

import BaseCallback
import NetworkService
import Server
import Utils.ChatType
import Utils.answer
import Utils.chatTypeEnum
import Utils.f
import Utils.getField
import Utils.setUserState
import Utils.updateField
import Utils.updateUserField
import Utils.userState
import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.dispatcher.*
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.entities.InlineKeyboardMarkup
import com.github.kotlintelegrambot.entities.KeyboardReplyMarkup
import com.github.kotlintelegrambot.entities.ReplyKeyboardRemove
import com.github.kotlintelegrambot.entities.inlinequeryresults.InlineQueryResult
import com.github.kotlintelegrambot.entities.inlinequeryresults.InputMessageContent
import com.github.kotlintelegrambot.entities.keyboard.InlineKeyboardButton
import db_models.State
import db_models.Users
import retrofit2.Call

context(Dispatcher)
fun commandAuth() {
    val serversKeyboardReplyMarkup = KeyboardReplyMarkup.createSimpleKeyboard(
        listOf(
            Server.values().map { it.serverName }
        )
    )

    val allowedAuthChatTypes = listOf(
        ChatType.bot,
        ChatType.private
    )

    command("auth") {

        if (message.chatTypeEnum !in allowedAuthChatTypes || userState != null) return@command

        userState = State.AuthSelectServer
        answer(
            "Выберите дневник", replyMarkup = serversKeyboardReplyMarkup
        )
        return@command
    }

    message(f { userState == State.AuthSelectServer && (chatTypeEnum == ChatType.bot || chatTypeEnum == ChatType.private) }) {
        if (message.text !in Server.values().map { it.serverName }) {
            answer("Неверное имя сервера!", replyMarkup = serversKeyboardReplyMarkup)
            return@message
        }
        updateField(Users.server, Server.values().first { it.serverName == message.text })
        answer("Хорошо! Введите имя пользователя.", replyMarkup = ReplyKeyboardRemove())
        userState = State.AuthEnterUsername
    }

    message(f { userState == State.AuthEnterUsername && (chatTypeEnum == ChatType.bot || chatTypeEnum == ChatType.private) }) {
        updateField(Users.tempUsername, message.text)
        answer(
            "Введите пароль (Вы также можете начать вводить @${
                bot.getMe().get().username
            }, а затем следовать инструкциям, чтобы пароль не остался в истории сообщений)"
        )
        userState = State.AuthEnterPassword
    }

    fun startAuth(username: String, password: String, server: Server, telegramUserId: Long, bot: Bot) {
        val call: Call<NetworkService.AuthResult> = NetworkService.api(server).auth(
            NetworkService.AuthRequestBody(
                username,
                password,
                server.clientId,
                server.clientSecret,
                "CommonInfo,EducationalInfo",
            )
        )
        val chatId = ChatId.fromId(telegramUserId)

        val handleAuthError: (NetworkService.DnevnikError) -> Unit = {
            if (it.reason == "HaveNotActiveMemberships") {
                bot.sendMessage(chatId, "Ребёнок не участвует в учебном процессе")
            } else {
                bot.sendMessage(chatId, it.description)
                setUserState(telegramUserId, State.AuthSelectServer)
                bot.sendMessage(chatId, "Выберите дневник", replyMarkup = serversKeyboardReplyMarkup)
            }
        }
        call.enqueue(object : BaseCallback<NetworkService.AuthResult>(
            okHandler = {
                if (it.credentials != null && it.type == "Success") {
                    updateUserField(telegramUserId, Users.accessToken, it.credentials.accessToken)
                    updateUserField(telegramUserId, Users.userId, it.credentials.userId)
                    setUserState(telegramUserId, State.Authorized)

                    bot.sendMessage(chatId, "Успешная авторизация!")
                } else handleAuthError(NetworkService.DnevnikError(it.reason, it.type, it.description))
            },
            errorHandler = {
                if (it?.reason == "HaveNotActiveMemberships") {
                    bot.sendMessage(chatId, "Ребёнок не участвует в учебном процессе")
                } else handleAuthError(it!!)
            },
            failureHandler = {
                bot.sendMessage(
                    chatId,
                    "Извините, произошла незвестная ошибка (auth.failureHandler). Пожалуйста, начните авторизацию заново: /auth."
                )
            }
        ) {})
    }

    message(f { userState == State.AuthEnterPassword && (chatTypeEnum == ChatType.bot || chatTypeEnum == ChatType.private) && viaBot == null}) {
        startAuth(
            getField(Users.tempUsername)!!,
            message.text!!,
            getField(Users.server)!!,
            message.from!!.id,
            bot
        )
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

    command("logout") {
        if (userState != State.Authorized) return@command

        updateUserField(message.from!!.id, Users.userId, null)
        updateUserField(message.from!!.id, Users.accessToken, null)
        updateUserField(message.from!!.id, Users.state, null)
        updateUserField(message.from!!.id, Users.server, null)
        updateUserField(message.from!!.id, Users.tempUsername, null)

        answer("Вы успешно вышли! Заного авторизоваться можно командой /auth.")
    }

    callbackQuery {
        if (callbackQuery.data.startsWith("password-") && userState == State.AuthEnterPassword) {
            bot.editMessageText(inlineMessageId = callbackQuery.inlineMessageId, text = "Идёт проверка...")
            startAuth(
                getField(Users.tempUsername)!!,
                callbackQuery.data.removePrefix("password-"),
                getField(Users.server)!!,
                callbackQuery.from.id,
                bot
            )
        }
    }
}
