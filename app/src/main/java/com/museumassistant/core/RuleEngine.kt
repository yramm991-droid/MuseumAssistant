package com.museumassistant.core

object RuleEngine {
    fun decide(artifact: Artifact, rules: List<KeepRule>, threeStarMode: ThreeStarMode, minimumConfidence: Float = .78f): Decision {
        if (artifact.confidence < minimumConfidence) return Decision(false, "UNCERTAIN: OCR confidence too low")
        if (artifact.category == null || artifact.skills.size != 5) return Decision(false, "UNCERTAIN: category or skill count not recognized")
        if (artifact.isThreeStar) {
            val preserve = when (threeStarMode) {
                ThreeStarMode.NO -> false
                ThreeStarMode.YES -> true
                ThreeStarMode.ONLY_NON_OIL -> artifact.upgradeSourceIsOil == false
            }
            if (preserve) return Decision(true, "Kept: three-star setting")
            if (threeStarMode == ThreeStarMode.ONLY_NON_OIL && artifact.upgradeSourceIsOil == null) return Decision(false, "UNCERTAIN: unknown three-star upgrade source")
        }
        rules.forEachIndexed { index, rule ->
            if (rule.category != artifact.category) return@forEachIndexed
            val remaining = artifact.skills.map { normalize(it.name) }.toMutableList()
            var mandatoryOk = true
            rule.mandatory.forEach { required ->
                val pos = remaining.indexOf(normalize(required))
                if (pos < 0) mandatoryOk = false else remaining.removeAt(pos)
            }
            if (!mandatoryOk) return@forEachIndexed
            val optionalMatches = rule.optional.count { wanted ->
                val pos = remaining.indexOf(normalize(wanted))
                if (pos >= 0) { remaining.removeAt(pos); true } else false
            }
            if (optionalMatches >= rule.minimumOptionalMatches) return Decision(true, "Kept: rule ${index + 1} matched", index)
        }
        return Decision(false, "No keep rule matched")
    }

    private fun normalize(s: String) = s.lowercase().replace(Regex("[^a-z0-9]+"), " ").trim().replace(Regex("\\s+"), " ")
}
