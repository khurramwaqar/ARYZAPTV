import io.realm.kotlin.types.RealmObject

class Favorites : RealmObject {

    var userId: String? = ""
    var seriedId: List<SeriesList>? = null

}

class SeriesList : RealmObject {
    var _id: String? = ""
    var title: String? = ""
    var description: String? = ""
    var cast: List<String>? = null
    var seriesDM: String? = ""
    var seriesYT: String? = ""
    var seiresCDN: String? = ""
    var imagePoster: String? = ""
    var imageCoverMobile: String? = ""
    var imageCoverDesktop: String? = ""
    var trailer: String? = ""
    var ost: String? = ""
    var logo: String? = ""
    var ageRatingId: String? = ""
    var time: String? = ""
    var status: String? = ""
    var geoPolicy: String? = ""
    var adsManager: String? = ""
    var seriesType: String? = ""
    var publishedAt: String? = ""
    var position: Int? = 0



}