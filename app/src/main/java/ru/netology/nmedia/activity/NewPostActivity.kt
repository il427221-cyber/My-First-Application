package ru.netology.nmedia.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContract
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.ActivityNewPostBinding
import ru.netology.nmedia.dto.Post

class NewPostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val binding = ActivityNewPostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.ok.setOnClickListener {
            val text = binding.edit.text.toString()
            if (text.isBlank()) {
                setResult(RESULT_CANCELED)
            } else {
                val intent = Intent().putExtra(NewPostContract.KEY_TEXT, text)
                setResult(RESULT_OK, intent)
            }
            finish()
        }

        val textToEdit = intent.getStringExtra(EditContract.KEY_TEXT)
        binding.edit.setText(textToEdit)
    }
}

object NewPostContract : ActivityResultContract<Unit, String?>() {
    const val KEY_TEXT = "post_text"

    override fun createIntent(context: Context, input: Unit): Intent =
        Intent(context, NewPostActivity::class.java)

    override fun parseResult(resultCode: Int, intent: Intent?): String? =
        intent?.getStringExtra(KEY_TEXT)
}

object EditContract : ActivityResultContract<String, String?>() {
    const val KEY_TEXT = "post_text"

    override fun createIntent(context: Context, input: String): Intent =
        Intent(context, NewPostActivity::class.java)
            .putExtra(KEY_TEXT, input)


    override fun parseResult(resultCode: Int, intent: Intent?): String? =
        intent?.getStringExtra(KEY_TEXT)
}