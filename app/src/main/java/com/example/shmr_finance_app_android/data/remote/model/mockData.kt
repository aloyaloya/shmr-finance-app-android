package com.example.shmr_finance_app_android.data.remote.model

import com.example.shmr_finance_app_android.domain.model.Balance
import com.example.shmr_finance_app_android.domain.model.Category
import com.example.shmr_finance_app_android.domain.model.Expense
import com.example.shmr_finance_app_android.domain.model.Income
import com.example.shmr_finance_app_android.domain.model.Option

val mockExpenses = listOf(
    Expense(
        id = "0",
        title = "Аренда квартиры",
        amount = 100000,
        currency = "₽",
        emoji = "🏡"
    ),
    Expense(
        id = "1",
        title = "Одежда",
        amount = 100000,
        currency = "₽",
        emoji = "👗"
    ),
    Expense(
        id = "2",
        title = "На собачку",
        subtitle = "Джек",
        amount = 100000,
        currency = "₽",
        emoji = "🐶"
    ),
    Expense(
        id = "3",
        title = "На собачку",
        subtitle = "Энни",
        amount = 100000,
        currency = "₽",
        emoji = "🐶"
    ),
    Expense(
        id = "4",
        title = "Ремонт квартиры",
        amount = 100000,
        currency = "₽"
    ),
    Expense(
        id = "5",
        title = "Продукты",
        amount = 100000,
        currency = "₽",
        emoji = "🍭"
    ),
    Expense(
        id = "6",
        title = "Спортзал",
        amount = 100000,
        currency = "₽",
        emoji = "🏋️‍♂️"
    ),
    Expense(
        id = "7",
        title = "Медицина",
        amount = 100000,
        currency = "₽",
        emoji = "💊"
    ),
)

val mockHistoryExpenses = listOf(
    Expense(
        id = "8",
        title = "Ремонт квартиры",
        subtitle = "Ремонт - фурнитура для дверей",
        amount = 100000,
        currency = "₽"
    ),
    Expense(
        id = "9",
        title = "На собачку",
        amount = 100000,
        currency = "₽",
        emoji = "🐶"
    ),
    Expense(
        id = "10",
        title = "На собачку",
        amount = 100000,
        currency = "₽",
        emoji = "🐶"
    ),
    Expense(
        id = "11",
        title = "На собачку",
        amount = 100000,
        currency = "₽",
        emoji = "🐶"
    ),
    Expense(
        id = "12",
        title = "На собачку",
        amount = 100000,
        currency = "₽",
        emoji = "🐶"
    ),
)

val mockIncomes = listOf(
    Income(
        id = "1",
        title = "Зарплата",
        amount = 500000,
        currency = "₽"
    ),
    Income(
        id = "2",
        title = "Подработка",
        amount = 100000,
        currency = "₽"
    )
)

val mockBalance = Balance(
    name = "Мой Счет",
    balance = "-670 000",
    currency = "₽"
)

val mockCategories = listOf(
    Category(
        id = "0",
        title = "Аренда квартиры",
        emoji = "🏡"
    ),
    Category(
        id = "1",
        title = "Одежда",
        emoji = "👗"
    ),
    Category(
        id = "2",
        title = "На собачку",
        emoji = "🐶"
    ),
    Category(
        id = "3",
        title = "На собачку",
        emoji = "🐶"
    ),
    Category(
        id = "4",
        title = "Ремонт квартиры"
    ),
    Category(
        id = "5",
        title = "Продукты",
        emoji = "🍭"
    ),
    Category(
        id = "6",
        title = "Спортзал",
        emoji = "🏋️‍♂️"
    ),
    Category(
        id = "7",
        title = "Медицина",
        emoji = "💊"
    )
)

val mockSettings = listOf(
    Option(title = "Основной цвет"),
    Option(title = "Звуки"),
    Option(title = "Хаптики"),
    Option(title = "Код пароль"),
    Option(title = "Синхронизация"),
    Option(title = "Язык"),
    Option(title = "О программе")
)