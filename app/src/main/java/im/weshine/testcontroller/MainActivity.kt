package im.weshine.testcontroller

import android.content.Context
import android.graphics.Point
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import kotlinx.android.synthetic.main.activity_main.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket


class MainActivity : AppCompatActivity() {
    companion object {
        const val TAG = "main_controller"
    }

    private lateinit var softKeyboardStateWatcher: SoftKeyboardStateWatcher

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Handler().postDelayed({
            et.requestFocus()
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(et, InputMethodManager.SHOW_FORCED)
        }, 500)

        Handler().postDelayed({
            Log.i(TAG, "screen width:${resources.displayMetrics.widthPixels}\n" +
                    "screen height:${resources.displayMetrics.heightPixels}\n" +
                    "real height:${getRealHeight()}\n" +
                    "decor_view_height:${window.decorView.height}\n" +
                    "rl_height:${rl.height}\n" +
                    "rl_bottom_screen:${{ val arr = IntArray(2);rl.getLocationOnScreen(arr);arr[1] + rl.height }()}")

            val arr = IntArray(2)
            rl.getLocationOnScreen(arr)
            val imeTop = arr[1] + rl.height
            tv_info.text = imeTop.toString()
        }, 1000)

        softKeyboardStateWatcher = SoftKeyboardStateWatcher(findViewById<View>(R.id.rl))
        softKeyboardStateWatcher.addSoftKeyboardStateListener(object : SoftKeyboardStateWatcher.SoftKeyboardStateListener {
            override fun onSoftKeyboardOpened(keyboardHeightInPx: Int) {
                tv_signal.text = "show"
            }

            override fun onSoftKeyboardClosed() {
                tv_signal.text = "hide"
                Log.i(packageName, "keyboard hide")

            }
        })
    }

    private fun getRealHeight(): Int {
        return try {
            val size = Point()
            windowManager.defaultDisplay.getRealSize(size)
            size.y
        } catch (e: NoSuchMethodError) {
            windowManager.defaultDisplay.height
        }
    }
}

//Thread {
//    //客户端
//    //1、创建客户端Socket，指定服务器地址和端口
//    val socket = Socket("192.168.31.72", 10086)
//    //2、获取输出流，向服务器端发送信息
//    val os = socket.getOutputStream() //字节输出流
//    val pw = PrintWriter(os)//将输出流包装成打印流
//    pw.write("over")
//    pw.flush()
//    socket.shutdownOutput()
//    //3、获取输入流，并读取服务器端的响应信息
//    val `is` = socket.getInputStream()
//    val br = BufferedReader(InputStreamReader(`is`))
//    var info: String? = null
//    while ({ info = br.readLine(); info } != null) {
//        System.out.println("我是客户端，服务器说：" + info)
//    }
//
//    //4、关闭资源
//    br.close()
//    `is`.close()
//    pw.close()
//    os.close()
//    socket.close()
//}.start()
