package com.example.api

import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

object GeminiSatireService {

    private val httpClient = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .writeTimeout(15, TimeUnit.SECONDS)
        .build()

    private val fallbackQuotes = listOf(
        "Dân cày chay tiếc vài nghìn lẻ à? NPH đang mỏi mắt chờ bạn nạp kìa!",
        "Chần chừ không nạp tiền thì chỉ có nước ngửi khói Cậu Ấm VIP15 thôi!",
        "Nạp 10k là chiến thắng ngay, suy nghĩ gì mà lâu thế hỡi đại gia dởm?",
        "Do dự là bản chất của dân nghèo! Hãy nạp tiền để xưng bá server!",
        "Đối thủ nạp VIP15 rồi, bạn còn đứng đó đếm xu lẻ làm gì?",
        "Tiếc tiền nạp game thì làm sao làm trùm đấu trường PvP được?",
        "Nạp VIP ngay đi, chần chừ chỉ làm đối thủ khinh bỉ thêm thôi!"
    )

    suspend fun generatePvpSatire(opponentName: String, playerPower: Long): String = withContext(Dispatchers.IO) {
        val apiKey = try { BuildConfig.GEMINI_API_KEY } catch (e: Exception) { "" }

        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext fallbackQuotes.random()
        }

        val prompt = """
            Bạn là giọng nói AI châm biếm, tham tiền của game Pay-to-Win 'Hãy Đưa Tiền Cho Tôi'.
            Người chơi đang đấu PvP với đối thủ '$opponentName' nhưng đang chần chừ, tiếc tiền không chịu nạp gói tăng lực chiến.
            Lực chiến hiện tại của người chơi rất thấp ($playerPower).
            
            Hãy viết đúng 1 câu ngắn gọn bằng tiếng Việt (dưới 20 từ) cực kỳ hài hước, châm biếm tính tiếc tiền, 'cày chay' của người chơi và giục người chơi nạp tiền ngay.
            Chỉ trả về duy nhất 1 câu văn bản thuần, KHÔNG biểu tượng cảm xúc, KHÔNG dấu chấm câu phức tạp để đọc bằng AI Voice.
        """.trimIndent()

        val requestJson = JSONObject().apply {
            put("contents", JSONArray().apply {
                put(JSONObject().apply {
                    put("parts", JSONArray().apply {
                        put(JSONObject().apply {
                            put("text", prompt)
                        })
                    })
                })
            })
            put("generationConfig", JSONObject().apply {
                put("temperature", 1.0)
                put("maxOutputTokens", 60)
            })
        }

        val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=$apiKey"

        val request = Request.Builder()
            .url(url)
            .post(requestJson.toString().toRequestBody("application/json".toMediaType()))
            .build()

        try {
            httpClient.newCall(request).execute().use { response ->
                val bodyStr = response.body?.string() ?: ""
                if (response.isSuccessful && bodyStr.isNotBlank()) {
                    val root = JSONObject(bodyStr)
                    val candidates = root.optJSONArray("candidates")
                    if (candidates != null && candidates.length() > 0) {
                        val firstCand = candidates.getJSONObject(0)
                        val content = firstCand.optJSONObject("content")
                        val parts = content?.optJSONArray("parts")
                        if (parts != null && parts.length() > 0) {
                            val text = parts.getJSONObject(0).optString("text", "").trim()
                            if (text.isNotBlank()) {
                                return@withContext text.replace("*", "").replace("#", "").replace("\n", " ").trim()
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return@withContext fallbackQuotes.random()
    }
}

