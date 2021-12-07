# 2단계 - 플레이어 이동 구현하기	

<details markdown="1">
<summary>문제 설명 및 요구사항</summary>

<!--summary 아래 빈칸 공백 두고 내용을 적는공간-->
<br>	

1단계 스테이지 2의 지도를 읽고 사용자 입력을 받아서 캐릭터를 움직이게 하는 프로그램을 작성하시오.

## 입력명령
<pre>	
<code>	
- w: 위쪽
- a: 왼쪽
- s: 아래쪽
- d: 오른쪽
- q: 프로그램 종료
</code>	
</pre>	
<br>	

## 요구사항
- 처음 시작하면 스테이지 2의 지도를 출력한다.
- 간단한 프롬프트 (예: SOKOBAN> )를 표시해 준다.
- 하나 이상의 문자를 입력받은 경우 순서대로 처리해서 단계별 상태를 출력한다.
- 벽이나 공등 다른 물체에 부딪히면 '해당 명령을 수행할 수 없습니다' 라는 메시지를 출력하고 플레이어를 움직이지 않는다.

<br>	

## 동작예시	
<pre>	
<code>	
Stage 2

  #######
###  O  ###
#    o    #
# Oo P oO #
###  o  ###
 #   O  # 
 ########

SOKOBAN> ddzw (엔터)

  #######
###  O  ###
#    o    #
# Oo  PoO #
###  o  ###
 #   O  # 
 ########
 
 D: 오른쪽으로 이동합니다.
 
  #######
###  O  ###
#    o    #
# Oo  PoO #
###  o  ###
 #   O  # 
 ########
 
 D: (경고!) 해당 명령을 수행할 수 없습니다!
 
  #######
###  O  ###
#    o    #
# Oo  PoO #
###  o  ###
 #   O  # 
 ########
 
 Z: (경고!) 해당 명령을 수행할 수 없습니다!
 
  #######
###  O  ###
#    o    #
# Oo  PoO #
###  o  ###
 #   O  # 
 ########
 
 W: 위로 이동합니다.
 
SOKOBAN> q
Bye~
</code>	
</pre>	

## 2단계 코딩 요구사항	
- 너무 크지 않은 함수 단위로 구현하고 중복된 코드를 줄이도록 노력한다
- 마찬가지로 Readme.md 파일과 작성한 소스 코드를 모두 기존 secret gist에 올려야 한다
- 전역변수의 사용을 자제한다
- 객체 또는 배열을 적절히 활용한다
</details>

***

## 구조
Step1에서 기능은 요구사항에 맞게 동작하지만 메서드로만 전체적인 구성을 하다보니 변수 관리에서 헷갈리는 부분 등이 발생하여 기능별 클래스 정의 후 요구사항을 구현하는 과정에서 이전보다 코드의 가독성을 높이려 노력했다

