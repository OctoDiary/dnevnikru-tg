import com.github.kotlintelegrambot.dispatcher.handlers.CallbackQueryHandlerEnvironment
import com.github.kotlintelegrambot.dispatcher.handlers.CommandHandlerEnvironment
import com.github.kotlintelegrambot.dispatcher.handlers.InlineQueryHandlerEnvironment
import com.github.kotlintelegrambot.dispatcher.handlers.MessageHandlerEnvironment
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.entities.Message
import com.github.kotlintelegrambot.entities.ParseMode
import com.github.kotlintelegrambot.entities.ReplyMarkup
import com.github.kotlintelegrambot.extensions.filters.Filter
import db_models.State
import db_models.Users
import db_models.Users.nullable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object Utils {

    enum class ChatType {
        private,
        supergroup,
        group,
        bot,
        channel
    }

    var MessageHandlerEnvironment.userState: State?
        get() = getUserState(message.from!!.id)
        set(value) = setUserState(message.from!!.id, value)

    var CommandHandlerEnvironment.userState: State?
        get() = getUserState(message.from!!.id)
        set(value) = setUserState(message.from!!.id, value)

    var InlineQueryHandlerEnvironment.userState: State?
        get() = getUserState(inlineQuery.from.id)
        set(value) = setUserState(inlineQuery.from.id, value)

    var CallbackQueryHandlerEnvironment.userState: State?
        get() = getUserState(callbackQuery.from.id)
        set(value) = setUserState(callbackQuery.from.id, value)

    var Message.userState: State?
        get() = getUserState(from!!.id)
        set(value) = setUserState(from!!.id, value)

    private fun getUserState(userId: Long): State? {
        return transaction {
            val user = Users.select { Users.telegramId eq userId }.firstOrNull()
            return@transaction user?.get(Users.state)
        }
    }

    fun setUserState(tgId: Long, state: State?) {
        return transaction {
            addLogger(StdOutSqlLogger)
            val user = Users.select { Users.telegramId eq tgId }.firstOrNull() ?: Users.insert {
                it[telegramId] = tgId
            }.resultedValues?.first()
            user ?: return@transaction
            Users.update({ Users.telegramId eq user[Users.telegramId] }) {
                it[Users.state] = state
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

    fun CallbackQueryHandlerEnvironment.answer(
        text: String,
        parseMode: ParseMode? = null,
        disableWebPagePreview: Boolean? = null,
        disableNotification: Boolean? = null,
        replyToMessageId: Long? = null,
        allowSendingWithoutReply: Boolean? = null,
        replyMarkup: ReplyMarkup? = null
    ) {
        bot.sendMessage(
            ChatId.fromId(callbackQuery.from.id),
            text,
            parseMode,
            disableWebPagePreview,
            disableNotification,
            replyToMessageId,
            allowSendingWithoutReply,
            replyMarkup
        )
    }

    fun <T> MessageHandlerEnvironment.updateField(field: Column<T>, value: T) {
        return transaction {
            addLogger(StdOutSqlLogger)
            val user = Users.select { Users.telegramId eq message.from!!.id }.firstOrNull()
            user ?: return@transaction
            Users.update({ Users.telegramId eq user[Users.telegramId] }) {
                it[field] = value
            }
        }
    }

    fun <T> updateUserField(telegramUserId: Long, field: Column<T>, value: T) {
        return transaction {
            addLogger(StdOutSqlLogger)
            val user = Users.select { Users.telegramId eq telegramUserId }.firstOrNull()
            user ?: return@transaction
            Users.update({ Users.telegramId eq user[Users.telegramId] }) {
                it[field] = value
            }
        }
    }

    val Message.chatTypeEnum: ChatType
        get() = ChatType.valueOf(chat.type)

    fun <T> MessageHandlerEnvironment.getField(field: Column<T>): T {
        return transaction {
            addLogger(StdOutSqlLogger)
            val user = Users.select { Users.telegramId eq message.from!!.id }.firstOrNull()
            return@transaction user!![field]
        }
    }

    fun <T> CallbackQueryHandlerEnvironment.getField(field: Column<T>): T {
        return transaction {
            addLogger(StdOutSqlLogger)
            val user = Users.select { Users.telegramId eq callbackQuery.from.id }.firstOrNull()
            return@transaction user!![field]
        }
    }
}