# 1단계 - 지도 데이터 출력하기	


### 지도 데이터 읽어서 2차원 배열에 저장하고 화면에 출력하기	


<details markdown="1">
<summary>문제 설명 및 요구사항</summary>

<!--summary 아래 빈칸 공백 두고 내용을 적는공간-->
<br>	

1. 입력: 아래 내용을 문자열로 넘겨서 처리하는 함수를 작성한다	
<pre>	
<code>	
Stage 1	
#####	
#OoP#	
#####	
=====	
Stage 2	
  #######	
###  O  ###	
#    o    #	
# Oo P oO #	
###  o  ###	
 #   O  # 	
 ########	
</code>	
</pre>	
<br>	
2. 위 값을 읽어 2차원 배열로 변환 저장한다.	

|기호|의미|저장값|	
|---|---|---|	
|#|벽(Wall)|0|	
|O|구멍(Hall)|1|	
|o|공(Ball)|2|	
|P|플레이어(Player)|3|	
|=|스테이지 구분|4|	

<br>	
3. 출력할 내용	

아래와 같은 형태로 각 스테이지 정보를 출력한다.	
  + 플레이어 위치는 배열 [0][0]을 기준으로 처리한다	
    + 아래 출력 예시와 상관없이 기준에 맞춰서 얼마나 떨어진지 표시하면 된다	
  + 스테이지 구분값은 출력하지 않는다	

<pre>	
<code>	
Stage 1	

#####	
#OoP#	
#####	

가로크기: 5	
세로크기: 3	
구멍의 수: 1	
공의 수: 1	
플레이어 위치 (2, 4)	

Stage 2	

  #######	
###  O  ###	
#    o    #	
# Oo P oO #	
###  o  ###	
 #   O  # 	
 ########	

가로크기: 11	
세로크기: 7	
구멍의 수: 4	
공의 수: 4	
플레이어 위치 (4, 6)	
</code>	
</pre>	

## 1단계 코딩 요구사항	
+ 파일 또는 실행이 가능해야 한다. (컴파일이나 실행되지 않을 경우 감점 대상)	
  + gist는 하위 폴더 구조를 지원하지 않기 때문에 컴파일 또는 실행에 필요한 소스 코드는 모두 포함하고, 프로젝트 파일 등은 포함하지 않아도 된다.	
+ 자기만의 기준으로 최대한 간결하게 코드를 작성한다.	
+ Readme.md에 풀이 과정 및 코드 설명, 실행 결과를 기술하고 코드와 같이 gist에 포함해야 한다.	
+ 제출시 gist URL과 revision 번호를 함께 제출한다.	
</details>

***

## 구조
- main : 입력값을 받고 배열의 변환,저장,출력을 명령하는 메서드
- getWidth : 문자열 입력값을 배열로 저장할 시 저장할 배열의 가로 길이를 구하는 메서드
- saveMap : 문제의 조건에 맞게 입력값들을 정수로 변환하여 배열에 저장하는 메서드
- printMap : 문제의 조건에 맞게 Stage의 맵 구성요소와, 가로/세로 크기, 공/구멍 개수, 플레이어 위치를 출력하는 메서드
<br>

### 1. main
1. 문제에 주어진 문자열을 줄 단위로 나누어 리스트에 저장한다

```kotlin
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
            " #   O  # \n" +
            " ########"

var changeTarget = target.split("\n")  // 개행 단위로 문자열 자르고 리스트화
```
<br>
 
 2. 위 문자열을 저장할 2차원 배열의 세로, 가로 길이를 구한다
