package com.blogsite.blog_common.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("DateTimeUtil Tests")
class DateTimeUtilTest {

    @Nested
    @DisplayName("Parse Date Tests")
    class ParseDateTests {
        @Test
        @DisplayName("Should parse valid date string")
        void shouldParseValidDateString() {
            LocalDate result = DateTimeUtil.parseDate("2024-01-15");
            assertNotNull(result);
            assertEquals(2024, result.getYear());
            assertEquals(1, result.getMonthValue());
            assertEquals(15, result.getDayOfMonth());
        }

        @ParameterizedTest
        @NullSource
        @ValueSource(strings = {"", " ", "  "})
        @DisplayName("Should return null for null, empty, or blank date string")
        void shouldReturnNullForNullOrBlankDate(String dateString) {
            assertNull(DateTimeUtil.parseDate(dateString));
        }

        @ParameterizedTest
        @ValueSource(strings = {
            "2024/01/15",
            "01-15-2024",
            "2024-1-15",
            "2024-01-5",
            "invalid",
            "2024-13-01",
            "2024-01-32"
        })
        @DisplayName("Should throw IllegalArgumentException for invalid date format")
        void shouldThrowForInvalidDateFormat(String dateString) {
            assertThrows(IllegalArgumentException.class, () -> DateTimeUtil.parseDate(dateString));
        }
    }

    @Nested
    @DisplayName("Format Date Tests")
    class FormatDateTests {
        @Test
        @DisplayName("Should format date correctly")
        void shouldFormatDateCorrectly() {
            LocalDate date = LocalDate.of(2024, 1, 15);
            String result = DateTimeUtil.formatDate(date);
            assertEquals("2024-01-15", result);
        }

        @Test
        @DisplayName("Should return null for null date")
        void shouldReturnNullForNullDate() {
            assertNull(DateTimeUtil.formatDate(null));
        }
    }

    @Nested
    @DisplayName("Format DateTime Tests")
    class FormatDateTimeTests {
        @Test
        @DisplayName("Should format dateTime correctly")
        void shouldFormatDateTimeCorrectly() {
            LocalDateTime dateTime = LocalDateTime.of(2024, 1, 15, 14, 30, 45);
            String result = DateTimeUtil.formatDateTime(dateTime);
            assertEquals("2024-01-15 14:30:45", result);
        }

        @Test
        @DisplayName("Should return null for null dateTime")
        void shouldReturnNullForNullDateTime() {
            assertNull(DateTimeUtil.formatDateTime(null));
        }
    }

    @Nested
    @DisplayName("Is Within Range Tests")
    class IsWithinRangeTests {
        @Test
        @DisplayName("Should return true when dateTime is within range")
        void shouldReturnTrueWhenWithinRange() {
            LocalDateTime dateTime = LocalDateTime.of(2024, 6, 15, 12, 0);
            LocalDateTime from = LocalDateTime.of(2024, 1, 1, 0, 0);
            LocalDateTime to = LocalDateTime.of(2024, 12, 31, 23, 59);
            
            assertTrue(DateTimeUtil.isWithinRange(dateTime, from, to));
        }

        @Test
        @DisplayName("Should return false when dateTime is before from")
        void shouldReturnFalseWhenBeforeFrom() {
            LocalDateTime dateTime = LocalDateTime.of(2023, 12, 31, 23, 59);
            LocalDateTime from = LocalDateTime.of(2024, 1, 1, 0, 0);
            LocalDateTime to = LocalDateTime.of(2024, 12, 31, 23, 59);
            
            assertFalse(DateTimeUtil.isWithinRange(dateTime, from, to));
        }

        @Test
        @DisplayName("Should return false when dateTime is after to")
        void shouldReturnFalseWhenAfterTo() {
            LocalDateTime dateTime = LocalDateTime.of(2025, 1, 1, 0, 0);
            LocalDateTime from = LocalDateTime.of(2024, 1, 1, 0, 0);
            LocalDateTime to = LocalDateTime.of(2024, 12, 31, 23, 59);
            
            assertFalse(DateTimeUtil.isWithinRange(dateTime, from, to));
        }

        @Test
        @DisplayName("Should return true when from is null")
        void shouldReturnTrueWhenFromIsNull() {
            LocalDateTime dateTime = LocalDateTime.of(2024, 6, 15, 12, 0);
            LocalDateTime to = LocalDateTime.of(2024, 12, 31, 23, 59);
            
            assertTrue(DateTimeUtil.isWithinRange(dateTime, null, to));
        }

        @Test
        @DisplayName("Should return true when to is null")
        void shouldReturnTrueWhenToIsNull() {
            LocalDateTime dateTime = LocalDateTime.of(2024, 6, 15, 12, 0);
            LocalDateTime from = LocalDateTime.of(2024, 1, 1, 0, 0);
            
            assertTrue(DateTimeUtil.isWithinRange(dateTime, from, null));
        }

        @Test
        @DisplayName("Should return false when dateTime is null")
        void shouldReturnFalseWhenDateTimeIsNull() {
            LocalDateTime from = LocalDateTime.of(2024, 1, 1, 0, 0);
            LocalDateTime to = LocalDateTime.of(2024, 12, 31, 23, 59);
            
            assertFalse(DateTimeUtil.isWithinRange(null, from, to));
        }
    }

    @Nested
    @DisplayName("Start Of Day Tests")
    class StartOfDayTests {
        @Test
        @DisplayName("Should return start of day for valid date")
        void shouldReturnStartOfDay() {
            LocalDate date = LocalDate.of(2024, 1, 15);
            LocalDateTime result = DateTimeUtil.startOfDay(date);
            
            assertNotNull(result);
            assertEquals(2024, result.getYear());
            assertEquals(1, result.getMonthValue());
            assertEquals(15, result.getDayOfMonth());
            assertEquals(0, result.getHour());
            assertEquals(0, result.getMinute());
            assertEquals(0, result.getSecond());
        }

        @Test
        @DisplayName("Should return null for null date")
        void shouldReturnNullForNullDate() {
            assertNull(DateTimeUtil.startOfDay(null));
        }
    }

    @Nested
    @DisplayName("End Of Day Tests")
    class EndOfDayTests {
        @Test
        @DisplayName("Should return end of day for valid date")
        void shouldReturnEndOfDay() {
            LocalDate date = LocalDate.of(2024, 1, 15);
            LocalDateTime result = DateTimeUtil.endOfDay(date);
            
            assertNotNull(result);
            assertEquals(2024, result.getYear());
            assertEquals(1, result.getMonthValue());
            assertEquals(15, result.getDayOfMonth());
            assertEquals(23, result.getHour());
            assertEquals(59, result.getMinute());
            assertEquals(59, result.getSecond());
        }

        @Test
        @DisplayName("Should return null for null date")
        void shouldReturnNullForNullDate() {
            assertNull(DateTimeUtil.endOfDay(null));
        }
    }
}
