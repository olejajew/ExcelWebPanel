package com.hedgehog.xmlparser.test

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileInputStream

object TestFirst {
    val gson = Gson()

    fun test() {
        val typeToken = object : TypeToken<List<FirstModel>>() {}.type

        val enModels = gson.fromJson<List<FirstModel>>(File("./x_files/first.json").readText(), typeToken)
        val ruModels = gson.fromJson<List<FirstModel>>(File("./x_files/result_first_json.json").readText(), typeToken)

        val resultModules = mutableListOf<FirstModelCombined>()
        enModels.forEach {
            val ruModel = ruModels.first { ruModel -> ruModel.id == it.id }
            resultModules.add(
                FirstModelCombined(
                    it.id,
                    it.title,
                    it.description,
                    ruModel.title,
                    ruModel.description,
                    it.kcalBurnedOnOneKgPerMinute,
                    it.maleAnimPath,
                    it.femaleAnimPath
                )
            )
        }
        val combinedFile = File("./x_files/first_combined_json.json")
        combinedFile.createNewFile()
        combinedFile.writeText(gson.toJson(resultModules))
    }

    fun getFromBat() {
        val firstModels = getModels()
        val map = hashMapOf<Int, FirstModel>()
        firstModels.forEach {
            map[it.id] = it
        }
        val firstResult = convertModels(map)
        val file = File("./result_first_json.json")
        file.createNewFile()
        file.writeText(gson.toJson(firstResult))
    }

    fun getModels(): List<FirstModel> {

        val file = File("./first.json")
        val token = object : TypeToken<List<FirstModel>>() {}.type
        val items = gson.fromJson<List<FirstModel>>(file.readText(), token)
//        ToExcel.saveToExcel(items)
        return items
    }

    fun convertModels(map: HashMap<Int, FirstModel>): List<FirstModel> {
        val file = File("./first_result.xlsx")
        val wb = XSSFWorkbook(FileInputStream(file))
        val sheet = wb.getSheetAt(0)

        var index = 0
        var row = sheet.getRow(index++)

        println(map.keys)

        val items = mutableListOf<FirstModel>()
        while (row?.getCell(0)?.cellType != null) {
            println(row.getCell(0))

            val id = row.getCell(0).toString().toInt()
            val title = row.getCell(4).toString()
            val description = row.getCell(5).toString()
            val item = map[id]!!
            item.description = description
            item.title = title
            items.add(item)

            row = sheet.getRow(index++)
        }
        return items
    }

}
