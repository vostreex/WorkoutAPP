package com.example.data.database


import com.example.data.model.ExerciseEntity
import com.example.data.model.WorkoutEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

class DatabaseInit(
    private val exerciseDao: ExerciseDAO,
    private val workoutDAO: WorkoutDAO
) {
    suspend fun initialize() {
        withContext(Dispatchers.IO) {

            val exercises = exerciseDao.getALL().first()
            val workouts = workoutDAO.getAllWorkouts().first()

            if (exercises.isEmpty()) {

                val builtInExercises = listOf(
                    ExerciseEntity(
                        name = "Жим лежа",
                        muscleGroup = "Грудь",
                        gifURL = "https://cs12.pikabu.ru/post_img/2019/08/27/10/1566924646183973804.gif",
                        aboutExercise = "Лягте на скамью, поставьте ноги на пол. Опускайте штангу к середине груди, затем выжимайте вверх. Следите за контролем движения и не выгибайте поясницу."
                    ),
                    ExerciseEntity(
                        name = "Приседания",
                        muscleGroup = "Ноги",
                        gifURL = "https://instructorpro.ru/wp-content/uploads/2023/06/Приседания-сумо-Выполнение.gif",
                        aboutExercise = "Поставьте ноги на ширине плеч. Опускайтесь вниз, отводя таз назад, спина прямая. Колени не выходят далеко за носки."
                    ),
                    ExerciseEntity(
                        name = "Становая тяга",
                        muscleGroup = "Спина",
                        gifURL = "https://goodlooker.ru/wp-content/uploads/2020/03/Stanovaya_tyaga-2.gif",
                        aboutExercise = "Держите спину ровной, поднимайте штангу за счёт ног и ягодиц. Не округляйте поясницу."
                    ),
                    ExerciseEntity(
                        name = "Подтягивания",
                        muscleGroup = "Спина",
                        gifURL = "https://vashsport.com/wp-content/uploads/podtyagivaniya-shirokim-hvatom.gif",
                        aboutExercise = "Тянитесь грудью к перекладине. Не раскачивайтесь, работайте за счёт мышц спины."
                    ),
                    ExerciseEntity(
                        name = "Жим гантелей сидя",
                        muscleGroup = "Плечи",
                        gifURL = "https://gymcoach.ru/wp-content/uploads/2023/04/ezgif-3-239b3d9a6e.gif",
                        aboutExercise = "Сидя выжимайте гантели вверх. Не прогибайте спину, держите корпус стабильным."
                    ),
                    ExerciseEntity(
                        name = "Бицепс со штангой",
                        muscleGroup = "Бицепс",
                        gifURL = "https://rt-sport.ru/image/catalog/rt-sport/blog/biceps/Biceps-5.gif",
                        aboutExercise = "Поднимайте штангу без раскачки корпуса. Локти прижаты к телу."
                    ),
                    ExerciseEntity(
                        name = "Разгибание рук на блоке",
                        muscleGroup = "Трицепс",
                        gifURL = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ-gDZUPKEr4jVvko0Pnfeb7uYNyPMjpQN2pA&s",
                        aboutExercise = "Разгибайте руки вниз, фиксируя локти. Работает трицепс."
                    ),
                    ExerciseEntity(
                        name = "Планка",
                        muscleGroup = "Пресс",
                        gifURL = "https://skillbox.ru/upload/setka_images/13033825032025_b629b7dc02fe6f5723da7d5f3dd63eb28fb5f3e4.jpg",
                        aboutExercise = "Держите тело прямым. Не проваливайте поясницу, напрягайте пресс."
                    ),
                    ExerciseEntity(
                        name = "Скручивания",
                        muscleGroup = "Пресс",
                        gifURL = "https://gymcoach.ru/wp-content/uploads/2023/04/ezgif-3-5a57af8381.gif",
                        aboutExercise = "Поднимайте корпус за счёт пресса. Не тяните шею руками."
                    ),
                    ExerciseEntity(
                        name = "Выпады",
                        muscleGroup = "Ноги",
                        gifURL = "https://inspire2.ru/images/exercises/gif/vypad-vpere-d-s-gantelyami.gif",
                        aboutExercise = "Шаг вперёд, колено почти касается пола. Держите баланс и прямую спину."
                    )
                )

                exerciseDao.insertExercises(builtInExercises)
            }

            if (workouts.isEmpty()) {

                val workoutsList = listOf(
                    WorkoutEntity(name = "Грудь + Трицепс", exercisesIdList = listOf(1, 6, 7, 1, 7)),
                    WorkoutEntity(name = "Ноги базовая",exercisesIdList = listOf(2, 10, 2, 10, 8)),
                    WorkoutEntity(name = "Спина + Бицепс",exercisesIdList = listOf(3, 4, 6, 3, 4)),
                    WorkoutEntity(name = "Плечи + Пресс",exercisesIdList = listOf(5, 8, 9, 5, 9)),
                    WorkoutEntity(name = "Фулбоди 1",exercisesIdList = listOf(1, 2, 3, 5, 8)),
                    WorkoutEntity(name = "Фулбоди 2",exercisesIdList = listOf(2, 4, 6, 7, 9)),
                    WorkoutEntity(name = "Лёгкая тренировка",exercisesIdList = listOf(8, 9, 10, 2, 5)),
                    WorkoutEntity(name = "Силовая",exercisesIdList = listOf(3, 1, 2, 3, 1)),
                    WorkoutEntity(name =  "Кардио + пресс", exercisesIdList = listOf(8, 9, 10, 8, 9))
                )

                workoutDAO.insertWorkouts(workoutsList)
            }
        }
    }
}