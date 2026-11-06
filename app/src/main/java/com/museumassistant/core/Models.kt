package com.museumassistant.core

enum class ArtifactCategory { WEAPON, ARMOR, JEWELRY, POTTERY, WAR_WEAPON, WAR_ARMOR, WAR_EQUIPMENT }
enum class ThreeStarMode { NO, YES, ONLY_NON_OIL }
data class ArtifactSkill(val name: String, val starred: Boolean = false)
data class Artifact(val category: ArtifactCategory?, val skills: List<ArtifactSkill>, val isThreeStar: Boolean = false, val upgradeSourceIsOil: Boolean? = null, val confidence: Float = 1f)
data class KeepRule(val category: ArtifactCategory, val mandatory: List<String> = emptyList(), val optional: List<String> = emptyList(), val minimumOptionalMatches: Int = 1)
data class Decision(val keep: Boolean, val reason: String, val matchedRule: Int? = null)
