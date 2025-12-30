package com.example.clipboardpro

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.clipboardpro.databinding.ActivityMainBinding
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val scope = CoroutineScope(Dispatchers.Main + Job())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

        binding.btnPaste.setOnClickListener {
            scope.launch {
                val clip = clipboard.primaryClip
                val text = if (clip != null && clip.itemCount > 0) clip.getItemAt(0).text.toString() else ""
                binding.editor.setText(text)
                if (text.isNotEmpty()) Toast.makeText(this@MainActivity, "Beillesztve!", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnCopy.setOnClickListener {
            val txt = binding.editor.text.toString()
            if (txt.isNotEmpty()) {
                clipboard.setPrimaryClip(ClipData.newPlainText("text", txt))
                Toast.makeText(this, "Másolva: ${txt.length} karakter", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnClear.setOnClickListener {
            binding.editor.text.clear()
            clipboard.setPrimaryClip(ClipData.newPlainText("", ""))
            Toast.makeText(this, "Memória törölve!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }
}
