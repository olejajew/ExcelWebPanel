package com.hedgehog.xmlparser.test

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileInputStream

object TestThird {
    val gson = Gson()

    fun test() {
        firstAction()
    }

    fun firstAction() {
        val firstModels = getModels()
        val map = hashMapOf<Int, ThirdModel>()
        firstModels.forEach {
            map[it.id] = it
        }
        val firstResult = convertModels(map)
        val file = File("./x_files/result_third_json.json")
        file.createNewFile()
        file.writeText(gson.toJson(firstResult))
    }

    fun getModels(): List<ThirdModel> {
        val file = File("./x_files/third.json")
        val token = object : TypeToken<List<ThirdModel>>() {}.type
        val items = gson.fromJson<List<ThirdModel>>(file.readText(), token)
        println(items)
        return items
    }

    fun convertModels(map: HashMap<Int, ThirdModel>): List<ThirdModel> {
        val file = File("./x_files/third_result.xlsx")
        val wb = XSSFWorkbook(FileInputStream(file))
        val sheet = wb.getSheetAt(0)

        var index = 0
        var row = sheet.getRow(index++)

        val items = mutableListOf<ThirdModel>()

        val nameMap = hashMapOf<String, String>()

        while (row?.getCell(0)?.cellType != null) {
            nameMap[row.getCell(0).toString()] = row.getCell(1).toString()
            row = sheet.getRow(index++)
        }

        map.values.forEach {
            it.titleEn = it.title
            it.titleRu = nameMap[it.title]
            it.title = null
            items.add(it)
        }

        return items
    }

}
