fun main(args: Array<String>){

    var target = "Stage 1\n" +
            "#####\n" +
            "#OoP#\n" +
            "#####\n" +
            "=====\n" +
            "Stage 2\n" +
            "  #######\n" +
            "###  O  ###\n" +
            "#    o    #\n" +
            "# Oo P oO #\n" +
            "###  o  ###\n" +
            " #   O  #\n" +
            " ########"

    var changeTarget = target.split("\n")

    var mapStorage = MapStorage()

    var readMap = ReadMap(changeTarget, mapStorage)
    readMap.convertMap()

    mapStorage.startGame(2)
}


class Map(var mapValueList : ArrayList<String>, var stageNum: Int){

    var mapIntArray = ArrayList<ArrayList<Int>>()

    fun converToIntArray(){
        for(i in 0..mapValueList.size-1){
            var line = mapValueList[i]

            var lineIntList = ArrayList<Int>()

            for(j in 0..line.length-1){
                when(line[j]){
                    '#' -> lineIntList.add(0)
                    'O' -> lineIntList.add(1)
                    'o' -> lineIntList.add(2)
                    'P' -> lineIntList.add(3)
                    '=' -> lineIntList.add(4)
                    ' ' -> lineIntList.add(5)
                }
            }

            mapIntArray.add(lineIntList)
        }


    }

    fun printStage(){
        println("Stage "+ stageNum)
        println()
    }

    fun printMap(){
        for(i in 0..mapIntArray.size-1){
            for(j in 0..mapIntArray[i].size-1){
                when(mapIntArray[i][j]){
                    0 -> print('#')
                    1 -> print('O')
                    2 -> print('o')
                    3 -> print('P')
                    5 -> print(' ')
                }
            }
            println()
        }
    }
}


class ReadMap(var mapLineList : List<String>, var mapStorage: MapStorage){

    fun convertMap(){
        var currentStage = 0
        var mapValueList = ArrayList<String>()

        for(i in 0..mapLineList.size-1){

            var line = mapLineList[i]

            if(line.contains("Stage")){
                currentStage++
            }
            else if(line.contains("=")){
                var map = Map(mapValueList, currentStage)
                mapStorage.addMap(map)
                mapValueList.clear()
            }
            else if(i == mapLineList.size-1){
                mapValueList.add(line)
                var map = Map(mapValueList, currentStage)
                mapStorage.addMap(map)

            }
            else{
                mapValueList.add(line)
            }

        }
    }

}

class MapStorage(){

    private var mapList = ArrayList<Map>()

    fun addMap(map: Map){
        mapList.add(map)
        map.converToIntArray()
    }

    fun startGame(stage: Int){
        GameStart.startGame(mapList[stage-1])
    }
}

class GameStart(){

    companion object{
        fun startGame(map: Map){
            map.printStage()
            map.printMap()

        }
    }
}