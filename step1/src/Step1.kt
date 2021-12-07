////////////////    풀이용 코드   /////////////

import java.util.*

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
            " #   O  # \n" +
            " ########"

    var changeTarget = target.split("\n")  // 개행 단위로 문자열 자르기

   /* for(i in 0..changeTarget.size-1){
        var line = changeTarget[i]
        println(line)
    }*/

    var Height = changeTarget.size
    var Width = getWidth(changeTarget)

   /* println("세로길이 : ${Height}, 가로길이 : ${Width}")
    println()*/

    // 변환 저장할 2차원 정수형 배열
    Height += 1   // 마지막 맵 출력을 위해 -1이 할당된 한 줄 추가  // line78 처럼 표현하기 위함
    var saveArray = Array(Height, {Array(Width, {-1})})

   /* for(i in 0..Height-1){
        for(j in 0..Width-1){
            print(saveArray[i][j])
        }
        println()
    }*/

    saveMap(changeTarget, saveArray)  // 변환 후 2차원 배열에 저장

    // 변환 결과 출력
   for(i in 0..Height-1){
        for(j in 0..Width-1){
            print(saveArray[i][j])
            print(" ")
        }
        println()
    }

    var stageNum = 1

    /*var mapIntArray = ArrayList<ArrayList<Int>>()
    var testList = ArrayList<Int>()
    testList.add(1)
    mapIntArray.add(testList)

    println(mapIntArray[0][0])*/

    var mapIntArray = ArrayList<ArrayList<Int>>()  // 맵별 정수형 배열

  for(i in 0..Height-1){

       var lineList = ArrayList<Int>()

      var flag1 = true
      if(saveArray[i][0] == -1){
          flag1 = false
      }

      var flag2 = true
      if(saveArray[i][0] == 4){
          flag2 = false
      }

       if(i == 0) {
           println("Stage " + stageNum++)
           println()
       }

       if(saveArray[i][0] == 4) {
           printMap(mapIntArray)
           mapIntArray.clear()

           println("Stage " + stageNum++)
           println()
       }

       if(i == Height-1) {
           printMap(mapIntArray)
       }

        for(j in 0..Width-1){

            //println("yes, ${saveArray[i][j]}")

            if(flag1 && flag2 && saveArray[i][j] != -1 && saveArray[i][j] != 4){
                //println("wow")
                lineList.add(saveArray[i][j])
            }

        }


      // 행의 시작이 -1, 4 가 아닌 경우에만!!
      if(flag1 && flag2){
         /* println("lineList test")
          for(k in 0..lineList.size-1){
              print(lineList[k])
          }
          println()
*/
          mapIntArray.add(lineList)
      }


    }

}

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

// 최대 가로길이 구하기
fun getWidth(mapLineList : List<String>): Int{
    var maxWidth = 0
    for(i in 0..mapLineList.size-1){
        var line = mapLineList[i]

        if(line.length > maxWidth)
            maxWidth = line.length
    }

    return maxWidth
}


// map 출력
fun printMap(array: ArrayList<ArrayList<Int>>){
   /* for(i in 0..array.size-1){
        for(j in 0..array[i].size-1){
            print(array[i][j])
        }
        println()
    }

    println()*/

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



// 가로길이 = 한 줄의 길이의 최대값
// 세로길이 = 첫번째 줄 제외하고 #다음에 공백이 절대 없는 경우





