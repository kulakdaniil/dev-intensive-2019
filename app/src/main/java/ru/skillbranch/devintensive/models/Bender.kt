package ru.skillbranch.devintensive.models


/**
 * Бандл - это такой класс, в котором хранятся пары ключ-значение
 * чтобы сохранять состояние интерфейса пользователя
 * Достаточно добавить id в layout.xml к элементу (textEdit),
 * чтобы введенное в него значение пережило ротацию экрана
 *
 * Когда приложение переходит с onPause на onStop у наших вьюх
 * вызывается метод onSaveInstanceState, для которого нужно id элемента
 * сохранение происходит в бандл - прозрачно для нас
 *
 */
class Bender(var status:Status = Status.NORMAL, var question: Question = Question.NAME) {

    fun askQuestion(): String = when(question) {
        Question.NAME -> Question.NAME.question
        Question.PROFESSION -> Question.PROFESSION.question
        Question.MATERIAL -> Question.MATERIAL.question
        Question.BDAY -> Question.BDAY.question
        Question.SERIAL -> Question.SERIAL.question
        Question.IDLE -> Question.IDLE.question
    }

    fun listenAnswer(answer: String) : Pair<String, Triple<Int, Int, Int>> {
        return when {
            question.answers.contains(answer) -> {
                question = question.nextQuestion()
                "Отлично - ты справился\n${question.question}" to status.color
            }
            status != Status.CRITICAL -> {
                status = status.nextStatus()
                "Это неправильный ответ\n${question.question}" to status.color
            }
            else -> {
                status = Status.NORMAL
                question = Question.NAME
                "Это неправильный ответ. Давай все по новой\n${question.question}" to status.color
            }
        }
    }

    enum class Status(val color: Triple<Int, Int, Int>) {
        NORMAL(Triple(255, 255, 255)),
        WARNING(Triple(255, 120, 0)),
        DANGER(Triple(255, 60, 60)),
        CRITICAL(Triple(255, 0, 0));

        fun nextStatus(): Status {
            // если текущий порядок меньше последнего индекса, то верни элемент со следующим порядком
            return if (this.ordinal < values().lastIndex) {
                values()[this.ordinal + 1]
            } else {
                values()[0]
            }
        }
    }

    enum class Question(val question: String, val answers: List<String>) {
        NAME("Как меня зовут?", listOf("бендер", "bender")) {
            override fun nextQuestion(): Question = PROFESSION
        },
        PROFESSION("Назови мою профессию?", listOf("сгибальщик", "bender")) {
            override fun nextQuestion(): Question = MATERIAL
        },
        MATERIAL("Из чего я сделан?", listOf("металл", "дерево", "metal", "iron", "wood")) {
            override fun nextQuestion(): Question = BDAY
        },
        BDAY("Когда меня создали?", listOf("2993")) {
            override fun nextQuestion(): Question = SERIAL
        },
        SERIAL("Мой серийный номер?", listOf("2716057")) {
            override fun nextQuestion(): Question = IDLE
        },
        IDLE("На этом все, вопросов больше нет", listOf()) {
            override fun nextQuestion(): Question = IDLE
        };

        // когда требуется реализация абстрактного метода в нутри каждого перечисления
        // если нужна сложная логика, зависящая от предыдущих ответов на вопросы
        abstract fun nextQuestion(): Question
    }

}