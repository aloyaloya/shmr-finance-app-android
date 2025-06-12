package com.example.shmr_finance_app_android.data.model

import com.example.shmr_finance_app_android.data.model.domain.Balance
import com.example.shmr_finance_app_android.data.model.domain.Category
import com.example.shmr_finance_app_android.data.model.domain.Expense
import com.example.shmr_finance_app_android.data.model.domain.Income

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
        author = "Джек",
        amount = 100000,
        currency = "₽",
        emoji = "🐶"
    ),
    Expense(
        id = "3",
        title = "На собачку",
        author = "Энни",
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