package db_models

enum class State {
    AuthSelectServer,
    AuthEnterUsername,
    AuthEnterPassword,
    Authorized
}