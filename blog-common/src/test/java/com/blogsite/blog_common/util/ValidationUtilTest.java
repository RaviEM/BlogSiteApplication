package com.blogsite.blog_common.util;

import com.blogsite.blog_common.constant.ApiConstants;
import com.blogsite.blog_common.exception.ValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ValidationUtil Tests")
class ValidationUtilTest {

    @Nested
    @DisplayName("Email Validation Tests")
    class EmailValidationTests {
        @Test
        @DisplayName("Should return true for valid email ending with .com")
        void shouldReturnTrueForValidEmail() {
            assertTrue(ValidationUtil.isValidEmail("user@example.com"));
            assertTrue(ValidationUtil.isValidEmail("test.user@domain.com"));
            assertTrue(ValidationUtil.isValidEmail("admin@blogsite.com"));
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {" ", "  ", "\t", "\n"})
        @DisplayName("Should return false for null, empty, or blank email")
        void shouldReturnFalseForInvalidEmail(String email) {
            assertFalse(ValidationUtil.isValidEmail(email));
        }

        @ParameterizedTest
        @ValueSource(strings = {
            "invalid.email",
            "user@example",
            "user@example.org",
            "user@example.net",
            "@example.com",
            "user@.com",
            "user@example.",
            "user name@example.com",
            "user@exam ple.com"
        })
        @DisplayName("Should return false for invalid email formats")
        void shouldReturnFalseForInvalidEmailFormats(String email) {
            assertFalse(ValidationUtil.isValidEmail(email));
        }
    }

    @Nested
    @DisplayName("Password Validation Tests")
    class PasswordValidationTests {
        @Test
        @DisplayName("Should return true for valid password with letters and digits")
        void shouldReturnTrueForValidPassword() {
            assertTrue(ValidationUtil.isValidPassword("Password123"));
            assertTrue(ValidationUtil.isValidPassword("abc12345")); // At least 8 chars with letters and digits
            assertTrue(ValidationUtil.isValidPassword("Test123!@#")); // 10 chars with letters and digits
            assertTrue(ValidationUtil.isValidPassword("12345678a")); // 9 chars with letters and digits
        }

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("Should return false for null or empty password")
        void shouldReturnFalseForNullOrEmptyPassword(String password) {
            assertFalse(ValidationUtil.isValidPassword(password));
        }

        @Test
        @DisplayName("Should return false for password shorter than minimum length")
        void shouldReturnFalseForShortPassword() {
            String shortPassword = "abc123"; // Less than MIN_PASSWORD_LENGTH (8)
            if (shortPassword.length() < ApiConstants.MIN_PASSWORD_LENGTH) {
                assertFalse(ValidationUtil.isValidPassword(shortPassword));
            }
        }

        @ParameterizedTest
        @ValueSource(strings = {
            "password",      // Only letters
            "12345678",      // Only digits
            "PASSWORD",      // Only uppercase letters
            "password!",     // Letters and special chars, no digits
            "12345678!"      // Digits and special chars, no letters
        })
        @DisplayName("Should return false for password without both letters and digits")
        void shouldReturnFalseForPasswordWithoutBothLettersAndDigits(String password) {
            assertFalse(ValidationUtil.isValidPassword(password));
        }
    }

    @Nested
    @DisplayName("Blog Name Validation Tests")
    class BlogNameValidationTests {
        @Test
        @DisplayName("Should return true for valid blog name")
        void shouldReturnTrueForValidBlogName() {
            String validName = "A".repeat(ApiConstants.MIN_BLOG_NAME_LENGTH);
            assertTrue(ValidationUtil.isValidBlogName(validName));
            assertTrue(ValidationUtil.isValidBlogName("This is a very long blog name that exceeds minimum"));
        }

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("Should return false for null or empty blog name")
        void shouldReturnFalseForNullOrEmptyBlogName(String blogName) {
            assertFalse(ValidationUtil.isValidBlogName(blogName));
        }

        @Test
        @DisplayName("Should return false for blog name shorter than minimum")
        void shouldReturnFalseForShortBlogName() {
            String shortName = "Short";
            if (shortName.length() < ApiConstants.MIN_BLOG_NAME_LENGTH) {
                assertFalse(ValidationUtil.isValidBlogName(shortName));
            }
        }
    }

    @Nested
    @DisplayName("Category Validation Tests")
    class CategoryValidationTests {
        @Test
        @DisplayName("Should return true for valid category")
        void shouldReturnTrueForValidCategory() {
            String validCategory = "A".repeat(ApiConstants.MIN_CATEGORY_LENGTH);
            assertTrue(ValidationUtil.isValidCategory(validCategory));
        }

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("Should return false for null or empty category")
        void shouldReturnFalseForNullOrEmptyCategory(String category) {
            assertFalse(ValidationUtil.isValidCategory(category));
        }

