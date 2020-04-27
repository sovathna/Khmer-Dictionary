package com.sovathna.khmerdictionary.ui.download

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import com.sovathna.androidmvi.fragment.MviDialogFragment
import com.sovathna.khmerdictionary.R
import com.sovathna.khmerdictionary.domain.model.intent.DownloadIntent
import com.sovathna.khmerdictionary.domain.model.state.DownloadState
import com.sovathna.khmerdictionary.ui.main.MainActivity
import com.sovathna.khmerdictionary.util.LogUtil
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_download.*
import javax.inject.Inject

class DownloadFragment : MviDialogFragment<DownloadIntent, DownloadState, DownloadViewModel>(
  R.layout.fragment_download
) {

  @Inject
  lateinit var downloadIntent: PublishSubject<DownloadIntent.Download>

  override fun onStart() {
    super.onStart()
    dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
    dialog?.window?.setLayout(
      ViewGroup.LayoutParams.MATCH_PARENT,
      ViewGroup.LayoutParams.WRAP_CONTENT
    )
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    isCancelable = false
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    btn_cancel.setOnClickListener {
      dismiss()
    }
  }

  override fun intents(): Observable<DownloadIntent> =
    downloadIntent.cast(DownloadIntent::class.java)

  override fun render(state: DownloadState) {
    LogUtil.i("state: $state")
    with(state) {
      if (isInit) downloadIntent.onNext(DownloadIntent.Download)

      pb.isIndeterminate = download == total

      if (download == total) {
        if (total == 0L) {
          tv_title.text = "កំពុងរៀបចំទាញយកទិន្នន័យ"
        } else {
          tv_title.text = "កំពុងរក្សាទុកទិន្នន័យ"
        }
        tv_sub_title.text = "កំពុងដំណើរការ..."
      } else {
        tv_title.text = "កំពុងទាញយកទិន្នន័យ"
        tv_sub_title.text =
          String.format(
            "%s នៃ %s",
            getFileSize(download),
            getFileSize(total)
          )
      }

      pb.max = total.toInt()
      pb.progress = download.toInt()


      successEvent?.getContentIfNotHandled()?.let {
        dismiss()
        val intent = Intent(requireActivity(), MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
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