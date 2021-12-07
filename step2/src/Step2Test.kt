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
}


class Map(var mapValueList : ArrayList<String>, var stageNum: Int){


}


class ReadMap(var mapLineList : List<String>, var mapStorage: MapStorage){

    fun convertMap(){

        var currentStage = 1
        var mapValueList = ArrayList<String>()

        for(i in 0..mapLineList.size-1){

            var line = mapLineList[i]

            if(line.contains("Stage")){
                println(currentStage++)
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
    }
}