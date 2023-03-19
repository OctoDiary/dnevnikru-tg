enum class Server(
    val serverName: String,
    val url: String,
    val clientId: String,
    val clientSecret: String
) {
    DNEVNIK_RU(
        "Дневник.ру",
        "https://api.dnevnik.ru/mobile/v6.0/",
        "1d7bd105-4cd1-4f6c-9ecc-394e400b53bd",
        "5dcb5237-b5d3-406b-8fee-4441c3a66c99"
    ),
    SCHOOL_MOSREG(
        "Школьный Портал",
        "https://api.school.mosreg.ru/mobile/v6.0/",
        "594df05c-fea3-4e66-9949-60ae72a2150d",
        "2436e132-8103-417b-ab63-5e89dcf6fba9"
    ),
    KUNDELIK(
        "Кунделiк.kz",
        "https://api.kundelik.kz/mobile/v6.0/",
        "387D44E3-E0C9-4265-A9E4-A4CAAAD5111C",
        "8A7D709C-FDBB-4047-B0EA-8947AFE89D67"
    )
}