# Museum Assistant — پروژه اندروید

## وضعیت واقعی این تحویل
این یک پروژه Android Studio قابل توسعه است، نه APK نهایی. موتور قوانین، مدل داده، اسکلت OCR با ML Kit، سرویس ضبط صفحه، اسکلت Accessibility و گردش ساخت APK در GitHub Actions در آن قرار دارد. **بانک ۲۵۲ نام مهارت هنوز وارد نشده و OCR هنوز به ساختار Artifact و موتور تصمیم متصل نشده است؛ بنابراین این نسخه نباید برای فروش واقعی Artifact استفاده شود.**

## ساخت APK با GitHub (بدون نصب Android Studio)
1. فایل ZIP را استخراج کن.
2. وارد https://github.com/ شو و یک حساب بساز/وارد شو.
3. از علامت + گزینه New repository را بزن؛ نام `MuseumAssistant` و حالت Public یا Private را انتخاب کن و Create repository.
4. داخل مخزن Add file → Upload files را بزن و **محتویات پوشه MuseumAssistant** را آپلود کن (خود پوشه را تو در تو آپلود نکن). پوشه `.github` هم باید آپلود شود.
5. Commit changes را بزن.
6. به تب Actions برو؛ اجرای Android APK را باز کن.
7. پس از سبز شدن کار، پایین صفحه بخش Artifacts فایل `MuseumAssistant-debug-apk` را دانلود و ZIP را استخراج کن.
8. فایل `app-debug.apk` را به گوشی منتقل و نصب کن. ممکن است لازم باشد اجازه نصب از همان منبع را در تنظیمات اندروید بدهی.

## ساخت محلی
با Android Studio نسخه جدید و JDK 17 پروژه را باز کن و از Build → Build APK(s) استفاده کن.

## نکات توسعه
- منطق نگه‌داری در `app/src/main/java/com/museumassistant/core/RuleEngine.kt` است.
- مدل قوانین در `Models.kt` است.
- کاتالوگ نام مهارت‌ها باید در `ocr/SkillMatcher.kt` از سورس رسمی MuseumCrawler تکمیل شود.
- `OcrReader.kt` متن خام را استخراج می‌کند؛ هنوز parser مختص صفحه Artifact لازم است.
- سرویس Accessibility فعلاً فقط تابع لمس عمومی دارد و الگوریتم لمس/فروش به آن متصل نشده است.
- ضبط صفحه از MediaProjection شروع می‌شود؛ گرفتن فریم باید از طریق Binder/Callback به Activity یا پردازشگر OCR وصل شود.
- فروش خودکار عمداً پیاده‌سازی/فعال نشده است تا وقتی OCR و تصمیم‌گیری واقعی اعتبارسنجی شوند.
