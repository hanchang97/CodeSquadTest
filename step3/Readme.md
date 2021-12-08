# 3단계 - 플레이어 이동 구현하기	

<details markdown="1">
<summary>문제 설명 및 요구사항</summary>

<!--summary 아래 빈칸 공백 두고 내용을 적는공간-->
<br>	

- 정상적인 소코반 게임을 완성한다.
- https://www.cbc.ca/kids/games/play/sokoban 를 참고하자.

<br>

## 요구사항
- 난이도를 고려하여 스테이지 1부터 5까지 플레이 가능한 map.txt 파일을 스스로 작성한다.
- 지도 파일 map.txt를 문자열로 읽어서 처리하도록 개선한다.
- 처음 시작시 Stage 1의 지도와 프롬프트가 표시된다.
- r 명령 입력시 스테이지를 초기화 한다.
- 모든 o를 O자리에 이동시키면 클리어 화면을 표시하고 다음 스테이지로 표시한다.
- 주어진 모든 스테이지를 클리어시 축하메시지를 출력하고 게임을 종료한다.
<br>

## 참고: 플레이어 이동조건
- 플레이어는 o를 밀어서 이동할 수 있지만 당길 수는 없다.
- o를 O 지점에 밀어 넣으면 0으로 변경된다.
- 플레이어는 O를 통과할 수 있다.
- 플레이어는 #을 통과할 수 없다.
- 0 상태의 o를 밀어내면 다시 o와 O로 분리된다.
- 플레이어가 움직일 때마다 턴수를 카운트한다.
- 상자가 두 개 연속으로 붙어있는 경우 밀 수 없다.
- 기타 필요한 로직은은 실제 게임을 참고해서 완성한다.
<br>

## 실행예시
<pre>	
<code>	
소코반의 세계에 오신 것을 환영합니다!
^오^

Stage 1

#####
#OoP#
#####

SOKOBAN> A

#####
#0P #
#####

빠밤! Stage 1 클리어!
턴수: 1

Stage 2
...

Stage 5
...

빠밤! Stage 5 클리어!
턴수: 5

전체 게임을 클리어하셨습니다!
축하드립니다! 
</code>	
</pre>	
<br>	

## 3단계 코딩 요구사항	
- 가능한 한 커밋을 자주 하고 구현의 의미가 명확하게 전달되도록 커밋 메시지를 작성한다
- 함수나 메소드는 한 번에 한 가지 일을 하고 가능하면 20줄이 넘지 않도록 구현한다
- 함수나 메소드의 들여쓰기를 가능하면 적게(3단계까지만) 할 수 있도록 노력한다
```kotlin
function main() {
        for() { // 들여쓰기 1단계
            if() { // 들여쓰기 2단계
                return; // 들여쓰기 3단계
            }
        }
    }
```
</details>

<details markdown="1">
<summary>1~5단계 완료 시</summary>

<!--summary 아래 빈칸 공백 두고 내용을 적는공간-->
<br>	

<pre>
<code>
Stage 1

&#35;&#35;&#35;&#35;&#35;
&#35;OoP&#35;
&#35;&#35;&#35;&#35;&#35;

SOKOBAN> a

&#35;&#35;&#35;&#35;&#35;
&#35;0P &#35;
&#35;&#35;&#35;&#35;&#35;

a: 왼쪽으로 이동합니다.
빠밤! Stage 1 클리어!
턴수: 1

...

Stage 5

  &#35;&#35;&#35;&#35;
&#35;&#35;&#35;  &#35;
&#35;P Oo&#35;&#35;
&#35;   o &#35;
&#35; &#35;O  &#35;
&#35;     &#35;
&#35;&#35;&#35;&#35;&#35;&#35;&#35;

...

 &#35;&#35;&#35;&#35;
&#35;&#35;&#35;  &#35;
&#35;  0 &#35;&#35;
&#35;     &#35;
&#35; &#35;OoP&#35;
&#35;     &#35;
&#35;&#35;&#35;&#35;&#35;&#35;&#35;

s: 아래쪽으로 이동합니다.

SOKOBAN> a

  &#35;&#35;&#35;&#35;
&#35;&#35;&#35;  &#35;
&#35;  0 &#35;&#35;
&#35;     &#35;
&#35; &#35;0P &#35;
&#35;     &#35;
&#35;&#35;&#35;&#35;&#35;&#35;&#35;

a: 왼쪽으로 이동합니다.
빠밤! Stage 5 클리어!
턴수: 35

전체 게임을 클리어하셨습니다!
축하드립니다!

</code>
</pre>

