package at.xa1.saveto.model.template

import at.xa1.saveto.model.Mime
import org.junit.Test
import java.time.ZoneId
import java.time.ZonedDateTime
import kotlin.test.assertEquals

class TemplatePlaceholderTest {

    private val context = TemplatePlaceholderContext(
        time = ZonedDateTime.of(2023, 7, 9, 17, 4, 3, 789, ZoneId.of("Europe/Vienna")),
        type = Mime.from("text/plain"),
        originalFilename = "Test Original Filename"
    )

    @Test
    fun `YYYY returns 4 digit year`() {
        assertEquals(
            "2023",
            TemplatePlaceholder.YYYY.value(context)
        )
    }

    @Test
    fun `YY returns 2 digit year`() {
        assertEquals(
            "23",
            TemplatePlaceholder.YY.value(context)
        )
    }

    @Test
    fun `MMMM returns month as full name`() {
        assertEquals(
            "July",
            TemplatePlaceholder.MMMM.value(context)
        )
    }

    @Test
    fun `MMM returns month as three letter text`() {
        assertEquals(
            "Jul",
            TemplatePlaceholder.MMM.value(context)
        )
    }

    @Test
    fun `MM returns month as two digit number`() {
        assertEquals(
            "07",
            TemplatePlaceholder.MM.value(context)
        )
    }

    @Test
    fun `M returns month as two digit number`() {
        assertEquals(
            "7",
            TemplatePlaceholder.M.value(context)
        )
    }

    @Test
    fun `DD returns X`() {
        assertEquals(
            "09",
            TemplatePlaceholder.DD.value(context)
        )
    }

    @Test
    fun `D returns X`() {
        assertEquals(
            "9",
            TemplatePlaceholder.D.value(context)
        )
    }

    @Test
    fun `DAY_OF_WEEK returns X`() {
        assertEquals(
            "Sun",
            TemplatePlaceholder.DAY_OF_WEEK.value(context)
        )
    }

    @Test
    fun `DAY_OF_WEEK_FULL returns X`() {
        assertEquals(
            "Sunday",
            TemplatePlaceholder.DAY_OF_WEEK_FULL.value(context)
        )
    }

    @Test
    fun `HH returns X`() {
        assertEquals(
            "17",
            TemplatePlaceholder.HH.value(context)
        )
    }

    @Test
    fun `H returns X`() {
        assertEquals(
            "17",
            TemplatePlaceholder.H.value(context)
        )
    }

    @Test
    fun `hh returns X`() {
        assertEquals(
            "05",
            TemplatePlaceholder.LOWERCASE_HH.value(context)
        )
    }

    @Test
    fun `h returns X`() {
        assertEquals(
            "5",
            TemplatePlaceholder.LOWERCASE_H.value(context)
        )
    }

    @Test
    fun `AMPM returns X`() {
        assertEquals(
            "pm",
            TemplatePlaceholder.AM_PM.value(context)
        )
    }

    @Test
    fun `mm returns X`() {
        assertEquals(
            "04",
            TemplatePlaceholder.LOWERCASE_MM.value(context)
        )
    }

    @Test
    fun `m returns X`() {
        assertEquals(
            "4",
            TemplatePlaceholder.LOWERCASE_M.value(context)
        )
    }

    @Test
    fun `ss returns X`() {
        assertEquals(
            "03",
            TemplatePlaceholder.LOWERCASE_SS.value(context)
        )
    }

    @Test
    fun `s returns X`() {
        assertEquals(
            "3",
            TemplatePlaceholder.LOWERCASE_S.value(context)
        )
    }

    @Test
    fun `ORIGINAL_FILENAME returns X`() {
        assertEquals(
            "Test Original Filename",
            TemplatePlaceholder.ORIGINAL_FILENAME.value(context)
        )
    }

    @Test
    fun `fill placeholders`() {
        assertEquals(
            "Hello, 2023 world 3! {NON_EXISTINNG_PLACEHOLDER}",
            TemplatePlaceholder.fill(
                "Hello, {YYYY} world {s}! {NON_EXISTINNG_PLACEHOLDER}",
                context
            )
        )
    }
}
