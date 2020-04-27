package com.sovathna.khmerdictionary.ui.splash

import android.view.View
import com.sovathna.androidmvi.activity.MviActivity
import com.sovathna.khmerdictionary.R
import com.sovathna.khmerdictionary.domain.model.intent.SplashIntent
import com.sovathna.khmerdictionary.domain.model.state.SplashState
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_splash.*
import kotlinx.android.synthetic.main.layout_download.view.*
import javax.inject.Inject

class SplashActivity : MviActivity<SplashIntent, SplashState, SplashViewModel>(
  R.layout.activity_splash
) {

  @Inject
  lateinit var checkDatabase: PublishSubject<SplashIntent.CheckDatabase>


  override fun intents(): Observable<SplashIntent> =
    checkDatabase.cast(SplashIntent::class.java)

  override fun render(state: SplashState) {
    with(state) {
      if (isInit) checkDatabase.onNext(SplashIntent.CheckDatabase)

//      exists?.getContentIfNotHandled()?.let { exists ->
//        if (exists) {
//          val intent = Intent(this@SplashActivity, MainActivity::class.java)
//          intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
//          startActivity(intent)
//        } else {
//          val fragment = DownloadFragment()
//          fragment.show(supportFragmentManager, null)
//        }
//      }

      pb.visibility =
        when {
          isProgress -> View.VISIBLE
          else -> View.GONE
        }

      if (downloaded != null && total != null) {
        val view = vs_download.inflate()
          view.pb.isIndeterminate = downloaded == total

          if (downloaded == total) {
            if (total == 0L) {
              view.tv_title.text = "រៀបចំទាញយកទិន្នន័យ"
              view.tv_sub_title.text = "កំពុងដំណើរការ..."
            } else {
              view.tv_title.text = "ពន្លា និងរក្សាទុកទិន្នន័យ"
              view.tv_sub_title.text = String.format(
                "ទំហំ %s",
                getFileSize(total)
              )
            }
          } else {
            view.tv_title.text = "ទាញយកទិន្នន័យ"
            view.tv_sub_title.text =
              String.format(
                "ទំហំ %s នៃ %s",
                getFileSize(downloaded),
                getFileSize(total)
              )
          }

          view.pb.max = total.toInt()
          view.pb.progress = downloaded.toInt()

      }


    }
  }

  private fun getFileSize(value: Long): String {
    return when {
      value >= 1_000_000 -> String.format("%.2f MB", value / 1_000_000f)
      value >= 1_000 -> String.format("%.2f KB", value / 1_000f)
      else -> String.format("%d %s", value, if (value == 1L) "Byte" else "Bytes")

    }
  }
}