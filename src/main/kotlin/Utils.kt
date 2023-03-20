import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.dispatcher.handlers.CommandHandlerEnvironment
import com.github.kotlintelegrambot.dispatcher.handlers.MessageHandlerEnvironment
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.entities.Message
import com.github.kotlintelegrambot.entities.ParseMode
import com.github.kotlintelegrambot.entities.ReplyMarkup
import com.github.kotlintelegrambot.extensions.filters.Filter
import db_models.State
import db_models.Users
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object Utils {

    var MessageHandlerEnvironment.userState: State?
        get() = getUserState(message.from!!.id)
        set(value) = setUserState(message.from!!.id, value)

    var CommandHandlerEnvironment.userState: State?
        get() = getUserState(message.from!!.id)
        set(value) = setUserState(message.from!!.id, value)

    var Message.userState: State?
        get() = getUserState(from!!.id)
        set(value) = setUserState(from!!.id, value)

    private fun getUserState(userId: Long): State? {
        return transaction {
            val user = Users.select { Users.telegramId eq userId }.firstOrNull()
            return@transaction user?.get(Users.state)
        }
    }

    private fun setUserState(tgId: Long, state: State?) {
        return transaction {
            addLogger(StdOutSqlLogger)
            val user = Users.select { Users.telegramId eq tgId }.firstOrNull() ?: Users.insert {
                it[telegramId] = tgId
            }.resultedValues?.first()
            user ?: return@transaction
            Users.update({ Users.telegramId eq user[Users.telegramId] }) {
                it[Users.state] = State.Authenticating
            }
        }
    }

    fun f(predicate: Message.() -> Boolean): Filter {
        return object : Filter {
            override fun Message.predicate(): Boolean {
                return predicate()
            }
        }
    }

    fun MessageHandlerEnvironment.answer(
        text: String,
        parseMode: ParseMode? = null,
        disableWebPagePreview: Boolean? = null,
        disableNotification: Boolean? = null,
        replyToMessageId: Long? = null,
        allowSendingWithoutReply: Boolean? = null,
        replyMarkup: ReplyMarkup? = null
    ) {
        bot.sendMessage(
            ChatId.fromId(message.chat.id),
            text,
            parseMode,
            disableWebPagePreview,
            disableNotification,
            replyToMessageId,
            allowSendingWithoutReply,
            replyMarkup
        )
    }

    fun CommandHandlerEnvironment.answer(
        text: String,
        parseMode: ParseMode? = null,
        disableWebPagePreview: Boolean? = null,
        disableNotification: Boolean? = null,
        replyToMessageId: Long? = null,
        allowSendingWithoutReply: Boolean? = null,
        replyMarkup: ReplyMarkup? = null
    ) {
        bot.sendMessage(
            ChatId.fromId(message.chat.id),
            text,
            parseMode,
            disableWebPagePreview,
            disableNotification,
            replyToMessageId,
            allowSendingWithoutReply,
            replyMarkup
        )
    }

    fun Message.isMine(bot: Bot): Boolean {
        return from!!.id == bot.getMe().get().id
    }
}