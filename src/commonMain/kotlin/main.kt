import ClickMode.*
import korlibs.image.color.*
import korlibs.image.font.*
import korlibs.image.text.*
import korlibs.io.async.*
import korlibs.io.file.std.*
import korlibs.io.lang.*
import korlibs.korge.*
import korlibs.korge.input.*
import korlibs.korge.scene.*
import korlibs.korge.style.*
import korlibs.korge.time.*
import korlibs.korge.ui.*
import korlibs.korge.view.*
import korlibs.korge.view.align.*
import korlibs.time.*
import kotlin.random.*

lateinit var font: Font

object ColorPalette {
    val GREEN = Colors["#00FF00"]
    val GRAY = Colors["#797979"]
    val RED = Colors["#FF0000"]
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
        val background = solidRect(sceneContainer.size , ColorPalette.GRAY).centerOnStage()
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
            val middleText = uiText("준비가 되었으면 화면을 클릭하세요").centerOnStage()
            val rectText = uiText("")
                .alignX(sceneContainer, 0.1, true)
                .alignY(sceneContainer, 0.1, true)
            lateinit var cancellable: Cancellable
            mouse.onDown {
                println("asdf")
                when(clickMode) {
                    IDLE -> {
                        middleText.text = "화면이 녹색으로 바뀌면 화면을 클릭하세요"
                        background.color = Colors.PALEVIOLETRED
                        val period = randomPeriod()
                        startTime = DateTime.now() + period
                        timers.timeout(period) {
                            startTime = DateTime.now()
                            middleText.text = ""
                            background.color = ColorPalette.GREEN
                        }.also { cancellable = it.cancellable() }
                    }
                    PLAY -> {
                        background.color = ColorPalette.GRAY
                        endTime = DateTime.now()
                        if (endTime < startTime) {
                            middleText.text = "화면이 녹색으로 바뀌었을때 클릭하세요. 다시 시작하시려면 화면을 클릭하세요"
                            cancellable.cancel()
                            clickMode = IDLE
                            return@onDown
                        }
                        rectText.text = "반응 시간: ${endTime - startTime}"
                        middleText.text = "준비가 되었으면 화면을 클릭하세요"
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
