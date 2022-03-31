package com.hedgehog.xmlparser.test

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileInputStream

object TestSecond {
    val gson = Gson()

    fun test() {

        val token = object : TypeToken<List<SecondModel>>() {}.type

        val ruModels = gson.fromJson<List<SecondModel>>(File("./x_files/result_second_json.json").readText(), token)
        val enModels = gson.fromJson<List<SecondModel>>(File("./x_files/second.json").readText(), token)

        val resultModels = mutableListOf<SecondModelCombined>()
        for (i in 0 until ruModels.size) {
            val enModel = enModels[i]
            val ruModel = ruModels[i]
            resultModels.add(
                SecondModelCombined(
                    ruModel.exerciseId,
                    ruModel.id,
                    ruModel.queue,
                    ruModel.repetitionsCount,
                    ruModel.subWorkoutId,
                    ruModel.approachesCount,
                    ruModel.time,
                    ruModel.title,
                    enModel.title
                )
            )
        }
        val resultFile = File("./x_files/second_combined_json.json")
        resultFile.createNewFile()
        resultFile.writeText(gson.toJson(resultModels))
    }

    fun firstAction() {
        //        val models = getModels()
//        for(i in 0 until models.size){
//            models[i].id = i
//        }
//        val file = File("./x_files/second_test.json")
//        file.writeText(gson.toJson(models))


        val firstModels = getModels()
        val map = hashMapOf<Int, SecondModel>()
        firstModels.forEach {
            map[it.id] = it
        }
        println(map)
        val firstResult = convertModels(map)
        val file = File("./x_files/result_second_json.json")
        file.createNewFile()
        file.writeText(gson.toJson(firstResult))
    }

    fun getModels(): List<SecondModel> {
        val file = File("./x_files/second_test.json")
        val token = object : TypeToken<List<SecondModel>>() {}.type
        val items = gson.fromJson<List<SecondModel>>(file.readText(), token)
        return items
    }

    fun convertModels(map: HashMap<Int, SecondModel>): List<SecondModel> {
        val file = File("./x_files/second_result.xlsx")
        val wb = XSSFWorkbook(FileInputStream(file))
        val sheet = wb.getSheetAt(0)

        var index = 0
        var row = sheet.getRow(index++)

        var i = 0
        val items = mutableListOf<SecondModel>()
        while (row?.getCell(0)?.cellType != null) {
            println(row.getCell(0))

            val title = row.getCell(4).toString()
            val item = map[i++]!!
            item.title = title
            items.add(item)

            row = sheet.getRow(index++)
        }
        return items
    }

}
