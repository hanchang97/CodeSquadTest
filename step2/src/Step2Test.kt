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

    //var mapIntArray = ArrayList<ArrayList<Int>>()
    lateinit var mapIntArray : Array<Array<Int>>

    var mapHeight = 0
    var mapWidth = 0

    fun findMapSize(){
        mapHeight = mapValueList.size

        for(i in 0..mapValueList.size-1){
            var line = mapValueList[i]

            if(line.length > mapWidth)
                mapWidth = line.length
        }
        println("가로길이 : ${mapWidth}, 세로길이 : ${mapHeight}")

        mapIntArray = Array(mapHeight, {Array(mapWidth, {-1})})
    }

    fun converToIntArray(){
        for(i in 0..mapValueList.size-1){
            var line = mapValueList[i]

            for(j in 0..line.length-1){
                when(line[j]){
                    '#' -> mapIntArray[i][j] = 0
                    'O' -> mapIntArray[i][j] = 1
                    'o' -> mapIntArray[i][j] = 2
                    'P' -> mapIntArray[i][j] = 3
                    '=' -> mapIntArray[i][j] = 4
                    ' ' -> mapIntArray[i][j] = 5
                }
            }
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
        println()
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
        map.findMapSize()
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

            SetGame.setGame(map)
        }
    }
}

// 게임에 사용을 위해 맵을 복사하고 명령어 입력을 받는다
class SetGame(){
    companion object{
        fun setGame(map : Map){
            var temp = Array(map.mapHeight, {Array(map.mapWidth, {-1})})  // 게임 플레이시 사용을 위해 맵 복사
            for(i in 0..map.mapHeight-1){
                for(j in 0..map.mapWidth-1){
                    temp[i][j] = map.mapIntArray[i][j]
                }
            }

            while(true) {
                print("SOKOBAN> ")
                var command = readLine()

                if (command?.length == 0) {  // 바로 종료
                    println("no command : Bye~")
                    System.exit(0)
                } else {
                    command = command!!.lowercase()  // 소문자 변환 / 문제의 입력에 대문자가 주어지는 경우 존재
                    println(command)
                    println()
                    PlayGame.playGame(temp, command!!)
                }
            }

        }
    }
}

class PlayGame(){
    companion object{
        fun playGame(map : Array<Array<Int>>, command : String){
            var playerLocation = findPlayerLocation(map)

            println("플레이어 위치 : ${playerLocation.playerY}, ${playerLocation.playerX}")

            for(i in 0..command.length-1){
                if(command[i] == 'q'){  //종료
                    println("Bye~")
                    System.exit(0)
                }
                else{

                }
            }
        }

        fun findPlayerLocation(map: Array<Array<Int>>) : PlayerLocation{
            for(i in 0..map.size-1){
                for(j in 0..map[i].size-1){
                    if(map[i][j] == 3){
                        return PlayerLocation(i, j)
                    }
                }
            }

            return PlayerLocation(0, 0)
        }

    }


}

// 인덱스 기준 값
data class PlayerLocation(
    var playerY : Int,
    var playerX : Int
)

