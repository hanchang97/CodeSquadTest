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

    mapStorage.startGame(1)  //스테이지 입력
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

    }
}


class ReadMap(var mapLineList : List<String>, var mapStorage: MapStorage){

    fun convertMap(){
        var currentStage = 0
        var mapValueList = ArrayList<String>()
        for(i in 0..mapLineList.size-1){
            var line = mapLineList[i]

            if(line.contains("Stage"))
                    currentStage++
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
            else
                mapValueList.add(line)
        }
    }

}

class MapStorage(){

    var mapList = ArrayList<Map>()

    fun addMap(map: Map){
        mapList.add(map)
        map.findMapSize()
        map.converToIntArray()
    }

    fun startGame(stage: Int){
        if(stage > mapList.size){
            println("전체 게임을 클리어하셨습니다!")
            println("축하드립니다!")
            System.exit(0)
        }
        else{
            GameStart.startGame(mapList[stage-1], this)
        }
    }
}

class GameStart(){

    companion object{
        fun startGame(map: Map, mapStorage: MapStorage){
            map.printStage()
            map.printMap()

            SetGame.setGame(map, mapStorage)
        }
    }
}

// 게임에 사용을 위해 맵을 복사하고 명령어 입력을 받는다
class SetGame(){
    companion object{
        fun setGame(map : Map, mapStorage: MapStorage){
            var temp = Array(map.mapHeight, {Array(map.mapWidth, {-1})})  // 게임 플레이시 사용을 위해 맵 복사
            for(i in 0..map.mapHeight-1){
                for(j in 0..map.mapWidth-1){
                    temp[i][j] = map.mapIntArray[i][j]
                }
            }

            var holeCoveredCheckArray = Array(map.mapHeight, {Array(map.mapWidth, {1})})
            var playerLocation = findPlayerLocation(temp)
            var currentStageNum = map.stageNum  // 현재 스테이지 번호

            while(true) {
                print("\nSOKOBAN> ")
                var command = readLine()
                if (command?.length == 0) {  // 바로 종료
                    println("no command : Bye~")
                    System.exit(0)
                } else {
                    println()
                    PlayGame.playGame(mapStorage, currentStageNum, playerLocation, temp, holeCoveredCheckArray, command!!)
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
        fun playGame(mapStorage: MapStorage, currentStageNum : Int, playerLocation : PlayerLocation,
                     map : Array<Array<Int>>, holeCoveredCheckArray : Array<Array<Int>>,  command : String){
            for(i in 0..command.length-1){
                if(command[i] == 'q'){  //종료
                    println("\nBye~")
                    System.exit(0)
                }
                else if(command[i] == 'r'){ // 현재 스테이지 다시 시작
                    println("현재 스테이지 다시 시작")
                    mapStorage.startGame(currentStageNum)
                }
                else if(command[i] == 'w' || command[i] == 'a' || command[i] == 's' || command[i] == 'd' || command[i] == 'W' || command[i] == 'A' || command[i] == 'S' || command[i] == 'D'){
                    movePlayer(mapStorage, currentStageNum, playerLocation, map, holeCoveredCheckArray, command[i])
                }
                else{  // 잘못된 명령어 = w,a,s,d,q에 어느것에도 해당하지 않음
                    printCurrentMap(map, holeCoveredCheckArray)
                    println("${command[i]}: (경고!) 해당 명령을 수행할 수 없습니다!")
                }
            }
        }


        fun movePlayer(mapStorage: MapStorage, currentStageNum: Int, playerLocation: PlayerLocation,
                       map : Array<Array<Int>>, holeCoveredCheckArray : Array<Array<Int>>, commandChar : Char){

            if(commandChar == 'w' || commandChar == 'W'){  //위쪽
                moveByCommand(mapStorage, currentStageNum, 0, playerLocation, map,
                    holeCoveredCheckArray, commandChar, "위쪽")
            }
            else if(commandChar == 'a' || commandChar == 'A'){  //왼쪽
                moveByCommand(mapStorage, currentStageNum,2, playerLocation, map,
                    holeCoveredCheckArray, commandChar, "왼쪽")
            }
            else if(commandChar == 's' || commandChar ==  'S'){  //아래쪽
                moveByCommand(mapStorage, currentStageNum,1, playerLocation, map,
                    holeCoveredCheckArray, commandChar, "아래쪽")
            }
            else{ // d or D 인 경우  //오른쪽
                moveByCommand(mapStorage, currentStageNum,3, playerLocation, map,
                    holeCoveredCheckArray, commandChar, "오른쪽")
            }
        }


        fun moveByCommand(mapStorage: MapStorage, currentStageNum: Int, dir : Int, playerLocation: PlayerLocation,
                          map: Array<Array<Int>>, holeCoveredCheckArray: Array<Array<Int>>, commandChar: Char, commandString : String){
            var dirY = arrayOf(-1, 1, 0, 0)
            var dirX = arrayOf(0, 0, -1, 1)
            var nextY = playerLocation.playerY + dirY[dir]
            var nextX = playerLocation.playerX + dirX[dir]
            var ballsNextY = nextY + dirY[dir]
            var ballsNextX = nextX + dirX[dir]  // 공이 이동하는 경우 공의 다음 위치

            if(map[nextY][nextX] == 5)
                playerMoveToEmptySpace(map, holeCoveredCheckArray, commandChar, commandString, playerLocation.playerY, playerLocation.playerX,
                nextY, nextX, ballsNextY, ballsNextX, playerLocation, mapStorage, currentStageNum)
            else if(map[nextY][nextX] == 1)
                playerMoveToHole(map, holeCoveredCheckArray, commandChar, commandString, playerLocation.playerY, playerLocation.playerX,
                    nextY, nextX, ballsNextY, ballsNextX, playerLocation, mapStorage, currentStageNum)
            else if(map[nextY][nextX] == 2) // 다음 위치에 공이 있는 경우
                playerPushBall(map, holeCoveredCheckArray, commandChar, commandString, playerLocation.playerY, playerLocation.playerX,
                    nextY, nextX, ballsNextY, ballsNextX, playerLocation, mapStorage, currentStageNum)
            else{
                printCurrentMap(map, holeCoveredCheckArray)
                println("${commandChar}: (경고!) 해당 명령을 수행할 수 없습니다!")
            }
        }


        fun playerMoveToEmptySpace(map: Array<Array<Int>>, holeCoveredCheckArray: Array<Array<Int>>, commandChar: Char, commandString : String,
        currentY : Int, currentX : Int, nextY : Int, nextX : Int, ballSnextY : Int, ballSnextX : Int, playerLocation: PlayerLocation,
        mapStorage: MapStorage, currentStageNum: Int){

            if(holeCoveredCheckArray[currentY][currentX] == 3){ // 현재 플레이어 위치에 구멍이 있었다면
                map[currentY][currentX] = 1  // 플레이어가 이동했으므로 구멍이 다시 보여야 하므로 1 할당
                holeCoveredCheckArray[currentY][currentX] = 1 // 구멍이 다시 보이므로 1을 할당
            }
            else  // 현재 위치에 구멍 없었다면 현재 위치는 공백이 된다
                map[currentY][currentX] = 5

            map[nextY][nextX] = 3  // 다음 위치에는 플레이어가 가게 되므로 3으로 갱신

            afterMove(map, holeCoveredCheckArray, commandChar, commandString, playerLocation, nextY, nextX,
                mapStorage, currentStageNum)
        }


        fun playerMoveToHole(map: Array<Array<Int>>, holeCoveredCheckArray: Array<Array<Int>>, commandChar: Char, commandString : String,
                             currentY : Int, currentX : Int, nextY : Int, nextX : Int, ballSnextY : Int, ballSnextX : Int, playerLocation: PlayerLocation,
                             mapStorage: MapStorage, currentStageNum: Int){

            if(holeCoveredCheckArray[currentY][currentX] == 3){ // 현재 플레이어 위치에 구멍이 있었다면
                map[currentY][currentX] = 1  // 플레이어가 이동했으므로 구멍이 다시 보여야 하므로 1 할당
                holeCoveredCheckArray[currentY][currentX] = 1 // 구멍이 다시 보이므로 false로 변경
            }
            else{ // 현재 위치에 구멍 없었다면 현재 위치는 공백이 된다
                map[currentY][currentX] = 5
            }

            // 다음 위치가 구멍을 덮게 되는 위치
            map[nextY][nextX] = 3
            holeCoveredCheckArray[nextY][nextX] = 3  // 구멍이 플레이어로 덮였으므로 3으로 갱신

            afterMove(map, holeCoveredCheckArray, commandChar, commandString, playerLocation, nextY, nextX,
                mapStorage, currentStageNum)
        }


        fun playerPushBall(map: Array<Array<Int>>, holeCoveredCheckArray: Array<Array<Int>>, commandChar: Char, commandString : String,
                           currentY : Int, currentX : Int, nextY : Int, nextX : Int, ballsNextY : Int, ballsNextX : Int, playerLocation: PlayerLocation,
                           mapStorage: MapStorage, currentStageNum: Int){

            if(map[ballsNextY][ballsNextX] == 5){ // 공 다음이 빈 공간인 경우
                BallMoveToEmptySpace(map, holeCoveredCheckArray, commandChar, commandString, currentY, currentX,
                    nextY, nextX, ballsNextY, ballsNextX, playerLocation, mapStorage, currentStageNum)
            }
            else if(map[ballsNextY][ballsNextX] == 1){ // 공 다음이 덮히지 않은 구멍인 경우
                BallMoveToHole(map, holeCoveredCheckArray, commandChar, commandString, currentY, currentX,
                    nextY, nextX, ballsNextY, ballsNextX, playerLocation, mapStorage, currentStageNum)
            }
            else{
                printCurrentMap(map, holeCoveredCheckArray)
                println("${commandChar}: (경고!) 해당 명령을 수행할 수 없습니다!")
            }
        }


        fun BallMoveToEmptySpace(map: Array<Array<Int>>, holeCoveredCheckArray: Array<Array<Int>>, commandChar: Char, commandString : String,
                                 currentY : Int, currentX : Int, nextY : Int, nextX : Int, ballsNextY : Int, ballsNextX : Int, playerLocation: PlayerLocation,
                                 mapStorage: MapStorage, currentStageNum: Int){

            map[ballsNextY][ballsNextX] = 2 // 공이 먼저 빈공간으로 이동
            map[nextY][nextX] = 3 // 플레이어는 기존 공의 위치로 이동
            // 기존 공이 있던 위치가 구멍 위라면 이제 플레이어가 덮게 되므로 값을 갱신해준다
            if(holeCoveredCheckArray[nextY][nextX] == 2)
                holeCoveredCheckArray[nextY][nextX] = 3

            // 기존 플레이어가 있던 곳이 구멍 위였다면 이제 구멍이 보이게 된다
            if(holeCoveredCheckArray[currentY][currentX] == 3){
                holeCoveredCheckArray[currentY][currentX] = 1
                map[currentY][currentX] = 1
            }
            else{ // 그냥 빈 공간이었다면
                map[currentY][currentX] = 5
            }

            afterMove(map, holeCoveredCheckArray, commandChar, commandString, playerLocation, nextY, nextX,
                mapStorage, currentStageNum)
        }


        fun BallMoveToHole(map: Array<Array<Int>>, holeCoveredCheckArray: Array<Array<Int>>, commandChar: Char, commandString : String,
                           currentY : Int, currentX : Int, nextY : Int, nextX : Int, ballsNextY : Int, ballsNextX : Int, playerLocation: PlayerLocation,
                           mapStorage: MapStorage, currentStageNum: Int){
            // 공이 이동 후 구멍이 공에 덮힌다
            map[ballsNextY][ballsNextX] = 2
            holeCoveredCheckArray[ballsNextY][ballsNextX] = 2
            // 플레이어는 기존 공의 위치로 이동
            map[nextY][nextX] = 3

            // 기존 공이 있던 위치가 구멍 위라면 이제 플레이어가 덮게 되므로 값을 갱신해준다
            if(holeCoveredCheckArray[nextY][nextX] == 2)
                holeCoveredCheckArray[nextY][nextX] = 3
            // 기존 플레이어가 있던 곳이 구멍 위였다면 이제 구멍이 보이게 된다
            if(holeCoveredCheckArray[currentY][currentX] == 3){
                holeCoveredCheckArray[currentY][currentX] = 1
                map[currentY][currentX] = 1
            }
            else{ // 그냥 빈 공간이었다면
                map[currentY][currentX] = 5
            }

          afterMove(map, holeCoveredCheckArray, commandChar, commandString, playerLocation, nextY, nextX,
          mapStorage, currentStageNum)
        }

        fun afterMove(map : Array<Array<Int>>, holeCoveredCheckArray: Array<Array<Int>>, commandChar: Char, commandString: String,
        playerLocation: PlayerLocation, nextY: Int, nextX: Int, mapStorage: MapStorage, currentStageNum: Int){

            printCurrentMap(map, holeCoveredCheckArray)
            println("${commandChar}: ${commandString}으로 이동합니다.")

            playerLocation.playerY = nextY
            playerLocation.playerX = nextX  // 플레이어 위치 업데이트

            checkStagePass(mapStorage, currentStageNum, map, holeCoveredCheckArray)  // 플레이어 이동 시 스테이지 패스 체크
        }


        fun printCurrentMap(map: Array<Array<Int>>, holeCoveredCheckArray: Array<Array<Int>>){
            println()
            for(i in 0..map.size-1){
                for(j in 0..map[i].size-1){
                    when(map[i][j]){
                        0 -> print('#')
                        1 -> print('O')
                        2 -> {
                            if(holeCoveredCheckArray[i][j] == 2)
                                print('0')
                            else
                                print('o')
                        }
                        3 -> print('P')
                        5 -> print(' ')
                    }
                }
                println()
            }
            println()
        }

        fun checkStagePass(mapStorage: MapStorage, currentStageNum: Int,
                           map: Array<Array<Int>>, holeCoveredCheckArray: Array<Array<Int>>){
            var isPass = true
            for(i in 0..map.size-1){
                for(j in 0..map[i].size-1){
                    if(map[i][j] == 2 && holeCoveredCheckArray[i][j] != 2){
                        isPass = false
                    }
                }
            }
            if(isPass){ // 현재 맵에서 모든 공의 위치가 구멍 위라면 곧 통과
                println("빠밤! Stage ${currentStageNum} 클리어!")
                println()
                mapStorage.startGame(currentStageNum+1)
            }
        }

    }

}


// 인덱스 기준 값
data class PlayerLocation(
    var playerY : Int,
    var playerX : Int
)

