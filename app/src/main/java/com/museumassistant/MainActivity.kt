package com.museumassistant

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.projection.MediaProjectionManager
import android.os.Bundle
import android.provider.Settings
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.museumassistant.capture.ProjectionService
import com.museumassistant.core.*

class MainActivity : AppCompatActivity() {
    private val captureRequest = 401
    override fun onCreate(savedInstanceState: Bundle?) { super.onCreate(savedInstanceState)
        val root = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL; setPadding(28, 32, 28, 24) }
        root.addView(TextView(this).apply { text = "Museum Assistant · نسخه آزمایشی"; textSize = 22f })
        root.addView(TextView(this).apply { text = "ابتدا قوانین نگه‌داری را بسازید. موتور قوانین آماده است؛ کاتالوگ مهارت‌ها و اتصال OCR به رابط تصمیم‌گیری باید تکمیل و روی تصاویر بازی کالیبره شود."; textSize = 14f })
        val status = TextView(this).apply { text = "وضعیت: فروش خودکار غیرفعال" }
        root.addView(status)
        root.addView(Button(this).apply { text = "اجازه ضبط صفحه"; setOnClickListener { val m = getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager; startActivityForResult(m.createScreenCaptureIntent(), captureRequest) } })
        root.addView(Button(this).apply { text = "فعال‌سازی سرویس دسترس‌پذیری"; setOnClickListener { startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)) } })
        root.addView(Button(this).apply { text = "تست موتور قوانین"; setOnClickListener {
            val sample = Artifact(ArtifactCategory.ARMOR, listOf(ArtifactSkill("Invading Bomber Damage"), ArtifactSkill("Invading Bomber Damage"), ArtifactSkill("Invading Guerilla Damage"), ArtifactSkill("Invading Machine Gun Infantry Damage"), ArtifactSkill("Invading Paratrooper Hitpoints")))
            val rule = KeepRule(ArtifactCategory.ARMOR, mandatory = listOf("Invading Bomber Damage", "Invading Bomber Damage"), optional = listOf("Invading Guerilla Damage"), minimumOptionalMatches = 1)
            val d = RuleEngine.decide(sample, listOf(rule), ThreeStarMode.NO)
            status.text = "تست: ${if (d.keep) "KEEP" else "SELL"} — ${d.reason}"
        } })
        root.addView(TextView(this).apply { text = "هشدار: این نسخه هنوز برای فروش خودکار آماده نیست. تا تکمیل بانک ۲۵۲ مهارت و تست OCR، از آن برای فروش واقعی استفاده نکنید." })
        setContentView(ScrollView(this).apply { addView(root) })
    }
    @Deprecated("Deprecated in Android") override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) { super.onActivityResult(requestCode, resultCode, data); if (requestCode == captureRequest && resultCode == Activity.RESULT_OK && data != null) { val i = Intent(this, ProjectionService::class.java).apply { action = ProjectionService.ACTION_START; putExtra(ProjectionService.RESULT_CODE, resultCode); putExtra(ProjectionService.DATA, data) }; if (android.os.Build.VERSION.SDK_INT >= 26) startForegroundService(i) else startService(i) } }
}
