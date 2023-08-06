import korlibs.time.*
import korlibs.korge.*
import korlibs.korge.scene.*
import korlibs.korge.tween.*
import korlibs.korge.view.*
import korlibs.korge.style.*
import korlibs.korge.ui.*
import korlibs.image.color.*
import korlibs.image.font.*
import korlibs.image.format.*
import korlibs.image.text.*
import korlibs.io.async.*
import korlibs.io.file.std.*
import korlibs.korge.input.*
import korlibs.korge.time.*
import korlibs.math.geom.*
import korlibs.math.interpolation.*
import kotlin.random.*

suspend fun main() = Korge(
    backgroundColor = Colors["#000000"],
        scaleMode = ScaleMode.NO_SCALE) {
	val sceneContainer = sceneContainer()

	sceneContainer.changeTo { MyScene() }
}

data class ClickState(
    val count: Int,
    val startTime: TimeSpan,
    val period: TimeSpan,
)


//ㅠㅁㅊ
class MyScene : Scene() {
    override suspend fun SContainer.sceneMain() {
        val background = solidRect(root.size.width, root.size.height, Colors["#808080"])
        val middleText = uiText("Click if you are ready") {
            styles {
                width = root.size.width
                height = root.size.height
                textAlignment = TextAlignment.MIDDLE_CENTER
                textColor = Colors.WHITE
                textFont = resourcesVfs["fonts/NanumSquareNeoTTF-dEb.woff"].readWoffFont()
                textSize = 32.0F
            }
        }
        val rectText = uiText("") {
            styles {
                width = root.size.width / 10
                height = root.size.height / 10
                textAlignment = TextAlignment.TOP_LEFT
                textColor = Colors.WHITE
                textFont = resourcesVfs["fonts/NanumSquareNeoTTF-dEb.woff"].readWoffFont()
                textSize = 32.0F
            }
        }
        onClick {
            if (change == 0) {
                background.color = Colors["#FF0000"]
                middleText.text = "Click when the screen turns green"
                change = 1
                val cancellable = timers.timeout((2..8).random().seconds) {
                    background.color = Colors["#00FF00"]
                    startTime = DateTime.now()
                    middleText.text = ""
                    change = 2
                }
            }else if (change == 1) {
                background.color = Colors["#797979"]
                middleText.text = "Please Click when the screen turns green. Click if you are ready"
                change = 0
            }
            else if (change == 2) {
                endTime = DateTime.now()
                reactTime = (endTime - startTime).milliseconds.toInt()
                background.color = Colors["#808080"]
                middleText.text = "Click if you are ready"
                rectText.text = "reaction time: ${reactTime}ms"
                change = 0
            }
        }
    }
}