        @Test
        @DisplayName("Should return false for category shorter than minimum")
        void shouldReturnFalseForShortCategory() {
            String shortCategory = "Short";
            if (shortCategory.length() < ApiConstants.MIN_CATEGORY_LENGTH) {
                assertFalse(ValidationUtil.isValidCategory(shortCategory));
            }
        }
    }

    @Nested
    @DisplayName("Article Validation Tests")
    class ArticleValidationTests {
        @Test
        @DisplayName("Should return true for valid article with sufficient words")
        void shouldReturnTrueForValidArticle() {
            String validArticle = generateArticleWithWords(ApiConstants.MIN_ARTICLE_WORDS);
            assertTrue(ValidationUtil.isValidArticle(validArticle));
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {" ", "  ", "\t", "\n"})
        @DisplayName("Should return false for null, empty, or blank article")
        void shouldReturnFalseForNullOrBlankArticle(String article) {
            assertFalse(ValidationUtil.isValidArticle(article));
        }

        @Test
        @DisplayName("Should return false for article with insufficient words")
        void shouldReturnFalseForShortArticle() {
            String shortArticle = "This is a short article.";
            assertFalse(ValidationUtil.isValidArticle(shortArticle));
        }

        @Test
        @DisplayName("Should handle article with HTML tags")
        void shouldHandleArticleWithHtmlTags() {
            String articleWithHtml = "<p>" + generateArticleWithWords(ApiConstants.MIN_ARTICLE_WORDS) + "</p>";
            assertTrue(ValidationUtil.isValidArticle(articleWithHtml));
        }
    }

    @Nested
    @DisplayName("Word Count Tests")
    class WordCountTests {
        @Test
        @DisplayName("Should count words correctly")
        void shouldCountWordsCorrectly() {
            assertEquals(5, ValidationUtil.countWords("This is a test sentence"));
            assertEquals(1, ValidationUtil.countWords("Word"));
            assertEquals(0, ValidationUtil.countWords(""));
        }

        @Test
        @DisplayName("Should handle multiple spaces between words")
        void shouldHandleMultipleSpaces() {
            assertEquals(3, ValidationUtil.countWords("word1    word2     word3"));
        }

        @Test
        @DisplayName("Should handle leading and trailing spaces")
        void shouldHandleLeadingAndTrailingSpaces() {
            assertEquals(3, ValidationUtil.countWords("  word1 word2 word3  "));
        }

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("Should return 0 for null or empty text")
        void shouldReturnZeroForNullOrEmptyText(String text) {
            assertEquals(0, ValidationUtil.countWords(text));
        }
    }

    @Nested
    @DisplayName("Require Non-Blank Tests")
    class RequireNonBlankTests {
        @Test
        @DisplayName("Should not throw exception for non-blank value")
        void shouldNotThrowForNonBlankValue() {
            assertDoesNotThrow(() -> ValidationUtil.requireNonBlank("value", "fieldName"));
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {" ", "  ", "\t", "\n"})
        @DisplayName("Should throw ValidationException for null, empty, or blank value")
        void shouldThrowForNullOrBlankValue(String value) {
            ValidationException exception = assertThrows(
                ValidationException.class,
                () -> ValidationUtil.requireNonBlank(value, "fieldName")
            );
            assertEquals("fieldName is mandatory", exception.getMessage());
            assertTrue(exception.hasError());
            assertTrue(exception.getErrors().containsKey("fieldName"));
        }
    }

    @Nested
    @DisplayName("Require Min Length Tests")
    class RequireMinLengthTests {
        @Test
        @DisplayName("Should not throw exception for value meeting minimum length")
        void shouldNotThrowForValidLength() {
            assertDoesNotThrow(() -> ValidationUtil.requireMinLength("12345", 5, "fieldName"));
            assertDoesNotThrow(() -> ValidationUtil.requireMinLength("123456", 5, "fieldName"));
        }

        @Test
        @DisplayName("Should throw ValidationException for value shorter than minimum")
        void shouldThrowForShortValue() {
            ValidationException exception = assertThrows(
                ValidationException.class,
                () -> ValidationUtil.requireMinLength("123", 5, "fieldName")
            );
            assertTrue(exception.getMessage().contains("must be at least"));
            assertTrue(exception.getMessage().contains("5"));
        }

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("Should throw ValidationException for null or empty value")
        void shouldThrowForNullOrEmptyValue(String value) {
            assertThrows(
                ValidationException.class,
                () -> ValidationUtil.requireMinLength(value, 5, "fieldName")
            );
        }
    }

    // Helper method
    private String generateArticleWithWords(int wordCount) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < wordCount; i++) {
            sb.append("word").append(i);
            if (i < wordCount - 1) {
                sb.append(" ");
            }
        }
        return sb.toString();
    }
}
