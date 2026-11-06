package com.museumassistant.ocr

import com.museumassistant.core.ArtifactCategory

/** Add the official 252 skill labels to this catalogue before relying on automatic decisions. */
object SkillMatcher {
    val catalog: Map<ArtifactCategory, List<String>> = ArtifactCategory.values().associateWith { emptyList() }
    data class Match(val name: String?, val score: Float, val certain: Boolean)
    fun match(raw: String, category: ArtifactCategory, threshold: Float = .88f): Match {
        val candidates = catalog[category].orEmpty()
        if (candidates.isEmpty()) return Match(null, 0f, false)
        val ranked = candidates.map { it to similarity(norm(raw), norm(it)) }.sortedByDescending { it.second }
        val best = ranked.first()
        val second = ranked.getOrNull(1)?.second ?: 0f
        val certain = best.second >= threshold && best.second - second >= .06f
        return Match(if (certain) best.first else null, best.second, certain)
    }
    private fun norm(s: String) = s.lowercase().replace(Regex("[^a-z0-9]+"), " ").trim().replace(Regex("\\s+"), " ")
    private fun similarity(a: String, b: String): Float {
        if (a == b) return 1f
        val prev = IntArray(b.length + 1) { it }
        var cur = IntArray(b.length + 1)
        for (i in a.indices) { cur[0] = i + 1; for (j in b.indices) cur[j + 1] = minOf(cur[j] + 1, prev[j + 1] + 1, prev[j] + if (a[i] == b[j]) 0 else 1); for (j in prev.indices) prev[j] = cur[j] }
        return 1f - prev[b.length].toFloat() / maxOf(1, maxOf(a.length, b.length))
    }
}
