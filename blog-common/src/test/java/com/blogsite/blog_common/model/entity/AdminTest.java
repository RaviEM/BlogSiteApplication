package com.blogsite.blog_common.model.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Admin Entity Tests")
class AdminTest {

    @Nested
    @DisplayName("Lifecycle Callback Tests")
    class LifecycleCallbackTests {

        @Test
        @DisplayName("Should set createdAt and updatedAt on create")
        void shouldSetCreatedAtAndUpdatedAtOnCreate() {
            Admin admin = new Admin();
            assertNull(admin.getCreatedAt());
            assertNull(admin.getUpdatedAt());

            admin.onCreate();

            assertNotNull(admin.getCreatedAt());
            assertNotNull(admin.getUpdatedAt());
            assertTrue(admin.getCreatedAt().isBefore(LocalDateTime.now().plusSeconds(1)));
            assertTrue(admin.getUpdatedAt().isBefore(LocalDateTime.now().plusSeconds(1)));
        }

        @Test
        @DisplayName("Should update updatedAt on update")
        void shouldUpdateUpdatedAtOnUpdate() throws InterruptedException {
            Admin admin = new Admin();
            LocalDateTime initialTime = LocalDateTime.now();
            admin.setUpdatedAt(initialTime);

            Thread.sleep(10);
            admin.onUpdate();

            assertNotNull(admin.getUpdatedAt());
            assertTrue(admin.getUpdatedAt().isAfter(initialTime));
        }
    }

    @Nested
    @DisplayName("Builder and Constructor Tests")
    class BuilderAndConstructorTests {

        @Test
        @DisplayName("Should create admin using builder")
        void shouldCreateAdminUsingBuilder() {
            User user = User.builder().userId(1L).build();
            LocalDateTime now = LocalDateTime.now();

            Admin admin = Admin.builder()
                    .adminId(1L)
                    .username("admin")
                    .email("admin@example.com")
                    .user(user)
                    .createdAt(now)
                    .updatedAt(now)
                    .isActive(true)
                    .build();

            assertEquals(1L, admin.getAdminId());
            assertEquals("admin", admin.getUsername());
            assertEquals("admin@example.com", admin.getEmail());
            assertEquals(user, admin.getUser());
            assertEquals(now, admin.getCreatedAt());
            assertTrue(admin.getIsActive());
        }
    }
}