- [main 함수](#main)
- [Map 클래스](#map)
- [MapStorage 클래스](#mapstorage)
- [ReadMap 클래스](#readmap)
- [GameStart 클래스](#gamestart)
- [SetGame 클래스](#setgame)
- [PlayGame 클래스](#playgame)
- [PlayerLocation 데이터 클래스](#playerlocation)

<br>

### main
```kotlin
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

    mapStorage.startGame(2)  //스테이지 입력
}
```
- 스테이지 문자열을 입력받고 줄 단위로 리스트화 한다
- 스테이지 별 맵 정보([Map 클래스](#map)에 해당)를 리스트 형태로 담을 [MapStorage](#mapstorage) 객체를 생성한다
- [ReadMap](#readmap) 객체를 생성하여 입력 문자열에서 스테이지 별로 Map을 만들고 위에서 생성한 mapStorage에 각각 추가해준다
- 원하는 스테이지 단계 값을 mapStorage의 startGame에 인자로 보내면 해당 스테이지로 프로그램이 수행된다
<br>

### Map
```kotlin
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

    }
}
```
- 맵의 각 줄이 문자열 형태의 원소로 존재하는 mapValueList 현재 스테이지 번호 stageNum를 인자로 전달받는다
- mapIntArray 는 문자열 형태에서 정수 형태로 변환하여 저장시킬 2차원 배열이다
- mapHeight, mapWidth = 맵의 세로/가로 길이를 나타내는 변수
- findMapSize 메서드를 통해 배열의 초기화를 위해 현재 맵의 가로/세로 길이 값을 구한다
```kotlin
fun findMapSize(){
        mapHeight = mapValueList.size

        for(i in 0..mapValueList.size-1){
            var line = mapValueList[i]

            if(line.length > mapWidth)
                mapWidth = line.length
        }

        mapIntArray = Array(mapHeight, {Array(mapWidth, {-1})})
    }
```
- 맵의 세로길이는 곧 맵을 나타내는 문자열의 리스트의 크기이다(세로길이 = 줄 개수)
- 맵의 줄별 길이를 비교해서 가장 긴 줄의 값이 곧 맵을 저장할 정수형 2차원 배열의 가로길이가 된다
- 구한 mapHeight, mapWidth 값을 통해 2차원 정수형 배열을 생성하고 -1로 우선 초기화한다
```kotlin
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
```
- line은 맵의 줄을 나타낸다(문자열 형태)
- 문자 별로 어떤 문자인지 검사 후 해당 정수로 변환하여 저장한다
- 문자열에 포함된 공백은 5의 값으로 저장했다
```kotlin
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
```
- printStage : 선택한 스테이지를 출력해준다
- printMap : 선택한 스테이지의 맵을 출력해준다
맵을 나타내는 정수형 2차원 배열을 다시 해당하는 문자로 바꾸어 출력한다

<br>

### MapStorage
```kotlin
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
```
- mapList : main 함수에서 입력한 문자열에서 스테이지를 구분하여 만들어진 Map들을 담는다
- addMap : mapList에 Map을 추가하고 해당 Map에서 2차원 정수형 배열로 맵 정보를 저장하도록 명령한다
- startGame : main 함수에서 설정한 스테이지로 게임을 진행하도록 한다 

<br>

### ReadMap
```kotlin
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
```
- main 함수에서 입력한 문자열을 읽어 스테이지 별로 구분하는 역할을 담당한다
- mapValueList : 맵을 실제로 구성하는 문자열만 담는 리스트
- line : 입력 문자열을 줄 단위로 자른 경우 각 줄의 문자열을 나타낸다, 맵을 구성하는 경우 mapValueList에 추가된다
- 문자열에 'Stage', '=' 이 없고 마지막 행이 아닌 경우 line에 해당 문자열을 할당한다
그리고 '=' 을 포함하는 행이거나 마지막 행인 경우 그전까지 담긴 mapValueList를 보내 Map을 생성하고 MapStorage에 생성한 Map을 추가한다
- 위 과정을 통해 스테이지 별 맵이 구분되고 MapStorage에 각 스테이지 별 맵이 저장되게 된다

<br>

### GameStart
```kotlin
class GameStart(){

    companion object{
        fun startGame(map: Map){
            map.printStage()
            map.printMap()

            SetGame.setGame(map)
        }
    }
}
```
- main 함수에서 생성한 MapStorage에 원하는 스테이지 번호를 입력 후 Map 리스트에서 선택한 스테이지에 해당하는 Map을 GameStart 클래스로 전달하여 게임을 시작한다
- Map에서 현재 스테이지 번호와 맵의 모습을 출력하고 SetGame 클래스를 통해 게임의 초기 준비를 진행하게 된다

<br>

### SetGame
```kotlin
class SetGame(){
    companion object{
        fun setGame(map : Map){
            var temp = Array(map.mapHeight, {Array(map.mapWidth, {-1})})
            for(i in 0..map.mapHeight-1){
                for(j in 0..map.mapWidth-1){
                    temp[i][j] = map.mapIntArray[i][j]
                }
            }

            var holeCoveredCheckArray = Array(map.mapHeight, {Array(map.mapWidth, {false})})

            var playerLocation = findPlayerLocation(temp)

            while(true) {
                println()
                print("SOKOBAN> ")
                var command = readLine()

                if (command?.length == 0) {
                    println("no command : Bye~")
                    System.exit(0)
                } else {
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
```
- 게임 진행의 준비를 위한 역할을 담당한다
- 선택된 스테이지의 Map을 전달받고 게임 진행에서 사용할 맵 배열을 복사한다
```kotlin
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
```
- 플레이어의 초기 위치를 찾는 메서드이다
- [PlayerLocation 데이터 클래스](#playerlocation) 형태로 플레이어 위치를 저장한다
```kotlin
   fun setGame(map : Map){
            var temp = Array(map.mapHeight, {Array(map.mapWidth, {-1})})
            for(i in 0..map.mapHeight-1){
                for(j in 0..map.mapWidth-1){
                    temp[i][j] = map.mapIntArray[i][j]
                }
            }

            var holeCoveredCheckArray = Array(map.mapHeight, {Array(map.mapWidth, {false})})     
            var playerLocation = findPlayerLocation(temp)

            while(true) {
                println()
                print("SOKOBAN> ")
                var command = readLine()

                if (command?.length == 0) {
                    println("no command : Bye~")
                    System.exit(0)
                } else {
                    println()
                    PlayGame.playGame(playerLocation, temp, holeCoveredCheckArray, command!!)
                }
            }

        }
```
- temp : 게임 진행 시 사용할 선택된 스테이지의 맵 정보 배열을 복사한다
- holeCoveredCheckArray : 맵에 존재하는 구멍이 무언가에 덮혀있는 경우가 발생할 수 있으므로 덮힘 여부를 저장하는 배열을 생성했다
- playerLocation : 위에서 설명한 findPlayerLocation 메서드를 통해 얻은 플레이어 초기 위치이다
```kotlin
 while(true) {
                println()
                print("SOKOBAN> ")
                var command = readLine()

                if (command?.length == 0) {  // 바로 종료
                    println("no command : Bye~")
                    System.exit(0)
                } else {
                    println()
                    PlayGame.playGame(playerLocation, temp, holeCoveredCheckArray, command!!)
                }
            }
```
- 명령어를 입력받고 해당 명령어를 통해 PlayGame 클래스에서 플레이어의 이동이 이루어지게 된다
- 명령어가 빈 상태인 경우 바로 프로그램을 종료하도록 구현했다
- 한 명령어를 통한 이동이 끝나면 다음 명령어를 입력받을 수 있게 구현했다

<br>

### PlayGame
```kotlin
class PlayGame(){
    companion object{
        fun playGame(playerLocation : PlayerLocation, map : Array<Array<Int>>,
                     holeCoveredCheckArray : Array<Array<Boolean>>,  command : String){

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
                    
                    if(holeCoveredCheckArray[playerY][playerX]){ // 현재 플레이어 위치에 구멍이 있었다면
                        map[playerY][playerX] = 1  // 플레이어가 이동했으므로 구멍이 다시 보여야 하므로 1 할당
                        holeCoveredCheckArray[playerY][playerX] = false // 구멍이 다시 보이므로 false로 변경
                    }
                    else{ // 구멍 없었다면 현재 위치는 공백이 된다
                        map[playerY][playerX] = 5
                    }
                
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
                   
                    if(holeCoveredCheckArray[playerY][playerX]){ 
                        map[playerY][playerX] = 1 
                        holeCoveredCheckArray[playerY][playerX] = false
                    }
                    else{ // 현재 위치에 구멍 없었다면 현재 위치는 공백이 된다
                        map[playerY][playerX] = 5
                    }
                 
                    if(map[playerY][playerX-1] == 1){ 
                        map[playerY][playerX-1] = 3
                        holeCoveredCheckArray[playerY][playerX-1] = true 
                    }
                    else{ 
                        map[playerY][playerX-1] = 3
                    }

                    printCurrentMap(map)
                    println("${commandChar}: 왼쪽으로 이동합니다.")

                    playerLocation.playerX -= 1 
                }
                else{
                    printCurrentMap(map)
                    println("${commandChar}: (경고!) 해당 명령을 수행할 수 없습니다!")
                }
            }
            else if(commandChar == 's' || commandChar ==  'S'){  //아래쪽
                if(map[playerY+1][playerX] == 5 || map[playerY+1][playerX] == 1){
                    
                    if(holeCoveredCheckArray[playerY][playerX]){
                        map[playerY][playerX] = 1 
                        holeCoveredCheckArray[playerY][playerX] = false
                    }
                    else{
                        map[playerY][playerX] = 5
                    }
                 
                    if(map[playerY+1][playerX] == 1){ 
                        map[playerY+1][playerX] = 3
                        holeCoveredCheckArray[playerY+1][playerX] = true
                    }
                    else{
                        map[playerY+1][playerX] = 3
                    }

                    printCurrentMap(map)
                    println("${commandChar}: 아래쪽으로 이동합니다.")

                    playerLocation.playerY += 1 
                }
                else{
                    printCurrentMap(map)
                    println("${commandChar}: (경고!) 해당 명령을 수행할 수 없습니다!")
                }
            }
            else{ // d or D 인 경우  //오른쪽
                if(map[playerY][playerX+1] == 5 || map[playerY][playerX+1] == 1){
                    
                    if(holeCoveredCheckArray[playerY][playerX]){
                        map[playerY][playerX] = 1  
                        holeCoveredCheckArray[playerY][playerX] = false 
                    }
                    else{ 
                        map[playerY][playerX] = 5
                    }
                   
                    if(map[playerY][playerX+1] == 1){ 
                        map[playerY][playerX+1] = 3
                        holeCoveredCheckArray[playerY][playerX+1] = true 
                    }
                    else{
                        map[playerY][playerX+1] = 3
                    }

                    printCurrentMap(map)
                    println("${commandChar}: 오른쪽으로 이동합니다.")

                    playerLocation.playerX += 1 
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
            }
        }

    }
```
```kotlin
fun playGame(playerLocation : PlayerLocation, map : Array<Array<Int>>,
                     holeCoveredCheckArray : Array<Array<Boolean>>,  command : String)
```
- SetGame으로 부터 플레이어 위치, 맵 정보, 구멍 덮힘 여부 배열, 명령어를 전달 받는다
```kotlin
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
```
- 명령어 길이만큼 반복문을 수행한다 / 이 때 명령어에 q의 값이 있는 경우 프로그램을 종료하게 된다
- w,a,s,d(대문자 포함) 가 명령어로 주어진 경우 플레이어 이동 메서드 movePlayer를 호출하여 이동 가능 여부를 판단하고 이동을 하게 된다
- 명령어에 그 외의 문자가 있는 경우는 해당 문자를 보여주고 명령을 수행할 수 없다는 메세지를 같이 출력한다
```kotlin
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
  
            
            .... // 나머지 방향 또한 위와 같은 로직
   
 }
```
- 네 방향의 이동 로직이 같으므로 지금은 위쪽 명령어인 'w' 혹은 'W'의 경우를 통해 설명하겠다
- playerY, playerX 명령어 적용 전 플레이어의 위치를 받게 된다
```kotlin
  if(map[playerY-1][playerX] == 5 || map[playerY-1][playerX] == 1){
                    
                    if(holeCoveredCheckArray[playerY][playerX]){ 
                        map[playerY][playerX] = 1
                        holeCoveredCheckArray[playerY][playerX] = false 
                    }
                    else{
                        map[playerY][playerX] = 5
                    }
                
                    if(map[playerY-1][playerX] == 1){ 
                        map[playerY-1][playerX] = 3
                        holeCoveredCheckArray[playerY-1][playerX] = true 
                    }
                    else{
                        map[playerY-1][playerX] = 3
                    }

                    printCurrentMap(map)
                    println("${commandChar}: 위쪽으로 이동합니다.")

                    playerLocation.playerY -= 1 
                }
```
- 위쪽 이동이므로 현재 플레이어의 배열에서 y좌표(세로 이동) 값을 하나 감소시켜야 한다
- 새로운 위치가 현재 배열에서 어떤 값인지를 파악한다
- 1:구멍 or 5:공백의 경우 해당 위치로 이동이 가능하다
- 이동 전 현재 위치에 구멍이 존재해서 덮여있는 상태였다면 플레이어가 이동하면서 구멍이 다시 보이게 되므로 현재 위치에서 구멍 덮힘 여부 배열의 값을 false로 변경해주고 맵의 상태도 1(구멍)로 갱신해준다,  그렇지 않다면 원래 공백인 위치였으므로 5(공백)로 갱신한다
- 플레이어가 이동할 다음 위치에 만약 구멍이 존재한다면 해당 위치에서 구멍 덮힘 여부 배열의 값을 true로 바꿔준다, 이 위치는 구멍이 덮여있음을 의미하게 된다
- 플레이어가 이동을 했으므로 해당위치의 값을 3으로 갱신한다
- printCurrentMap : 플레이어의 이동 후 맵 정보를 출력한다
- playerLocation.playerY -= 1  : 이동 후 해당 이동에 맞게 playerLocation에서 플레이어 위치 정보를 갱신해준다
```kotlin
  else{
       printCurrentMap(map)
       println("${commandChar}: (경고!) 해당 명령을 수행할 수 없습니다!")
      }
```
- 만약 이동하려는 다음 위치에서 배열의 값이 1 혹은 5가 아니라면  공 or 벽이 존재하고 있으므로 이동이 불가하다
- 따라서 그대로 원상태의 맵 정보가 출력될 것이고 이동할 수 없다는 메세지를 같이 출력해준다
```kotlin
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
          }
```
- 매 명령어 수행 후 맵의 상태를 나타내기 위해 호출하는 메서드이다
- 출력시에는 각 정수에 해당하는 문자로 변환하여 맵의 상태를 출력한다

<br>

### PlayerLocation
```kotlin
data class PlayerLocation(
    var playerY : Int,
    var playerX : Int
)
```
- 맵(2차원 배열)에서 플레이어의 위치를 나타내기 위한 데이터 클래스이다
- 맵의 좌측 상단 원소 (0,0) 기준으로 나타내게 된다(배열의 인덱스 값)
