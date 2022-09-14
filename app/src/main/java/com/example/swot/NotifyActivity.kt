package com.example.swot

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.swot.databinding.ActivityNotifyBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.gun0912.tedpermission.PermissionListener
import kotlinx.android.synthetic.main.activity_notify.*
import java.io.File
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*


class NotifyActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    val db = Firebase.firestore
    val PERM_CAMEARA = 10
    val PERM_STORAGE = 9

    val REQ_CAMERA = 11
    val REQ_GALLERY = 12
    lateinit var curPhotoPath: String   //문자열 형태의 사진 경로 값
    lateinit var binding: ActivityNotifyBinding

    data class notify(
        var notify_name: String? = null,
        var notify_why: String? = null,
        var userid: String? = null
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotifyBinding.inflate(layoutInflater)
        auth = FirebaseAuth.getInstance()

        setContentView(R.layout.activity_notify)
        setContentView(binding.root)
        setSupportActionBar(toolbar5)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)  // 왼쪽 버튼 사용 여부 true
        supportActionBar?.setDisplayShowTitleEnabled(false)

        setPermission() // 권한을 체크하는 기능을 수행

        binding.button10.setOnClickListener {
            openGallery()
        }

    }

    private fun createImageFile(): File {
        val user = auth.currentUser
        val timestamp: String =
            SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())  //사진이름을 설정할 때 날짜로 설정하게 만들 String
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)     //임시저장공간
        //  return File.createTempFile("JPEG_${timestamp}",".jpg",storageDir)

        return File.createTempFile(user?.uid, ".jpg", storageDir)
            .apply { curPhotoPath = absolutePath } //사진 경로 절대경로로 만듬
    }

    private fun setPermission() {
        val permission = object : PermissionListener {  //singleTon이라 사용하는 것으로 판단
            override fun onPermissionGranted() {    //설정해놓은 위험권한들이 허용 되었을 경우 이곳을 수행.
                Toast.makeText(this@NotifyActivity, "권한이 허용 되었습니다.", Toast.LENGTH_SHORT).show()
            }

            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {  //설정해놓은 위험권한 둘 중 거부를 한 경우 이곳을 수행
                Toast.makeText(this@NotifyActivity, "권한이 거부 되었습니다.", Toast.LENGTH_SHORT).show()
            }
        }

    }

    var realUri: Uri? = null

    fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = MediaStore.Images.Media.CONTENT_TYPE
        startActivityForResult(intent, REQ_GALLERY)
    }

    fun createImageUri(filename: String, mimeType: String): Uri? {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.DISPLAY_NAME, filename)
        values.put(MediaStore.Images.Media.MIME_TYPE, filename)

        return contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
    }

    fun newFilename(): String {
        val user = auth.currentUser
        val sdf = SimpleDateFormat("yyyyMMdd HHmmss")
        val filename = sdf.format(System.currentTimeMillis())
        // return "$(filename).jpg"
        return "$(user).jpg"
    }

    fun loadBitmap(photoUri: Uri): Bitmap? {
        try {
            return if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O_MR1) {
                val source = ImageDecoder.createSource(contentResolver, photoUri)
                ImageDecoder.decodeBitmap(source)
            } else {
                MediaStore.Images.Media.getBitmap(contentResolver, photoUri)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun permissionGranted() {
        Toast.makeText(applicationContext, "권한이 허용됨", Toast.LENGTH_SHORT).show()
    }

    fun permissionDenied(deniedPermissions: ArrayList<String?>?) {
        Toast.makeText(applicationContext, "권한이 거부됨", Toast.LENGTH_SHORT).show()
    }

    //Firebase Storage에 이미지를 업로드 하는 함수.
    fun uploadImageTOFirebase(uri: Uri) {
        val user = auth.currentUser
        var storage: FirebaseStorage? = FirebaseStorage.getInstance()   //FirebaseStorage 인스턴스 생성
        //파일 이름 생성.r
        //  var fileName = "IMAGE_${SimpleDateFormat("yyyymmdd_HHmmss").format(Date())}_.png"
        var fileName = "${user?.uid}.jpg"
        //파일 업로드, 다운로드, 삭제, 메타데이터 가져오기 또는 업데이트를 하기 위해 참조를 생성.
        //참조는 클라우드 파일을 가리키는 포인터라고 할 수 있음.
        var imagesRef =
            storage!!.reference.child("images/").child(fileName)    //기본 참조 위치/images/${fileName}
        //이미지 파일 업로드
        imagesRef.putFile(uri!!).addOnSuccessListener {
            //  Toast.makeText(activity, getString(R.string.upload_success), Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            println(it)
            // Toast.makeText(activity, getString(R.string.upload_fail), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                REQ_CAMERA -> {
                    val bitmap = data?.extras?.get("data") as Bitmap
                    binding.imageView3.setImageBitmap(bitmap)
                    realUri?.let { uri ->
                        val bitmap = loadBitmap(uri)
                        binding.imageView3.setImageBitmap(bitmap)
                        realUri = null
                    }
                }
                REQ_GALLERY -> {
                    data?.data?.let { uri ->
                        binding.imageView3.setImageURI(uri)
                        binding.button.setOnClickListener {
                            uploadImageTOFirebase(uri)
                            var notify = notify()
                            val user = auth.currentUser
                            notify.notify_name = binding.editTextTextPersonName2.text.toString().trim()
                            notify.notify_why = binding.textView10.text.toString().trim()
                            notify.userid = user?.uid
                            val db = Firebase.firestore
                            val doclist = arrayListOf<String>()
                            val random = Random()
                            var number = random.nextInt(10) + 1
                            // 랜덤숫자로..?
                            db.collection("NOTIFY")
                                .get()
                                .addOnSuccessListener { result ->
                                    for (i in result) {
                                        doclist.add(i.id)
                                    }
                                    while (true) {
                                        if (doclist.contains("OFFICIAL$number")) {
                                            number = random.nextInt(10) + 1
                                            Log.d("Add", "동일 숫자$number")
                                        } else {
                                            db.collection("NOTIFY").document("notify$number")
                                                .set(notify)
                                            Toast.makeText(this,"신고 접수가 완료되었습니다",Toast.LENGTH_SHORT).show()
                                            val intent = Intent(this, MainActivity::class.java)
                                            startActivity(intent)
                                            break
                                        }
                                    }
                                }
                        }
                    }

                }
            }

        }

    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.onlyback,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        when(id){
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

}