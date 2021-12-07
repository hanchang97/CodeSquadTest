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

            // 구멍이 무언가에 덮여있는 상태인지 검사하기 위함
            var holeCoveredCheckArray = Array(map.mapHeight, {Array(map.mapWidth, {false})})

            // 초기 플레이어 위치 찾기
            var playerLocation = findPlayerLocation(temp)

            while(true) {
                print("SOKOBAN> ")
                var command = readLine()

                if (command?.length == 0) {  // 바로 종료
                    println("no command : Bye~")
                    System.exit(0)
                } else {
                    println(command)
                    println()
                    PlayGame.playGame(playerLocation, temp, holeCoveredCheckArray, command!!)
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

class PlayGame(){
    companion object{
        fun playGame(playerLocation : PlayerLocation, map : Array<Array<Int>>,
                     holeCoveredCheckArray : Array<Array<Boolean>>,  command : String){

            println("플레이어 위치 : ${playerLocation.playerY}, ${playerLocation.playerX}")

            for(i in 0..command.length-1){
                if(command[i] == 'q'){  //종료
                    println("Bye~")
                    System.exit(0)
                }
                else if(command[i] == 'w' || command[i] == 'a' || command[i] == 's' || command[i] == 'd'
                    || command[i] == 'W' || command[i] == 'A' || command[i] == 'S' || command[i] == 'D'){

                    movePlayer(playerLocation, map, holeCoveredCheckArray, command[i])
                }
                else{  // 잘못된 명령어 = w,a,s,d,q에 어느것에도 해당하지 않음
                    printCurrentMap(map)
                    println("${command[i]}: (경고!) 해당 명령을 수행할 수 없습니다!")
                }
            }
        }


        fun movePlayer(playerLocation: PlayerLocation, map : Array<Array<Int>>,
                       holeCoveredCheckArray : Array<Array<Boolean>>, commandChar : Char){

            var playerY = playerLocation.playerY
            var playerX = playerLocation.playerX

            if(commandChar == 'w' || commandChar == 'W'){  //위쪽
                if(map[playerY-1][playerX] == 5 || map[playerY-1][playerX] == 1){
                    // 플레이어 현재 위치 값 변화
                    if(holeCoveredCheckArray[playerY][playerX]){ // 현재 플레이어 위치에 구멍이 있었다면
                        map[playerY][playerX] = 1  // 플레이어가 이동했으므로 구멍이 다시 보여야 하므로 1 할당
                        holeCoveredCheckArray[playerY][playerX] = false // 구멍이 다시 보이므로 false로 변경
                    }
                    else{ // 구멍 없었다면 현재 위치는 공백이 된다
                        map[playerY][playerX] = 5
                    }

                    //플레이어 다음 위치 값 변화
                    if(map[playerY-1][playerX] == 1){  // 다음 위치가 구멍을 덮게 되는 위치
                        map[playerY-1][playerX] = 3
                        holeCoveredCheckArray[playerY-1][playerX] = true  // 구멍이 덮였으므로 true 된다
                    }
                    else{ // 공백으로 이동
                        map[playerY-1][playerX] = 3
                    }

                    printCurrentMap(map)
                    println("${commandChar}: 위쪽으로 이동합니다.")


                    playerLocation.playerY -= 1  // 플레이어 위치 업데이트

                }
                else{
                    printCurrentMap(map)
                    println("${commandChar}: (경고!) 해당 명령을 수행할 수 없습니다!")
                }
            }
            else if(commandChar == 'a' || commandChar == 'A'){  //왼쪽
                if(map[playerY][playerX-1] == 5 || map[playerY][playerX-1] == 1){
                    // 플레이어 현재 위치 값 변화
                    if(holeCoveredCheckArray[playerY][playerX]){ // 현재 플레이어 위치에 구멍이 있었다면
                        map[playerY][playerX] = 1  // 플레이어가 이동했으므로 구멍이 다시 보여야 하므로 1 할당
                        holeCoveredCheckArray[playerY][playerX] = false // 구멍이 다시 보이므로 false로 변경
                    }
                    else{ // 현재 위치에 구멍 없었다면 현재 위치는 공백이 된다
                        map[playerY][playerX] = 5
                    }

                    //플레이어 다음 위치 값 변화
                    if(map[playerY][playerX-1] == 1){  // 다음 위치가 구멍을 덮게 되는 위치
                        map[playerY][playerX-1] = 3
                        holeCoveredCheckArray[playerY][playerX-1] = true  // 구멍이 덮였으므로 true 된다
                    }
                    else{ // 공백으로 이동
                        map[playerY][playerX-1] = 3
                    }

                    printCurrentMap(map)
                    println("${commandChar}: 왼쪽으로 이동합니다.")

                    playerLocation.playerX -= 1  // 플레이어 위치 업데이트

                }
                else{
                    printCurrentMap(map)
                    println("${commandChar}: (경고!) 해당 명령을 수행할 수 없습니다!")
                }

            }
            else if(commandChar == 's' || commandChar ==  'S'){  //아래쪽
                if(map[playerY+1][playerX] == 5 || map[playerY+1][playerX] == 1){
                    // 플레이어 현재 위치 값 변화
                    if(holeCoveredCheckArray[playerY][playerX]){ // 현재 플레이어 위치에 구멍이 있었다면
                        map[playerY][playerX] = 1  // 플레이어가 이동했으므로 구멍이 다시 보여야 하므로 1 할당
                        holeCoveredCheckArray[playerY][playerX] = false // 구멍이 다시 보이므로 false로 변경
                    }
                    else{ // 현재 위치에 구멍 없었다면 현재 위치는 공백이 된다
                        map[playerY][playerX] = 5
                    }

                    //플레이어 다음 위치 값 변화
                    if(map[playerY+1][playerX] == 1){  // 다음 위치가 구멍을 덮게 되는 위치
                        map[playerY+1][playerX] = 3
                        holeCoveredCheckArray[playerY+1][playerX] = true  // 구멍이 덮였으므로 true 된다
                    }
                    else{ // 공백으로 이동
                        map[playerY+1][playerX] = 3
                    }

                    printCurrentMap(map)
                    println("${commandChar}: 아래쪽으로 이동합니다.")

                    playerLocation.playerY += 1  // 플레이어 위치 업데이트
                }
                else{
                    printCurrentMap(map)
                    println("${commandChar}: (경고!) 해당 명령을 수행할 수 없습니다!")
                }
            }
            else{ // d or D 인 경우  //오른쪽
                if(map[playerY][playerX+1] == 5 || map[playerY][playerX+1] == 1){
                    // 플레이어 현재 위치 값 변화
                    if(holeCoveredCheckArray[playerY][playerX]){ // 현재 플레이어 위치에 구멍이 있었다면
                        map[playerY][playerX] = 1  // 플레이어가 이동했으므로 구멍이 다시 보여야 하므로 1 할당
                        holeCoveredCheckArray[playerY][playerX] = false // 구멍이 다시 보이므로 false로 변경
                    }
                    else{ // 현재 위치에 구멍 없었다면 현재 위치는 공백이 된다
                        map[playerY][playerX] = 5
                    }

                    //플레이어 다음 위치 값 변화
                    if(map[playerY][playerX+1] == 1){  // 다음 위치가 구멍을 덮게 되는 위치
                        map[playerY][playerX+1] = 3
                        holeCoveredCheckArray[playerY][playerX+1] = true  // 구멍이 덮였으므로 true 된다
                    }
                    else{ // 공백으로 이동
                        map[playerY][playerX+1] = 3
                    }

                    printCurrentMap(map)
                    println("${commandChar}: 오른쪽으로 이동합니다.")

                    playerLocation.playerX += 1  // 플레이어 위치 업데이트
                }
                else{
                    printCurrentMap(map)
                    println("${commandChar}: (경고!) 해당 명령을 수행할 수 없습니다!")
                }
            }
        }

        fun printCurrentMap(map: Array<Array<Int>>){
            println()
            for(i in 0..map.size-1){
                for(j in 0..map[i].size-1){
                    when(map[i][j]){
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

            for(i in 0..map.size-1){
                for(j in 0..map[i].size-1){
                    print(map[i][j])
                    }
                println()
                }
                println()
            }
        }

    }





// 인덱스 기준 값
data class PlayerLocation(
    var playerY : Int,
    var playerX : Int
)

