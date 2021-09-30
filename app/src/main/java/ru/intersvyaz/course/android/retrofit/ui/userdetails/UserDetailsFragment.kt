package ru.intersvyaz.course.android.retrofit.ui.userdetails

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.user_fragment.*
import ru.intersvyaz.course.android.retrofit.R
import ru.intersvyaz.course.android.retrofit.data.model.User
import java.io.ByteArrayOutputStream
import android.widget.RemoteViews
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import kotlinx.android.synthetic.main.notification_layout.*
import kotlinx.android.synthetic.main.user_fragment.view.*
import kotlinx.coroutines.launch
import java.io.IOException
import java.io.InputStream
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*


class UserDetailsFragment : Fragment(R.layout.user_fragment) {
    private val args: UserDetailsFragmentArgs by navArgs()
    private val viewModel: UserDetailsViewModel by lazy { ViewModelProvider(this).get(UserDetailsViewModel::class.java) }
    private var CHANNEL_ID = "My notification"

    @RequiresApi(Build.VERSION_CODES.Q)
    private val userObserver = Observer<User?> {
        val btn = requireView().findViewById<Button>(R.id.button)
        it ?: return@Observer
        requireView().findViewById<TextView>(R.id.userName).text = it.name
        requireView().findViewById<TextView>(R.id.userId).text = it.id
        requireView().findViewById<TextView>(R.id.userEmail).text = it.email
        val imageViewAvatar = requireView().findViewById<ImageView>(R.id.userAvatar)

        Glide.with(imageViewAvatar.context)
            .load(it.avatar)
            .into(imageViewAvatar)

        btn.setOnClickListener {
            viewModel.user.observe(viewLifecycleOwner) { res->
                val image : Bitmap = getBitmapFromView(imageViewAvatar)
                val share = Intent(Intent.ACTION_SEND)
                share.type = "image/*"
                share.putExtra(Intent.EXTRA_STREAM, context?.let { it1 -> getImgUri(it1, image) })
                if (res != null) {
                    share.putExtra(Intent.EXTRA_TEXT, "${res.name} ${res.email}")
                }
                startActivity(Intent.createChooser(share, "Поделиться .."))
            }
        }



        // Получаю bitmap из аватара пользователя
        // val image : Bitmap = getBitmapFromView(imageViewAvatar)
        val simpleDateFormat = SimpleDateFormat("HH:mm")
        val currentTime: String = simpleDateFormat.format(Date())

        val remoteViews = RemoteViews(requireContext().packageName, R.layout.notification_layout)
        remoteViews.setTextViewText(R.id.notifyName, it.name)
        remoteViews.setTextViewText(R.id.notifyEmail, it.email)
        remoteViews.setTextViewText(R.id.notifyDate, currentTime)

        // устанавливаю иконку LargeIcon
        val builder = NotificationCompat.Builder(this.requireContext(), CHANNEL_ID)
            .setCustomContentView(remoteViews)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setSmallIcon(R.drawable.ic_notifications)

        lifecycleScope.launch {
            val t = getBitmap(it.avatar)
            builder.setLargeIcon(t)
            // System.out.println("RESULT_BITMAP $t")
        }

        addNotification.setOnClickListener {
            with(NotificationManagerCompat.from(requireContext())) {
                notify(0, builder.build())
            }
        }
    }

    private fun getImgUri(context: Context, img: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        img.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(context.contentResolver,
        img, "Изображение", null)
        return Uri.parse(path)
    }

    private fun getBitmapFromView(view: ImageView):Bitmap {
        val bitmap = Bitmap.createBitmap(view.width, view.height,Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        args.let { viewModel.loadUserInfo(args.userId) }
        viewModel.user.observe(viewLifecycleOwner, userObserver)
    }

    private suspend fun getBitmap(url: String) : Bitmap {
        val loading = ImageLoader(requireContext())
        val request  = ImageRequest.Builder(requireContext())
            .data(url)
            .build()
        val result = (loading.execute(request) as SuccessResult).drawable
        return (result as BitmapDrawable).bitmap
    }

}