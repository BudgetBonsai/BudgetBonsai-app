package com.example.budgetbonsai.utils

object Category {

    fun expenseCategory(): ArrayList<String> {
        val listExpense = ArrayList<String>()
        listExpense.add("Food")
        listExpense.add("Transportation")
        listExpense.add("Entertainment")
        listExpense.add("Other Expense")

        return listExpense
    }

    fun incomeCategory(): ArrayList<String> {
        val listIncome = ArrayList<String>()
        listIncome.add("Salary")
        listIncome.add("Investment Return")
        listIncome.add("Other Income")

        return listIncome
    }
}