```kotlin
 var Height = changeTarget.size
 var Width = getWidth(changeTarget)
```
- 입력한 문자열이 줄별로 리스트화 되었으므로 changeTarget의 크기가 2차원 배열의 세로 길이(= 줄의 개수)가 된다
- 배열의 가로의 길이는 getWidth 메소드를 통해 구한다&nbsp;&nbsp;  [getWidth 메서드](#getwidth)

<br>

3. 입력 문자열을 변환해서 정수형 2차원 배열에 저장한다
```kotlin
Height += 1
var saveArray = Array(Height, {Array(Width, {-1})})

saveMap(changeTarget, saveArray)
```
- Height += 1 : 2차원 배열에서 현재 마지막 행이 마지막 스테이지의 맵에서 마지막 줄을 포함하고 있다  출력의 편의성을 위해 행 1개를 추가해줬고 추가한 행에 도달한 경우 마지막 스테이지의 출력을 명령하게 된다
따라서 Height의 값을 하나 증가시켜줬다(추가한 행은 -1로만 채워진다)
- Height, Width 값을 사용하여 2차원 배열을 생성하고 모두 -1의 값으로 초기화 한다
- saveMap 메서드를 통해 입력받은 문자열을 2차원 정수형 배열에 저장한다&nbsp;&nbsp;  [saveMap 메서드](#savemap)

<br>

4. 단계를 나타낼 변수와 각 스테이지 별 실제 맵을 구성하는 요소들을 저장할 2차원 배열을 생성한다
```kotlin
var stageNum = 1
var mapIntArray = ArrayList<ArrayList<Int>>()  // 맵별 정수형 배열
```
- stageNum : 단계를 나타내는 정수형 변수
- mapIntArray : 각 스테이지별로 실제 맵을 구성하는 유의미한 값들만을 담을 2차원 ArrayList 이다.
현재 정수형 2차원 배열에서 각 스테이지의 맵을 구성하는 부분만 뽑아서 출력하고, 그 외 값들을 구하기 위함이다.
ex>
<pre>
-1 -1 -1 -1 -1 5 -1 -1 -1 -1 -1 
0 0 0 0 0 -1 -1 -1 -1 -1 -1 
0 1 2 3 0 -1 -1 -1 -1 -1 -1 
0 0 0 0 0 -1 -1 -1 -1 -1 -1 
4 4 4 4 4 -1 -1 -1 -1 -1 -1 
-1 -1 -1 -1 -1 5 -1 -1 -1 -1 -1 
5 5 0 0 0 0 0 0 0 -1 -1 
0 0 0 5 5 1 5 5 0 0 0 
0 5 5 5 5 2 5 5 5 5 0 
0 5 1 2 5 3 5 2 1 5 0 
0 0 0 5 5 2 5 5 0 0 0 
5 0 5 5 5 1 5 5 0 5 -1 
5 0 0 0 0 0 0 0 0 -1 -1 
-1 -1 -1 -1 -1 -1 -1 -1 -1 -1 -1 
</pre>
saveMap 메서드 결과 입력 문자열이 위와 같이 2차원 정수형 배열로 변환이 된다.
이때 Stage1의 경우 -1를 제외한 실제 맵을 구성하는 부분 {(0,0,0,0,0), (0,1,2,3,0), (0,0,0,0,0)}을 골라 내어 mapIntArray에 저장을 하고 이를 사용하여 출력을 하게 된다
(위 3번에서 설명과 같이 Height에 1을 더해만든 -1로 구성되어 있는 행이 마지막 줄에 있는 것을 볼 수 있다)

<br>

5. 변환하여 저장된 2차원 정수형 배열에서 반복문을 수행하며 문제의 요구조건에 맞게 값을 출력한다
```kotlin
  for(i in 0..Height-1){
        var lineList = ArrayList<Int>()   // 현재 행이 맵의 구성요소를 포함하는 행인 경우 해당 행에서 맵의 구성요소를 담기 위한 리스트

        var flag1 = true
        if(saveArray[i][0] == -1){
            flag1 = false
        }

        var flag2 = true
        if(saveArray[i][0] == 4){
            flag2 = false
        }

        if(i == 0) {   // 첫 번째 행의 경우 Stage 문구 출력한다
            println("Stage " + stageNum++)  // Stage 출력 후 값을 하나 증가시킨다
            println()
        }

        if(saveArray[i][0] == 4) { // 스테이지가 나눠지는 시점이므로 현재 스테이지를 출력하고 다음 스테이지를 위해 mapIntArray 초기화
            printMap(mapIntArray)  // (자세한 설명은 아래 내용 )
            mapIntArray.clear()

            println("Stage " + stageNum++)
            println()
        }

        if(i == Height-1) {  // 마지막 스테이지의 출력을 위함
            printMap(mapIntArray)
        }

        //
        for(j in 0..Width-1){
            if(flag1 && flag2 && saveArray[i][j] != -1 && saveArray[i][j] != 4){
                lineList.add(saveArray[i][j])
            }
        }

        if(flag1 && flag2){
            mapIntArray.add(lineList)
        }

    }
```
- lineList : 현재 행이 맵의 구성요소를 포함하는 행인 경우 해당 행에서 맵의 구성요소를 담기 위한 1차원 리스트이다. 맵의 한 줄을 담게되고 이를 mapIntArray(2차원 리스트)에 추가하게 된다.
ex> Stage1의 경우 예를 들어 맵의 구성요소가 있는 행이 1,2,3 행이라고 가정한다면 반복문에서 행의 값이 1인 경우, 2인 경우, 3인 경우 각각 lineList에 맵의 구성요소들이 추가되고 이 세개의 lineList가 mapIntArray에 각각 추가된다
-> mapIntArray에는 Stage1의 맵을 구성하는 요소들이 저장되게 된다
- flag1 & flag2 : Boolean 변수이며 2차원 배열에서 n행 0열의 값이 -1 혹은 4인 경우에는  맵의 구성요소들이 있는 행이 아니므로 각각 false의 값을 할당한다
flag1 과 flag2가 동시에 참인 경우 맵의 구성요소가 있는 행에 해당되므로 이 경우에만 위에서 설명한 lineList에 값을 추가하게 된다
```kotlin
 if(saveArray[i][0] == 4) {
            printMap(mapIntArray)
            mapIntArray.clear()

            println("Stage " + stageNum++)
            println()
        }
```
- 현재 행의 시작값이 4인 경우 스테이지 구분이 되는 행이므로 현재 스테이지를 출력하기 위해 printMap 메서드를 사용한다
- printMap 메서드를 통해 요구조건에 해당하는 현재 스테이지의 맵, 가로/세로 크기, 구멍/공 개수, 플레이어 위치를 출력하게  
- [printMap 메서드](#printmap)
- 현재 mapIntArray에는 현재 스테이지의 lineList들이 저장되어 있다. 위에서 printMap을 통해 현재 스테이지에 관한 출력을 수행했으므로 다음 스테이지의 맵 구성요소를 새로 담기 위해 mapIntArray를 초기화 해준다
- 또한 한 스테이지가 끝나고 다음 스테이지가 시작되므로 몇 Stage인지 출력을 해준다

```kotlin
   if(i == Height-1) {
            printMap(mapIntArray)
        }
```
- 행의 값이 Height-1 이라면 배열의 마지막 행이므로 마지막으로 저장된 스테이지를 출력해주게 된다

```kotlin
 for(j in 0..Width-1){
            if(flag1 && flag2 && saveArray[i][j] != -1 && saveArray[i][j] != 4){
                lineList.add(saveArray[i][j])
            }
        }
 
if(flag1 && flag2){
            mapIntArray.add(lineList)
        }
```
- 현재 행의 시작이 -1 혹은 4가 아니라면 flag1, flag2가 모두 참일 것이고, 맵의 구성요소에는 -1과 4가 포함되어 있지 않으므로 이 점을 활용하여 현재 행에서 맵의 구성요소를 lineList에 차례대로 추가한다
(벽 = 0, 구멍 = 1, 공 = 2, 플레이어 = 3, 공백 = 5 로 구성함)
- 그 다음 flag1, flag2를 통해 현재 행이 맵의 구성요소를 포함하고 있는 행이라고 판단이 되면 mapIntArray에 lineList를 추가해준다
(맵의 한줄을 추가해주는 의미)

<br>

### getWidth
```kotlin
fun getWidth(mapLineList : List<String>): Int{
    var maxWidth = 0
    for(i in 0..mapLineList.size-1){
        var line = mapLineList[i]

        if(line.length > maxWidth)
            maxWidth = line.length
    }

    return maxWidth
}
```
- 문제의 주어진 문자열을 줄 단위로 잘라서 저장한 리스트를 메서드의 인자로 받게된다
- 이 리스트의 구성요소는 각 줄에 해당하는 문자열이다
- 각 문자열의 길이를 비교하여 가장 긴 문자열을 구한다
- 이 길이가 문자열을 저장할 2차원 배열의 가로 길이가 된다

<br>

### saveMap
```kotlin
fun saveMap(mapLineList: List<String>, array: Array<Array<Int>>){
    for(i in 0..mapLineList.size-1){
        var line = mapLineList[i]

        for(j in 0..line.length-1){
            when(line[j]){
                '#' -> array[i][j] = 0
                'O' -> array[i][j] = 1
                'o' -> array[i][j] = 2
                'P' -> array[i][j] = 3
                '=' -> array[i][j] = 4
                ' ' -> array[i][j] = 5
            }
        }
    }
}
```
- 문자열을 2차원 정수형 배열로 변환 후 저장하는 메서드이다
- getWidth 메서드 처럼 줄별로 자른 문자열 리스트와 이를 저장할 정수형 2차원 배열을 인자로 받고 문제의 요구조건에 맞게 저장한다
- 각 문자열에 포함되어 있는 공백은 5의 값을 할당하였다

<br>

### printMap
```kotlin
fun printMap(array: ArrayList<ArrayList<Int>>){
    var maxWidth = 0
    var maxHeight = 0
    var countHole = 0
    var countBall = 0
    var playerY = 0
    var playerX = 0

    for(i in 0..array.size-1){
        maxHeight++
        if(array[i].size > maxWidth){
            maxWidth = array[i].size
        }

        for(j in 0..array[i].size-1){
            when(array[i][j]){
                0 -> print('#')
                1 -> {
                    print('O')
                    countHole++
                }
                2 -> {
                    print('o')
                    countBall++
                }
                3 -> {
                    print('P')
                    playerY = i+1
                    playerX = j+1
                }
                5 -> print(' ')
            }
        }
        println()
    }
    println()

    println("가로크기: ${maxWidth}")
    println("세로크기: ${maxHeight}")
    println("구멍의 수: ${countHole}")
    println("공의 수: ${countBall}")
    println("플레이어 위치 (${playerY}, ${playerX})")
    println()
}
```
- 인자로 받은 array는 main 함수에서 스테이지 별로 생성한 mapIntArray를 전달받은 것이다
- 따라서 array 변수에는 실제 맵을 구성하는 요소들이 담겨져 있다
ex> Stage2
<pre>
 "  #######\n" +
 "###  O  ###\n" +
 "#    o    #\n" +
 "# Oo P oO #\n" +
 "###  o  ###\n" +
 " #   O  # \n" +
 " ########"
 
5 5 0 0 0 0 0 0 0
0 0 0 5 5 1 5 5 0 0 0 
0 5 5 5 5 2 5 5 5 5 0 
0 5 1 2 5 3 5 2 1 5 0 
0 0 0 5 5 2 5 5 0 0 0 
5 0 5 5 5 1 5 5 0 5
5 0 0 0 0 0 0 0 0
</pre>
- Stage2의 입력 문자열과 이 문자열이 변환과 맵의 구성요소를 뽑는 과정을 통해 위와 같은 2차원 정수형 ArrayList로 넘어오게 된다
- 한 행이 하나의 1차원 ArrayList 이며 전체 2차원 ArrayList의 크기가 곧 이 맵의 세로 길이가 된다
- 가로길이는 한 행을 구성하는 1차원 ArrayList중 크기가 큰 것의 값을 가지게 된다
- 위 2차원 리스트를 다시 문자로 변환하여 맵의 모습을 출력한다
- 이때 공,구멍의 개수를 세주고, 플레이어의 위치를 구한다
- 플레이어 위치의 경우 리스트의 인덱스 값이 처음에 할당되므로 1씩 더해줘서 좌측 상단 꼭짓점 기준 얼만큼 떨어져 있는지를 문제와 같이 나타낸다
