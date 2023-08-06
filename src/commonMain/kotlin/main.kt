import ClickMode.*
import korlibs.datastructure.*
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
import korlibs.io.lang.*
import korlibs.korge.input.*
import korlibs.korge.time.*
import korlibs.korge.view.align.*
import korlibs.math.geom.*
import korlibs.math.interpolation.*
import kotlinx.coroutines.*
import org.koin.core.context.*
import org.koin.dsl.*
import kotlin.coroutines.*
import kotlin.random.*

lateinit var font: Font

object ColorPalette {
    val GREEN = Colors["#00FF00"]
    val GRAY = Colors["#797979"]
}

enum class ClickMode {
    IDLE, PLAY, STOP
}

suspend fun main() = Korge(backgroundColor = Colors["#000000"]) {
    val sceneContainer = sceneContainer()
    font = resourcesVfs["fonts/NanumSquareNeoTTF-dEb.woff"].readWoffFont()
    sceneContainer.changeTo { MyScene() }
}


class MyScene : Scene() {

    override suspend fun SContainer.sceneMain() {
        val background = solidRect(root.size.width, root.size.height, ColorPalette.GRAY)
        var startTime = DateTime.now()
        var endTime = DateTime.now()
        var clickMode = IDLE
        uiContainer(size) {
            styles {
                textSize = 32.0F
                textColor = Colors.WHITE
                textFont = font
                textAlignment = TextAlignment.MIDDLE_CENTER
            }
            val middleText = uiText("준비가 되었으면 클릭합니다").centerOnStage()
            val rectText = uiText("")
                .alignX(root, 0.1, true)
                .alignY(root, 0.1, true)
            mouse.onDown {
                println("asdf")
                when(clickMode) {
                    IDLE -> {
                        middleText.text = "화면이 녹색으로 바뀌면 클릭하세요"
                        val period = randomPeriod()
                        startTime = DateTime.now() + period
                        timers.timeout(period) {
                            startTime = DateTime.now()
                            middleText.text = ""
                            background.color = ColorPalette.GREEN
                        }
                    }
                    PLAY -> {
                        background.color = ColorPalette.GRAY
                        endTime = DateTime.now()
                        if (endTime < startTime) {
                            //todo 늦게 누름
                            return@onDown
                        }
                        rectText.text = "반응 시간: ${endTime - startTime}"
                    }
                    STOP -> {
                        launchImmediately { sceneContainer.changeTo<MyScene>() }
                    }
                }
                clickMode = ClickMode.values()[clickMode.ordinal+1]

            }
        }
    }
}


fun randomPeriod() = Random.nextDouble(1.0, 2.0).seconds
