package com.blogsite.blog_common.util;

import com.blogsite.blog_common.constant.ApiConstants;
import com.blogsite.blog_common.exception. ValidationException;
import java.util.regex.Pattern;

 public final class ValidationUtil {
     private static final Pattern EMAIL_PATTERN = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.com$");
     private static final Pattern ALPHANUMERIC_PATTERN = Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d).*$");

     private ValidationUtil() {
     }

     public static boolean isValidEmail(String email) {
         if (email == null || email.isBlank()) {
             return false;
         }
         return EMAIL_PATTERN.matcher(email).matches();
     }

     public static boolean isValidPassword(String password) {
         if (password == null || password.length() < ApiConstants.MIN_PASSWORD_LENGTH) {
             return false;
         }
         return ALPHANUMERIC_PATTERN.matcher(password).matches();
     }

     /**
      * Validates blog name - must be at least 20 characters
      */
     public static boolean isValidBlogName(String blogName) {
         return blogName != null && blogName.length() >= ApiConstants.MIN_BLOG_NAME_LENGTH;
     }

     public static boolean isValidCategory(String category) {
         return category != null && category.length() >= ApiConstants.MIN_CATEGORY_LENGTH;
     }

     public static boolean isValidArticle(String article) {

         if (article == null || article.isBlank()) {
             return false;
         }
         return countWords(article) >= ApiConstants.MIN_ARTICLE_WORDS;
     }

     public static int countWords(String text) {
         if (text == null || text.isBlank()) {
             return 0;
         }
         String[] words = text.trim().split("\\s+");
         return words.length;
     }

     public static void requireNonBlank(String value, String fieldName) {
         if (value == null || value.isBlank()) {
             throw new ValidationException(fieldName, fieldName + " is mandatory");
         }
     }

     public static void requireMinLength(String value, int minLength, String fieldName) {
         if (value == null || value.length() < minLength) {
             throw new ValidationException(fieldName, fieldName + " must be at least " + minLength + " characters");
         }
     }
 }