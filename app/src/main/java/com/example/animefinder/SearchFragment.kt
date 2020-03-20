package com.example.animefinder

import android.Manifest
import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.widget.Button
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.checkSelfPermission
import android.content.ContentResolver
import android.graphics.Bitmap
import android.provider.MediaStore
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import android.util.Base64
import android.util.JsonReader
import android.util.Log
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.android.volley.*
import com.android.volley.Response
import java.io.StringReader


class SearchFragment : Fragment() {
    private lateinit var imageView: ImageView
    private lateinit var resButton: Button
    private lateinit var layout: ConstraintLayout
    private lateinit var imgUri: Uri
    private lateinit var contentResolver: ContentResolver
    private var imageData: ByteArray? = null
    private var animeList: String = ""
    private val url = "https://trace.moe/api/search"
    private lateinit var bitmap: Bitmap

    companion object {
        fun newInstance(): SearchFragment = SearchFragment()
        //image pick code
        private val IMAGE_PICK_CODE = 1000
        //Permission code
        private val PERMISSION_CODE = 1001
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)
        contentResolver = container!!.context.contentResolver
        val view = inflater.inflate(R.layout.fragment_search, container, false)
        imageView = view.findViewById(R.id.selected_img)
        resButton = view.findViewById(R.id.button_see_results)
        layout = view.findViewById(R.id.search_container)

        imageView.setOnClickListener {
            //check runtime permission
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if (checkSelfPermission(view.context, Manifest.permission.READ_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_DENIED){
                    //permission denied
                    val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE);
                    //show popup to request runtime permission
                    requestPermissions(permissions, PERMISSION_CODE)
                }
                else{
                    //permission already granted
                    pickImageFromGallery()
                }
            }
            else{
                //system OS is < Marshmallow
                pickImageFromGallery()
            }
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Sending data from one fragment to another fragment
        resButton.setOnClickListener {
            if(animeList != "") {

                val action = SearchFragmentDirections.actionSearchFragmentToAnimeListFragment()
                //action.setAnimeList(animeList)
                findNavController().navigate(action)
            }
            else {
                Toast.makeText(view.context, "Error sending request to a server", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    private fun getPostData(): JSONObject {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val byteArrayImage = baos.toByteArray()
        val encodedImage = Base64.encodeToString(byteArrayImage, Base64.DEFAULT)

        val json = JSONObject()
        json.put("image", encodedImage)
        return json
    }

    private fun uploadImage() {
        if (imgUri != null) run {
            val postData: JSONObject = getPostData()

            val request = JsonObjectRequest(Request.Method.POST, url, postData,
                Response.Listener { response ->
                    val docs = response.get("docs").toString()
                    Log.d("ANIMELISTRECEIVED", docs)
                    resButton.visibility = View.VISIBLE
                    animeList = docs
                }, Response.ErrorListener {
                    // Error in request
                    val text = "Volley error: $it"
                    Toast.makeText(context, text, Toast.LENGTH_LONG).show()
                })

            request.retryPolicy = object : RetryPolicy {
                override fun getCurrentTimeout(): Int {
                    return 10000
                }

                override fun getCurrentRetryCount(): Int {
                    return 10000
                }

                @Throws(VolleyError::class)
                override fun retry(error: VolleyError) {

                }
            }

            VolleySingleton.getInstance(context!!).addToRequestQueue(request)

        }
    }

    //handle requested permission result
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    pickImageFromGallery()
                }
                else{
                    Toast.makeText(this.context, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    //handle result of picked image
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            imgUri = data?.data!!
            bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imgUri)
            imageView.setImageURI(imgUri)
            imageView.setPadding(0,0,0,0)
            imageView.setBackgroundResource(0)
            uploadImage()
        }
    }
}