package com.futo.themestudiopro.data

/**
 * بيانات خط واحد من Google Fonts.
 */
data class FontInfo(
    val id: String,
    val displayName: String,
    val fileName: String,
    val url: String,
    val language: FontLanguage,
    val category: FontCategory,
)

enum class FontLanguage(val display: String) {
    ARABIC("العربية"),
    ENGLISH("English"),
}

enum class FontCategory(val display: String) {
    SANS("Sans"),
    SERIF("Serif"),
    DISPLAY("Display"),
    HANDWRITING("Handwriting"),
    MONOSPACE("Monospace"),
}

/**
 * كتالوج الخطوط — 20 عربي + 20 إنجليزي.
 */
object FontCatalog {

    private const val GH = "https://github.com/google/fonts/raw/main"

    val ARABIC: List<FontInfo> = listOf(
        FontInfo(
            "cairo", "القاهرة", "Cairo-Regular.ttf",
            "$GH/ofl/cairo/Cairo%5Bslnt%2Cwght%5D.ttf",
            FontLanguage.ARABIC, FontCategory.SANS,
        ),
        FontInfo(
            "tajawal", "تجوّل", "Tajawal-Regular.ttf",
            "$GH/ofl/tajawal/Tajawal-Regular.ttf",
            FontLanguage.ARABIC, FontCategory.SANS,
        ),
        FontInfo(
            "almarai", "المراعي", "Almarai-Regular.ttf",
            "$GH/ofl/almarai/Almarai-Regular.ttf",
            FontLanguage.ARABIC, FontCategory.SANS,
        ),
        FontInfo(
            "amiri", "أميري", "Amiri-Regular.ttf",
            "$GH/ofl/amiri/Amiri-Regular.ttf",
            FontLanguage.ARABIC, FontCategory.SERIF,
        ),
        FontInfo(
            "changa", "شنغها", "Changa-Regular.ttf",
            "$GH/ofl/changa/Changa-Regular.ttf",
            FontLanguage.ARABIC, FontCategory.SANS,
        ),
        FontInfo(
            "elmessiri", "المصيري", "ElMessiri-Regular.ttf",
            "$GH/ofl/elmessiri/ElMessiri%5Bwght%5D.ttf",
            FontLanguage.ARABIC, FontCategory.SANS,
        ),
        FontInfo(
            "reemkufi", "ريم كوفي", "ReemKufi-Regular.ttf",
            "$GH/ofl/reemkufi/ReemKufi%5Bwght%5D.ttf",
            FontLanguage.ARABIC, FontCategory.DISPLAY,
        ),
        FontInfo(
            "markazi", "مركزي", "MarkaziText-Regular.ttf",
            "$GH/ofl/markazitext/MarkaziText%5Bwght%5D.ttf",
            FontLanguage.ARABIC, FontCategory.SERIF,
        ),
        FontInfo(
            "kufam", "كوفام", "Kufam-Regular.ttf",
            "$GH/ofl/kufam/Kufam%5Bwght%5D.ttf",
            FontLanguage.ARABIC, FontCategory.DISPLAY,
        ),
        FontInfo(
            "lateef", "لطيف", "Lateef-Regular.ttf",
            "$GH/ofl/lateef/Lateef-Regular.ttf",
            FontLanguage.ARABIC, FontCategory.SERIF,
        ),
        FontInfo(
            "scheherazade", "شهرزاد", "ScheherazadeNew-Regular.ttf",
            "$GH/ofl/scheherazadenew/ScheherazadeNew-Regular.ttf",
            FontLanguage.ARABIC, FontCategory.SERIF,
        ),
        FontInfo(
            "notoarabic", "نوتو عربي", "NotoNaskhArabic-Regular.ttf",
            "$GH/ofl/notonaskharabic/NotoNaskhArabic%5Bwght%5D.ttf",
            FontLanguage.ARABIC, FontCategory.SANS,
        ),
        FontInfo(
            "ibmplexarabic", "IBM بلكس عربي", "IBMPlexSansArabic-Regular.ttf",
            "$GH/ofl/ibmplexsansarabic/IBMPlexSansArabic-Regular.ttf",
            FontLanguage.ARABIC, FontCategory.SANS,
        ),
        FontInfo(
            "vibes", "فايبس", "Vibes-Regular.ttf",
            "$GH/ofl/vibes/Vibes-Regular.ttf",
            FontLanguage.ARABIC, FontCategory.DISPLAY,
        ),
        FontInfo(
            "lemonada", "ليمونادة", "Lemonada-Regular.ttf",
            "$GH/ofl/lemonada/Lemonada%5Bwght%5D.ttf",
            FontLanguage.ARABIC, FontCategory.DISPLAY,
        ),
        FontInfo(
            "mada", "مدى", "Mada-Regular.ttf",
            "$GH/ofl/mada/Mada%5Bwght%5D.ttf",
            FontLanguage.ARABIC, FontCategory.SANS,
        ),
        FontInfo(
            "baloo", "بالو بهاجان", "BalooBhaijaan2-Regular.ttf",
            "$GH/ofl/baloobhaijaan2/BalooBhaijaan2%5Bwght%5D.ttf",
            FontLanguage.ARABIC, FontCategory.DISPLAY,
        ),
        FontInfo(
            "cairoplay", "القاهرة بلاي", "CairoPlay-Regular.ttf",
            "$GH/ofl/cairoplay/CairoPlay%5Bslnt%2Cwght%5D.ttf",
            FontLanguage.ARABIC, FontCategory.DISPLAY,
        ),
        FontInfo(
            "alkalami", "القلمي", "Alkalami-Regular.ttf",
            "$GH/ofl/alkalami/Alkalami-Regular.ttf",
            FontLanguage.ARABIC, FontCategory.SERIF,
        ),
        FontInfo(
            "mirza", "ميرزا", "Mirza-Regular.ttf",
            "$GH/ofl/mirza/Mirza-Regular.ttf",
            FontLanguage.ARABIC, FontCategory.SERIF,
        ),
    )

