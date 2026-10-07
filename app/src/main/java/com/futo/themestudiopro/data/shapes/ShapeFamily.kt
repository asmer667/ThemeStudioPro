package com.futo.themestudiopro.data.shapes

/**
 * عائلة من عائلات الأشكال الهندسية.
 * كل عائلة لها معرّف، اسم عرض، رمز، نمط رسم، ووصف.
 */
data class ShapeFamily(
    val id: String,
    val displayName: String,
    val emoji: String,
    val renderStyle: RenderStyle,
    val description: String = "",
)

/**
 * نمط الرسم — يحدّد كيف يُرسم الشكل داخل الـ Canvas.
 */
enum class RenderStyle {
    GLASS,
    FLAT,
    GEOMETRY_3D,
    ARROW,
    ICON,
    KEYCAP,
    GRADIENT,
    TORUS_3D,
    NEON,
    ORGANIC,
    PATTERN,
    TOGGLE,
    LUXURY,
    LETTER,
    HYBRID_3D,
    MIXED,
}

/**
 * العائلات الـ16 بترتيب الصورة المرجعية.
 */
object ShapeFamilies {

    val ALL: List<ShapeFamily> = listOf(
        ShapeFamily("glass",      "أزرار دائرية ومربعة", "◉", RenderStyle.GLASS,       "زجاجي لامع بألوان متدرّجة"),
        ShapeFamily("geometry3d", "أشكال هندسية 3D",     "◈", RenderStyle.GEOMETRY_3D, "مكعبات، أهرامات، كرات"),
        ShapeFamily("flat",       "مخلّعات مسطّحة",       "▲", RenderStyle.FLAT,        "مثلثات، معينات، أشكال مسطّحة"),
        ShapeFamily("arrow",      "أسهم واتجاهات",        "→", RenderStyle.ARROW,       "يمين، يسار، أعلى، أسفل"),
        ShapeFamily("icon",       "رموز ووظائف",         "✓", RenderStyle.ICON,        "صح، خطأ، زائد، نجوم، قلوب"),
        ShapeFamily("keycap",     "مفاتيح خاصة",          "⌘", RenderStyle.KEYCAP,      "123، ABC، ميكروفون"),
        ShapeFamily("gradient",   "تدرّجات زجاجية",       "▬", RenderStyle.GRADIENT,    "مستطيلات بتدرّج زجاجي"),
        ShapeFamily("torus3d",    "حلقات ومجسمات",        "◯", RenderStyle.TORUS_3D,    "حلقات، نجوم، معينات"),
        ShapeFamily("neon",       "نيون وإضاءة",          "✦", RenderStyle.NEON,        "حدود متوهّجة"),
        ShapeFamily("organic",    "أشكال طبيعية",         "❀", RenderStyle.ORGANIC,     "أوراق، قطرات، أزهار"),
        ShapeFamily("pattern",    "أنماط متكرّرة",        "▦", RenderStyle.PATTERN,     "نقاط، خطوط، موجات"),
        ShapeFamily("toggle",     "أزرار تبديل",          "⬭", RenderStyle.TOGGLE,      "مفاتيح On/Off"),
        ShapeFamily("luxury",     "فخمة ومعدنية",         "♛", RenderStyle.LUXURY,      "نجوم، تيجان، دروع"),
        ShapeFamily("letter",     "حروف وكلمات",          "ا", RenderStyle.LETTER,      "حروف عربية وإنجليزية"),
        ShapeFamily("hybrid3d",   "مجسمات مدموجة",        "◆", RenderStyle.HYBRID_3D,   "مكعبات متراكبة"),
        ShapeFamily("mixed",      "مجموعات متنوعة",       "★", RenderStyle.MIXED,       "أزهار، نجوم، ريش"),
    )

    fun findById(id: String): ShapeFamily? = ALL.firstOrNull { it.id == id }
}
