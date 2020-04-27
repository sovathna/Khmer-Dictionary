package com.sovathna.khmerdictionary.ui.splash

import android.content.Intent
import android.os.Bundle
import com.sovathna.androidmvi.activity.MviActivity
import com.sovathna.khmerdictionary.R
import com.sovathna.khmerdictionary.domain.model.intent.SplashIntent
import com.sovathna.khmerdictionary.domain.model.state.SplashState
import com.sovathna.khmerdictionary.ui.download.DownloadFragment
import com.sovathna.khmerdictionary.ui.main.MainActivity
import com.sovathna.khmerdictionary.util.LogUtil
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_splash.*
import java.util.concurrent.TimeUnit
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

      exists?.getContentIfNotHandled()?.let { exists ->
        if (exists) {
          val intent = Intent(this@SplashActivity, MainActivity::class.java)
          intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
          startActivity(intent)
        } else {
          val fragment = DownloadFragment()
          fragment.show(supportFragmentManager, null)
        }
      }


    }
  }
}