    val ENGLISH: List<FontInfo> = listOf(
        FontInfo(
            "roboto", "Roboto", "Roboto-Regular.ttf",
            "$GH/apache/roboto/Roboto%5Bwdth%2Cwght%5D.ttf",
            FontLanguage.ENGLISH, FontCategory.SANS,
        ),
        FontInfo(
            "opensans", "Open Sans", "OpenSans-Regular.ttf",
            "$GH/apache/opensans/OpenSans%5Bwdth%2Cwght%5D.ttf",
            FontLanguage.ENGLISH, FontCategory.SANS,
        ),
        FontInfo(
            "lato", "Lato", "Lato-Regular.ttf",
            "$GH/ofl/lato/Lato-Regular.ttf",
            FontLanguage.ENGLISH, FontCategory.SANS,
        ),
        FontInfo(
            "montserrat", "Montserrat", "Montserrat-Regular.ttf",
            "$GH/ofl/montserrat/Montserrat%5Bwght%5D.ttf",
            FontLanguage.ENGLISH, FontCategory.SANS,
        ),
        FontInfo(
            "poppins", "Poppins", "Poppins-Regular.ttf",
            "$GH/ofl/poppins/Poppins-Regular.ttf",
            FontLanguage.ENGLISH, FontCategory.SANS,
        ),
        FontInfo(
            "inter", "Inter", "Inter-Regular.ttf",
            "$GH/ofl/inter/Inter%5Bopsz%2Cwght%5D.ttf",
            FontLanguage.ENGLISH, FontCategory.SANS,
        ),
        FontInfo(
            "rubik", "Rubik", "Rubik-Regular.ttf",
            "$GH/ofl/rubik/Rubik%5Bwght%5D.ttf",
            FontLanguage.ENGLISH, FontCategory.SANS,
        ),
        FontInfo(
            "nunito", "Nunito", "Nunito-Regular.ttf",
            "$GH/ofl/nunito/Nunito%5Bwght%5D.ttf",
            FontLanguage.ENGLISH, FontCategory.SANS,
        ),
        FontInfo(
            "oswald", "Oswald", "Oswald-Regular.ttf",
            "$GH/ofl/oswald/Oswald%5Bwght%5D.ttf",
            FontLanguage.ENGLISH, FontCategory.DISPLAY,
        ),
        FontInfo(
            "raleway", "Raleway", "Raleway-Regular.ttf",
            "$GH/ofl/raleway/Raleway%5Bwght%5D.ttf",
            FontLanguage.ENGLISH, FontCategory.SANS,
        ),
        FontInfo(
            "merriweather", "Merriweather", "Merriweather-Regular.ttf",
            "$GH/ofl/merriweather/Merriweather%5Bopsz%2Cwdth%2Cwght%5D.ttf",
            FontLanguage.ENGLISH, FontCategory.SERIF,
        ),
        FontInfo(
            "playfair", "Playfair Display", "PlayfairDisplay-Regular.ttf",
            "$GH/ofl/playfairdisplay/PlayfairDisplay%5Bwght%5D.ttf",
            FontLanguage.ENGLISH, FontCategory.SERIF,
        ),
        FontInfo(
            "dancing", "Dancing Script", "DancingScript-Regular.ttf",
            "$GH/ofl/dancingscript/DancingScript%5Bwght%5D.ttf",
            FontLanguage.ENGLISH, FontCategory.HANDWRITING,
        ),
        FontInfo(
            "pacifico", "Pacifico", "Pacifico-Regular.ttf",
            "$GH/ofl/pacifico/Pacifico-Regular.ttf",
            FontLanguage.ENGLISH, FontCategory.HANDWRITING,
        ),
        FontInfo(
            "lobster", "Lobster", "Lobster-Regular.ttf",
            "$GH/ofl/lobster/Lobster-Regular.ttf",
            FontLanguage.ENGLISH, FontCategory.DISPLAY,
        ),
        FontInfo(
            "bebasneue", "Bebas Neue", "BebasNeue-Regular.ttf",
            "$GH/ofl/bebasneue/BebasNeue-Regular.ttf",
            FontLanguage.ENGLISH, FontCategory.DISPLAY,
        ),
        FontInfo(
            "righteous", "Righteous", "Righteous-Regular.ttf",
            "$GH/ofl/righteous/Righteous-Regular.ttf",
            FontLanguage.ENGLISH, FontCategory.DISPLAY,
        ),
        FontInfo(
            "caveat", "Caveat", "Caveat-Regular.ttf",
            "$GH/ofl/caveat/Caveat%5Bwght%5D.ttf",
            FontLanguage.ENGLISH, FontCategory.HANDWRITING,
        ),
        FontInfo(
            "comfortaa", "Comfortaa", "Comfortaa-Regular.ttf",
            "$GH/ofl/comfortaa/Comfortaa%5Bwght%5D.ttf",
            FontLanguage.ENGLISH, FontCategory.DISPLAY,
        ),
        FontInfo(
            "jetbrainsmono", "JetBrains Mono", "JetBrainsMono-Regular.ttf",
            "$GH/ofl/jetbrainsmono/JetBrainsMono%5Bwght%5D.ttf",
            FontLanguage.ENGLISH, FontCategory.MONOSPACE,
        ),
    )

    val ALL: List<FontInfo> = ARABIC + ENGLISH

    fun byId(id: String): FontInfo? = ALL.find { it.id == id }
}
