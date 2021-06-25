package com.subhambikashsubhamgupta.instagramdownloader

import android.graphics.Bitmap
import android.net.Uri
import java.io.File

data class HistoryModel (
    var url: Uri,
    var urlthum:Bitmap,
    var file:File
    )