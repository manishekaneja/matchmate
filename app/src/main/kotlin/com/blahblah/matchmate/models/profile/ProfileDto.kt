package com.blahblah.matchmate.models.profile

data class ProfileDto(
    val login: LoginDto?,
    val name: NameDto?,
    val dob: DobDto?,
    val location: LocationDto?,
    val picture: PictureDto?
) {
    data class LoginDto(val uuid: String?)
    data class NameDto(val first: String?, val last: String?)
    data class DobDto(val age: Int?)
    data class LocationDto(val city: String?, val state: String?)
    data class PictureDto(val large: String?, val medium: String?)
}
