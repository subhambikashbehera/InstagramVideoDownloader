package com.subhambikashsubhamgupta.instagramdownloader

import android.content.pm.PackageManager
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File


class HistoryFragment : Fragment() {

    private var listvideos= arrayListOf<HistoryModel>()
    private lateinit var historyAdapter:HistoryAdapter
    private lateinit var recyclerView: RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView=view.findViewById(R.id.recycle)


        if (activity?.let { ContextCompat.checkSelfPermission(it,android.Manifest.permission.READ_EXTERNAL_STORAGE) } != PackageManager.PERMISSION_GRANTED)
        {
            activity?.let { ActivityCompat.requestPermissions(it, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),102) }
            Toast.makeText(context,"Permisssion is needed fo showing the videos",Toast.LENGTH_SHORT).show()
        }
        else
        {
            loadvideo()
        }
        historyAdapter.notifyDataSetChanged()
    }

    private fun loadvideo() {


        val cols= listOf<String>(MediaStore.Video.Media.DATA,MediaStore.Video.Thumbnails.DATA).toTypedArray()
        val selection = MediaStore.Video.Media.DATA + " like?"
        val selectionArgs = arrayOf("%Instagram Downloader%")

       // var rs=context?.contentResolver?.query(uri,cols,null,null,null)

        val rs = context?.contentResolver?.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            cols, selection, selectionArgs, MediaStore.Video.Media.DATE_TAKEN + " DESC");


        if (rs!=null)
        {
            while (rs.moveToNext())
            {
                val url = rs.getString(rs.getColumnIndex(MediaStore.Video.Media.DATA))
                val urlthumb = rs.getString(rs.getColumnIndex(MediaStore.Video.Thumbnails.DATA))

                val uriss= Uri.fromFile(File(url))
                val bMap = ThumbnailUtils.createVideoThumbnail(uriss.toString(),
                    MediaStore.Video.Thumbnails.MICRO_KIND)

                listvideos.add(HistoryModel(uriss,bMap!!))

            }

            historyAdapter= activity?.let { HistoryAdapter(listvideos, it) }!!
            recyclerView.adapter=historyAdapter
            var layout=GridLayoutManager(context,3)
            recyclerView.layoutManager=layout
            historyAdapter.notifyDataSetChanged()
        }

    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode==102 && grantResults[0] != PackageManager.PERMISSION_GRANTED)
        {
            Toast.makeText(context,"Permisssion is needed fo showing the videos",Toast.LENGTH_SHORT).show()
        }else
        {
            Toast.makeText(context,"Permisssion is Granted",Toast.LENGTH_SHORT).show()
        }
    }



}