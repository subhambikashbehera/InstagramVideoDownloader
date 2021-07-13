package com.subhambikashsubhamgupta.instagramdownloader

import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class MainActivity : AppCompatActivity() {

    var is_permission=false
    lateinit var downloadFragment: DownloadFragment
    var version:String=""
    lateinit var viewPager: ViewPager2
    lateinit var tablayout: TabLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar=findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        viewPager = findViewById(R.id.viewpager1)
        tablayout = findViewById(R.id.tablayout)
        viewPager.adapter = PageAdapters(supportFragmentManager, lifecycle)
        downloadFragment = DownloadFragment()

        TabLayoutMediator(tablayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "Download"
                1 -> tab.text = "History"
            }

        }.attach()


        

        val uri = intent.data
        Log.e("uri", uri.toString())
        if (uri != null) {
            val path = uri.pathSegments
            println(path)
        }

        
        checkpermisson()



      val databaseReference=FirebaseDatabase.getInstance().reference.child("reeldownloader")

        val listner = databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
               version= snapshot.child("version").getValue().toString()

                val manager: PackageManager = getPackageManager()
                val info: PackageInfo =
                    manager.getPackageInfo(getPackageName(), PackageManager.GET_ACTIVITIES)

                if (info.versionCode < Integer.parseInt(version))
                {
                    val popup = MaterialAlertDialogBuilder(this@MainActivity)
                    var dialog: AlertDialog? = null
                    val view =layoutInflater?.inflate( R.layout.popup2,null)
                    val title : TextView? = view?.findViewById(R.id.title)
                    val message : TextView? = view?.findViewById(R.id.message)
                    val sharebtn : TextView? = view?.findViewById(R.id.sharebtn)
                    val cancelbtn : TextView? = view?.findViewById(R.id.closebtn)
                    title?.setText("Horray! New Update")
                    message?.setText("New Update is Live on Playstore go and Update It for our latest features and app Optimization")
                    sharebtn?.setText("OK")
                    sharebtn?.setOnClickListener{
                        dialog?.dismiss()
                        dialog?.cancel()
                        try {
                            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName")))
                        } catch (e: Exception) {
                            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$packageName")))
                        }
                    }
                    cancelbtn?.setOnClickListener {
                        dialog?.dismiss()
                        dialog?.cancel()
                    }
                    popup?.setBackground(ColorDrawable(Color.TRANSPARENT))
                    popup?.setCancelable(false)
                    popup?.setView(view).create()
                    dialog = popup?.show()
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }





    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId)
        {
            R.id.shareapp->
            {
                try {

                    val intent1 = Intent(Intent.ACTION_SEND)
                    intent1.type = "text/plain"
                    intent1.putExtra(Intent.EXTRA_SUBJECT, "REEL DOWNLOADER")
                     val shareMessage="https://play.google.com/store/apps/details?id="+BuildConfig.APPLICATION_ID+"\n\n";
                    intent1.putExtra(Intent.EXTRA_TEXT, shareMessage)
                    startActivity(Intent.createChooser(intent1, "share by"))

                } catch (e: Exception) {
                    Toast.makeText(this@MainActivity, "error occured", Toast.LENGTH_SHORT).show()
                }
            }
            R.id.privecypolicies->{
               val intent=Intent(this,Privacypolicies::class.java)
                startActivity(intent)
            }
        }

        return true

    }


    fun checkpermisson(){

        if (ActivityCompat.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE)  != PackageManager.PERMISSION_GRANTED)
        {
                val popup = MaterialAlertDialogBuilder(this)
                var dialog: AlertDialog? = null
                val view =layoutInflater?.inflate( R.layout.popup2,null)
                val title : TextView? = view?.findViewById(R.id.title)
                val message : TextView? = view?.findViewById(R.id.message)
                val sharebtn : TextView? = view?.findViewById(R.id.sharebtn)
                val cancelbtn : TextView? = view?.findViewById(R.id.closebtn)
                title?.setText("About Permission")
                message?.setText("We are taking The Permission For to show the history that you will download.To run the app fluently just allow the permisiion")
                sharebtn?.setText("OK")
                sharebtn?.setOnClickListener{
                    dialog?.dismiss()
                    ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),103)
                }
                cancelbtn?.setOnClickListener {
                    dialog?.dismiss()
                }
                popup?.setBackground(ColorDrawable(Color.TRANSPARENT))
                popup?.setCancelable(false)
                popup?.setView(view).create()
                dialog = popup?.show()
        }

    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        if (requestCode==103 && grantResults[0] != PackageManager.PERMISSION_GRANTED)
        {
            Toast.makeText(this,"Permisssion is needed for Downloading and showing the videos",Toast.LENGTH_LONG).show()
        }else
        {
            Toast.makeText(this,"Welcome",Toast.LENGTH_SHORT).show()
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }



}