</details>

<details markdown="1">
<summary>공 밀기, 공/구멍 분리 예시</summary>

<br>	

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

SOKOBAN> w


  #######
###  0  ###
#    P    #
# Oo   oO #
###  o  ###
 #   O  #
 ########

w: 위쪽으로 이동합니다.    // 공이 구멍에 들어감

SOKOBAN> dw


  #######
###  0  ###
#     P   #
# Oo   oO #
###  o  ###
 #   O  #
 ########

d: 오른쪽으로 이동합니다.

  #######
###  0P ###
#         #
# Oo   oO #
###  o  ###
 #   O  #
 ########

w: 위쪽으로 이동합니다.

SOKOBAN> a


  #######
### oP  ###
#         #
# Oo   oO #
###  o  ###
 #   O  #
 ########

a: 왼쪽으로 이동합니다.   // 공을 구멍에서 밀어내고 구멍 위에 플레이어 위치

SOKOBAN> s


  #######
### oO  ###
#    P    #
# Oo   oO #
###  o  ###
 #   O  #
 ########

s: 아래쪽으로 이동합니다.  // 플레이어 구멍에서 나옴

SOKOBAN> 

</code>
</pre>

</details>

***

## 구조
Step2의 구조와 유사하며 스테이지 txt 파일 읽어오기, 플레이어 이동 관련 기능들이 추가됨
- txt 파일을 추가해줘야 한다(참고 : https://github.com/hanchang97/CodeSquadTest/tree/main/step3)

- main 함수
- Map 클래스
- MapStorage 클래스
- ReadMap 클래스
- GameStart 클래스
- SetGame 클래스
- PlayGame 클래스
- PlayerLocation 데이터 클래스
- MoveCount 클래스

<br>

### main 
```kotlin
fun main(args: Array<String>){

    /*
    val listOfLines = mutableListOf<String>()
    File("SokobanMap.txt").bufferedReader().useLines {
                lines -> lines.forEach {
            listOfLines.add(it)
        }
    }
    if(listOfLines.size == 0){
        println("map이 비었습니다")
        System.exit(0)
    }
    */
    // SokobanMap.txt를 읽어서 사용하는 경우 위 주석을 해제한다, 사용하지 않는 경우 주석처리 해준다

    // target 변수는 SokobanMap.txt의 내용과 같은 문자열 값이다
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
            " ########\n" +
            "=====\n" +
            "Stage 3\n" +
            "######\n" +
            "#P   #\n" +
            "#  oO#\n" +
            "# Oo #\n" +
            "######\n" +
            "=====\n" +
            "Stage 4\n" +
            "######\n" +
            "#P   ##\n" +
            "# oo  #\n" +
            "# #O O#\n" +
            "#     #\n" +
            "#######\n" +
            "=====\n" +
            "Stage 5\n" +
            "  ####\n" +
            "###  #\n" +
            "#P Oo##\n" +
            "#   o #\n" +
            "# #O  #\n" +
            "#     #\n" +
            "#######"

    var changeTarget = target.split("\n")

    var mapStorage = MapStorage()

    // 위의 Stage 문자열인 target 변수를 사용하는 경우
    var readMap = ReadMap(changeTarget, mapStorage) //SokobanMap.txt.를 사용하는 경우 현재 라인을 주석처리 해준다

    //var readMap = ReadMap(listOfLines, mapStorage)   // SokobanMap.txt를 읽어서 사용하는 경우 주석을 해제 해주고 사용하지 않으면 주석처리 해준다

   readMap.convertMap()

    mapStorage.startGame(1)  //스테이지 입력
}
```
- Stage map txt 파일을 읽어서 사용하는 경우와 txt 파일과 내용이 같은 문자열 변수를 사용하는 경우를 나누어 2가지 방법을 작성했다
<br>

### Map 
- Step2 와 동일
<br>

### MapStorage 
```kotlin
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
```
- startGame 메서드 수정
- 스테이지 완수 시 다음 스테이지 번호로 startGame을 호출하게 되는데 마지막 스테이지가 끝나면 현재 맵의 개수보다 하나 많은 값을 보내게 되고 이를 감지하여 전체 게임을 완료했다는 문구와 함께 프로그램이 종료된다
<br>

### ReadMap 
- Step2와 동일
<br>

### GameStart
```kotlin
class GameStart(){

    companion object{
        fun startGame(map: Map, mapStorage: MapStorage){
            map.printStage()
            map.printMap()

            SetGame.setGame(map, mapStorage)
        }
    }
}
```
- setGame을 하면 main에서 생성한 MapStorage 객체도 같이 넘겨주도록 수정했다
- 스테이지 완료 후 다음 스테이지로 넘어가거나 현재 스테이지를 리셋하는 경우 사용하기 위
<br>

### SetGame 
```kotlin
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

        ....
    }
}
```
- 구멍이 공, 플레이어 2가지 경우로 덮힐 수 있으므로 기존의 holderCoveredCheckArray를 정수형으로 수정했다
1 = 구멍위에 아무것도 없음, 2 = 구멍위에 공이 있음, 3 = 구멍위에 플레이어 있음
- 명령어 입력 후 playGame 메서드를 호출하는 경우 MapStorage 객체와 현재 스테이지 번호를 추가적으로 같이 넘기도록 

<br>

### PlayGame 
- PlayGame 클래스 내부 메서드 별로 설명을 작성하겠다
```kotlin
 fun playGame(mapStorage: MapStorage, currentStageNum : Int, playerLocation : PlayerLocation,
                     map : Array<Array<Int>>, holeCoveredCheckArray : Array<Array<Int>>,  command : String){
            for(i in 0..command.length-1){
                if(command[i] == 'q'){  //종료
                    println("\nBye~")
                    System.exit(0)
                }
                else if(command[i] == 'r'){ // 현재 스테이지 다시 시작
                    println("현재 스테이지 다시 시작")
                    MoveCount.clearCount()
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
```
- 전달받은 명령어를 바탕으로 명령을 내리는 부분이다
- 'r' 입력 시 현재 스테이지를 초기화하는 기능을 추가했다
이 때 스테이지가 초기화 되면서 플레이어의 턴수도 같이 초기화된다
- MapStorage 객체를 통해 현재 스테이지 번호를 넘겨 현재 스테이지를 다시 시작하게 된다
- 이동 명령인 movePlayer 메서드를 호출하는 경우 MoveStorage 객체와 현재 스테이지 번호를 추가로 넘기도록 수정했다
<br>

```kotlin
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
```
- 이동 명령어의 값을 검사하고 플레이어의 이동 방향을 정한다
- 알맞은 방향값 설정 후 moveByCommand 메서드를 호출한다
- 방향은 0,1,2,3 이 각각 상,하,좌,우를 의미하게 된다
<br>

```kotlin
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
```
- 방향에 맞게 플레이어의 다음 위치를 판단한다
- 설정된 방향으로 플레이어 기준 한 칸을 이동하는 경우 그 위치가 빈 공간 or 구멍 or 공 or 벽 으로 나누어 각각 해당하는 메서드를 호출한다
- 공이 이동하는 경우 공이 새로 위치할 곳의 위치값도 계산하여 메서드 호출 시 같이 보내도록 구현했다

<br>

```kotlin
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
```
- 플레이어의 다음 위치가 빈 공간인 경우 호출되는 메서드이다
- 이동 전 현재 플레이어가 있는 곳에 구멍이 있었는지를 판단하여 플레이어 이동 시 구멍이 다시 보이게 구현했다
- 구멍이 없었다면 플레이어 이동 후 빈 공간의 상태가 된다
- 새로 이동한 위치에는 플레이어가 존재하므로 값을 갱신해준다
- 이동 후 스테이지 상태의 출력과 검사 기능을 수행하는 afterMove 메서드를 호출한다

<br>

```kotlin
fun playerMoveToHole(map: Array<Array<Int>>, holeCoveredCheckArray: Array<Array<Int>>, commandChar: Char, commandString : String,
   currentY : Int, currentX : Int, nextY : Int, nextX : Int, ballSnextY : Int, ballSnextX : Int, playerLocation:PlayerLocation,
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
```
- 플레이어의 다음 위치가 빈 구멍인 경우 호출되는 메서드이다
- 이동 전 현재 플레이어가 있는 곳에 구멍이 있었는지를 판단하여 플레이어 이동 시 구멍이 다시 보이게 구현했다
- 구멍이 없었다면 플레이어 이동 후 빈 공간의 상태가 된다
- 새로 이동한 위치에는 플레이어가 존재하므로 값을 갱신해준다
이 때, 구멍이 플레이어에 의해 덮였으므로 해당위치의 holeCoveredCheckArray 값을 3으로 갱신한다
- 이동 후 스테이지 상태의 출력과 검사 기능을 수행하는 afterMove 메서드를 호출한다

<br>

```kotlin
 fun playerPushBall(map: Array<Array<Int>>, holeCoveredCheckArray: Array<Array<Int>>, commandChar: Char, commandString : String,
      currentY : Int, currentX : Int, nextY : Int, nextX : Int, ballsNextY : Int, ballsNextX : Int, playerLocation : PlayerLocation,
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
```
- 플레이어의 다음 위치에 공이 있는 경우 호출되는 메서드이다
- 공을 밀고 이동이 가능한지를 판단해야 한다
- 공의 다음위치가 빈 공간인 경우, 빈 구멍인 경우에 이동하도록 각각 BallMoveToEmpty, BallMoveToHole 메서드를 호출한다
- 그 외의 경우에는 공을 밀고 이동이 불가하므로 경고 메세지를 출력한다

<br>

```kotlin
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
```
- 공의 다음 위치가 빈 공간인 경우 호출되는 메서드이다
- 공이 이동전 구멍위에 있었다면 이제 플레이어가 그 구멍을 덮게 되므로 holeCoveredCheckArray 값을 갱신한다
- 공이 이동한 위치 값을 갱신한다
- 플레이어가 이동한 위치 값을 갱신한다
- 플레이어가 기존에 있던 곳이 구멍 위였다면 구멍이 다시 보이도록 holeCoveredCheckArray 값을 갱신한다
- 이동 후 스테이지 상태의 출력과 검사 기능을 수행하는 afterMove 메서드를 호출한다

<br>

```kotlin
fun BallMoveToHole(map: Array<Array<Int>>, holeCoveredCheckArray: Array<Array<Int>>, commandChar: Char, commandString : String,
           currentY : Int, currentX : Int, nextY : Int, nextX : Int, ballsNextY : Int, ballsNextX : Int, playerLocation: PlayerLocation
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
```
- 공의 다음 위치가 빈 구멍인 경우 호출되는 메서드이다
- 공이 이동전 구멍위에 있었다면 이제 플레이어가 그 구멍을 덮게 되므로 holeCoveredCheckArray 값을 갱신한다
- 공이 이동한 위치 값을 갱신한다
- 공이 이동한 위치의 구멍이 공에 의해 덮여지므로 holeCoveredCheckArray 값을 갱신한다
- 플레이어가 이동한 위치 값을 갱신한다
- 플레이어가 기존에 있던 곳이 구멍 위였다면 구멍이 다시 보이도록 holeCoveredCheckArray 값을 갱신한다
- 이동 후 스테이지 상태의 출력과 검사 기능을 수행하는 afterMove 메서드를 호출한다

<br>

```kotlin
 fun afterMove(map : Array<Array<Int>>, holeCoveredCheckArray: Array<Array<Int>>, commandChar: Char, commandString: String,
                      playerLocation: PlayerLocation, nextY: Int, nextX: Int, mapStorage: MapStorage, currentStageNum: Int){

            printCurrentMap(map, holeCoveredCheckArray)
            println("${commandChar}: ${commandString}으로 이동합니다.")

            MoveCount.moveCount++ // 턴수 증가

            playerLocation.playerY = nextY
            playerLocation.playerX = nextX  // 플레이어 위치 업데이트

            checkStagePass(mapStorage, currentStageNum, map, holeCoveredCheckArray)  // 플레이어 이동 시 스테이지 패스 체크
        }
```
- 플레이어가 움직인 후 호출되는 메서드이다
- printCurrentMap 을 통해 이동 후의 맵 상태를 출력한다
- 플레이어의 턴수를 증가시킨다
- 플레이어의 현재 위치를 새로 갱신한다
- 플레이어 이동 후 checkStagePass 메서드를 통해 스테이지 완료 여부를 검사한다

<br>

```kotlin
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
                println("턴수: ${MoveCount.moveCount}")
                println()
                MoveCount.clearCount() // 턴수 초기화
                mapStorage.startGame(currentStageNum+1)
            }
        }
```
- 스테이지 완료 여부를 검사하는 메서드이다
- 맵에서 모든 공이 구멍위에 있는 경우 스테이지가 완료되도록 구현했다
- 완료한 스테이지에서의 턴수를 출력해주고 다음 스테이지를 위해 턴수를 초기화한다
- 스테이지 완료의 경우 전달받은 스테이지 번호를 하나 증가시켜 다음 스테이지를 호출한다
- 
<br>

### PlayerLocation 
- Step2와 동일
<br>

### MoveCount
```kotlin
class MoveCount(){
    companion object{
        var moveCount = 0

        fun clearCount(){
            moveCount = 0
        }
    }
}
```
- 스테이지에서 플레이어 이동 시 턴수를 세기 위한 클래스
- 턴수를 나타내는 변수와 초기화 메서드가 포함되어 있다
