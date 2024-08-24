import com.google.jetstream.data.models.SeriesSingle
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.Ignore

class Favorites : RealmObject {
    @Ignore
    var userId: String? = ""
    @Ignore
    var seriedId: List<SeriesList>? = null

}

class SeriesList : RealmObject {
    var _id: String? = ""
    var title: String? = ""
    var description: String? = ""
    @Ignore
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