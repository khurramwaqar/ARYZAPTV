package com.google.jetstream.data.models

data class ModelValorant(
	val data: List<DataItem?>? = null,
	val status: Int? = null
)

data class AbilitiesItem(
	val displayIcon: String? = null,
	val displayName: String? = null,
	val description: String? = null,
	val slot: String? = null
)

data class RecruitmentData(
	val levelVpCostOverride: Int? = null,
	val endDate: String? = null,
	val milestoneThreshold: Int? = null,
	val milestoneId: String? = null,
	val useLevelVpCostOverride: Boolean? = null,
	val counterId: String? = null,
	val startDate: String? = null
)

data class DataItem(
	val killfeedPortrait: String? = null,
	val role: Role? = null,
	val isFullPortraitRightFacing: Boolean? = null,
	val displayName: String? = null,
	val isBaseContent: Boolean? = null,
	val description: String? = null,
	val backgroundGradientColors: List<String?>? = null,
	val isAvailableForTest: Boolean? = null,
	val uuid: String? = null,
	val characterTags: Any? = null,
	val displayIconSmall: String? = null,
	val fullPortrait: String? = null,
	val fullPortraitV2: String? = null,
	val abilities: List<AbilitiesItem?>? = null,
	val displayIcon: String? = null,
	val recruitmentData: Any? = null,
	val bustPortrait: String? = null,
	val background: String? = null,
	val assetPath: String? = null,
	val voiceLine: Any? = null,
	val isPlayableCharacter: Boolean? = null,
	val developerName: String? = null
)

data class Role(
	val displayIcon: String? = null,
	val displayName: String? = null,
	val assetPath: String? = null,
	val description: String? = null,
	val uuid: String? = null
)

