package com.softwador.whatsapp.messaging.ui.main

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.app.NotificationManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.text.trimmedLength
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.textfield.TextInputEditText
import com.softwador.whatsapp.messaging.R
import com.softwador.whatsapp.messaging.utils.ImageUtils
import com.softwador.whatsapp.messaging.utils.MessagingUtils
import com.softwador.whatsapp.messaging.utils.PermissionUtils
import java.io.File


/**
 * A placeholder fragment containing a simple view.
 */
class MainFragment : Fragment() {

    private lateinit var mainPageViewModel: MainPageViewModel

    var selectedImagePath = ""
    var url = "https://api.whatsapp.com/send/"
    var urlPhonePrefix = "?phone="
    var urlMessageTextPrefix = "&text="
    var urlSuffix = "&app_absent=0"
    var numberFromNotification = ""
    var sendBusinessCardFromNotification = false;
    var notificationId = -1

    //views and boxes
    lateinit var phoneNumberView: TextInputEditText
    lateinit var notImportantTextView: TextView
    lateinit var addImageBox: ImageView
    lateinit var removeImageBox: ImageView
    lateinit var messageTextBox: TextInputEditText
    lateinit var customerNameTextBox: TextInputEditText
    lateinit var jobDescriptionTextBox: TextInputEditText
    lateinit var jobPriceTextBox: TextInputEditText
    lateinit var sendMessageButton: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainPageViewModel = ViewModelProviders.of(this).get(MainPageViewModel::class.java).apply {
            setIndex(arguments?.getInt(ARG_SECTION_NUMBER) ?: 1)
        }
        val numberFromNotificationString =
            activity?.intent?.extras?.getString("numberFromNotification")
        var sendBusinessCardFromNotificationBool: Boolean? =
            activity?.intent?.extras?.getBoolean("sendBusinessCardFromNotification")
        val notificationIdInt: Int? =
            activity?.intent?.extras?.getInt("notificationId")

        println("numberFromNotificationString is - " + numberFromNotificationString)
        println("sendBusinessCardFromNotification is - " + sendBusinessCardFromNotification)
        if (null != sendBusinessCardFromNotificationBool) {
            sendBusinessCardFromNotification = sendBusinessCardFromNotificationBool
        }
        if (null != numberFromNotificationString) {
            numberFromNotification = numberFromNotificationString
        }
        if (null != notificationIdInt) {
            notificationId = notificationIdInt
        }

        val notificationManager =
            activity!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        //notifications add 2 notifications ids, one for each action, so we make sure we close them both
        notificationManager.cancel(notificationId)
        notificationManager.cancel(notificationId + 1)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(
            com.softwador.whatsapp.messaging.R.layout.fragment_main,
            container,
            false
        )

        //set views
        phoneNumberView = root.findViewById<TextInputEditText>(R.id.phoneNumber)
        notImportantTextView =
            root.findViewById(com.softwador.whatsapp.messaging.R.id.section_label)
        addImageBox = root.findViewById<ImageView>(R.id.addImageBox)
        removeImageBox = root.findViewById<ImageView>(R.id.removeImageBox)
        messageTextBox = root.findViewById<TextInputEditText>(R.id.messageText)
        customerNameTextBox = root.findViewById<TextInputEditText>(R.id.customerName)
        jobDescriptionTextBox = root.findViewById<TextInputEditText>(R.id.jobDescription)
        jobPriceTextBox = root.findViewById<TextInputEditText>(R.id.jobPrice)
        sendMessageButton = root.findViewById<Button>(R.id.sendMessageButton)
        //////////////////////////////////////

//        mainPageViewModel.text.observe(this, Observer<String> {
//            notImportantTextView.text = it
//        })

        phoneNumberView.setText(numberFromNotification)

        //send business card from notification
        if (sendBusinessCardFromNotification) {
            messageTextBox.setText(MessagingUtils.BUSINESS_CARD_MESSAGE)
            sendMessage(root)
        }
        addImageBox.setOnClickListener {
            selectImage(root.context)
        }

        removeImageBox.setImageResource(android.R.color.transparent)
        removeImageBox.setOnClickListener {
            selectedImagePath = ""
            addImageBox.setImageBitmap(null)
            addImageBox.setImageResource(android.R.drawable.ic_input_add)
            removeImageBox.setImageResource(android.R.color.transparent)
        }


        messageTextBox.imeOptions = EditorInfo.IME_ACTION_SEND
        messageTextBox.setRawInputType(InputType.TYPE_CLASS_TEXT)
        messageTextBox.setOnEditorActionListener { v, actionId, event ->
            return@setOnEditorActionListener when (actionId) {
                EditorInfo.IME_ACTION_SEND -> {
                    sendMessage(root)
                    true
                }
                else -> false
            }
        }


        jobPriceTextBox.imeOptions = EditorInfo.IME_ACTION_SEND
        jobPriceTextBox.setRawInputType(InputType.TYPE_CLASS_TEXT)
        jobPriceTextBox.setOnEditorActionListener { v, actionId, event ->
            return@setOnEditorActionListener when (actionId) {
                EditorInfo.IME_ACTION_SEND -> {
                    sendMessage(root)
                    true
                }
                else -> false
            }
        }

        sendMessageButton.setOnClickListener {
            println("url is - " + url)
            sendMessage(root)
        }
        return root
    }

    fun sendMessage(view: View) {
        var maxTrimLength = 1
        println("sendMessageButton pressed")
        println("phoneNumber is - " + phoneNumberView.text)
        var fixedPhoneNumber = "972+" + phoneNumberView.text?.drop(1)
        var textForMessage = ""
        //check if usign template
        if (customerNameTextBox.text!!.trimmedLength() > maxTrimLength || customerNameTextBox.text!!.trimmedLength() > maxTrimLength || customerNameTextBox.text!!.trimmedLength() > maxTrimLength) {
            //assume template is wanted
            textForMessage = MessagingUtils.buildMessage(
                customerNameTextBox.text.toString(),
                jobDescriptionTextBox.text.toString(),
                jobPriceTextBox.text.toString()
            )
        } else {
            textForMessage = messageTextBox.text.toString()
        }

        println("text is - " + textForMessage)
        val finalUrl =
            url + urlPhonePrefix + fixedPhoneNumber + urlMessageTextPrefix + textForMessage + urlSuffix
        println("redirecting to url - " + finalUrl)

        if ("" == selectedImagePath) {
            //send message no file to particular phone
            try {
                val i = Intent(Intent.ACTION_VIEW)
                i.setPackage("com.whatsapp")
                i.type = "text/plain"
                i.data = Uri.parse(finalUrl)
                startActivity(i)
            } catch (e: PackageManager.NameNotFoundException) {
                Toast.makeText(
                    view.context,
                    "Whatsapp app not installed in your phone",
                    Toast.LENGTH_SHORT
                ).show()
                e.printStackTrace()
            }
        } else {
            //send message with image but must choose contact
            try {
                val i = Intent(Intent.ACTION_SEND, Uri.parse(finalUrl))
                i.setPackage("com.whatsapp")
                i.putExtra(
                    Intent.EXTRA_PHONE_NUMBER,
                    fixedPhoneNumber
                )
                i.putExtra(Intent.EXTRA_TEXT, messageTextBox.text.toString())
                i.putExtra(Intent.EXTRA_STREAM, Uri.parse(selectedImagePath))
                i.type = "image/jpeg"
                i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                //                i.data = Uri.parse(finalUrl)
                startActivity(i)
            } catch (e: PackageManager.NameNotFoundException) {
                Toast.makeText(
                    view.context,
                    "Whatsapp app not installed in your phone",
                    Toast.LENGTH_SHORT
                ).show()
                e.printStackTrace()
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            println("add image ok")
            if (requestCode === 2) {
                println("add image result code 2")
                val selectedImage = data?.data
                val filePath = arrayOf(MediaStore.Images.Media.DATA)
                println("got filePath - " + filePath.toString())
                val c = (context as Activity).contentResolver
                    .query(selectedImage!!, filePath, null, null, null)
                c?.moveToFirst()
                val columnIndex = c?.getColumnIndex(filePath[0])
                selectedImagePath = c!!.getString(columnIndex!!)
                c.close()
                var thumbnail = BitmapFactory.decodeFile(selectedImagePath)
                thumbnail = ImageUtils.getResizedBitmap(thumbnail, 400)
                println("image path - " + Uri.parse(selectedImagePath.toString()))
                println("setting thumbnail")
                addImageBox.setImageBitmap(thumbnail)
                println("set thumbnail")
                removeImageBox.setImageResource(android.R.drawable.ic_delete)
//                BitMapToString(thumbnail)
            }
        }
    }

    fun selectImage(context: Context) {
        println("Ask for permissions")
        PermissionUtils.verifyStoragePermissions(activity as Activity)
        val options = arrayOf<CharSequence>("Choose from Gallery", "Cancel")
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Add Photo!")
        builder.setItems(options, DialogInterface.OnClickListener { dialog, item ->
            if (options[item] == "Take Photo") {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                val f = File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg")
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f))
                startActivityForResult(intent, 1)
            } else if (options[item] == "Choose from Gallery") {
                val intent = Intent(
                    Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                )
                startActivityForResult(intent, 2)
            } else if (options[item] == "Cancel") {
                dialog.dismiss()
            }
        })
        builder.show()
    }


    companion object {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private const val ARG_SECTION_NUMBER = "section_number"

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        @JvmStatic
        fun newInstance(sectionNumber: Int): MainFragment {
            return MainFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, sectionNumber)
                }
            }
        }

    }
}