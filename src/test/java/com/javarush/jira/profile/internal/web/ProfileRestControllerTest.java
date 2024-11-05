package com.javarush.jira.profile.internal.web;

import com.javarush.jira.common.error.IllegalRequestDataException;
import com.javarush.jira.login.AuthUser;
import com.javarush.jira.login.Role;
import com.javarush.jira.login.User;
import com.javarush.jira.profile.ProfileTo;
import com.javarush.jira.profile.internal.ProfileMapper;
import com.javarush.jira.profile.internal.ProfileRepository;
import com.javarush.jira.profile.internal.model.Profile;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProfileRestControllerTest {

    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private ProfileMapper profileMapper;

    @InjectMocks
    private ProfileRestController profileRestController;

    private AuthUser authUser;
    private Profile profile;
    private ProfileTo profileTo;

    @BeforeEach
    void setUp() {
        authUser = new AuthUser(new User(1L, "admin@gmail.com", "admin", "adminFirstName",
                "adminLastName", "adminDisplayName",null, null, new HashSet<>(Set.of(Role.ADMIN))));

        profileTo = new ProfileTo(1L, Set.of("NEW_TASK", "DAILY_DIGEST"), Set.of());
        profileTo.setLastLogin(LocalDateTime.now());

        profile = new Profile(1L);
        profile.setLastLogin(profileTo.getLastLogin());
        profile.setMailNotifications(3L);
    }

    @Test
    void testGet_Success() {
        when(profileRepository.getOrCreate(authUser.id())).thenReturn(profile);
        when(profileMapper.toTo(profile)).thenReturn(profileTo);

        ProfileTo result = profileRestController.get(authUser);

        assertEquals(profileTo, result);
        verify(profileRepository).getOrCreate(authUser.id());
        verify(profileMapper).toTo(profile);
    }

    @Test
    void testGet_ProfileNotFound() {
        Profile newProfile = new Profile(authUser.id());
        when(profileRepository.getOrCreate(authUser.id())).thenReturn(newProfile);
        when(profileMapper.toTo(newProfile)).thenReturn(new ProfileTo(authUser.id(), Collections.emptySet(), Collections.emptySet()));

        ProfileTo result = profileRestController.get(authUser);

        assertNotNull(result);
        assertEquals(authUser.id(), result.getId());
        assertTrue(result.getMailNotifications().isEmpty());
        assertTrue(result.getContacts().isEmpty());
        verify(profileRepository).getOrCreate(authUser.id());
        verify(profileMapper).toTo(newProfile);
    }

    @Test
    void testUpdate_Success() {
        ProfileTo updatedProfileTo = new ProfileTo(1L, Set.of("DAILY_DIGEST"), Set.of());
        updatedProfileTo.setLastLogin(LocalDateTime.now());

        Profile profile = new Profile(1L);
        Profile updatedProfile = new Profile(1L);
        updatedProfile.setMailNotifications(2L); // Bitmask for "DAILY_DIGEST"

        when(profileRepository.getOrCreate(authUser.id())).thenReturn(profile);
        doReturn(updatedProfile).when(profileMapper).updateFromTo(any(Profile.class), eq(updatedProfileTo));

        profileRestController.update(updatedProfileTo, authUser);

        verify(profileRepository).getOrCreate(authUser.id());
        verify(profileMapper).updateFromTo(profile, updatedProfileTo);
        verify(profileRepository).save(updatedProfile);
    }


    @Test
    void testUpdate_ProfileNotFound() {
        ProfileTo updatedProfileTo = new ProfileTo(1L, Set.of("WEEKLY_SUMMARY"), Set.of());
        Profile newProfile = new Profile(1L);
        newProfile.setMailNotifications(4L); // "WEEKLY_SUMMARY" bitmask

        when(profileRepository.getOrCreate(authUser.id())).thenReturn(new Profile(authUser.id()));
        when(profileMapper.updateFromTo(any(Profile.class), eq(updatedProfileTo))).thenReturn(newProfile);

        profileRestController.update(updatedProfileTo, authUser);

        verify(profileRepository).getOrCreate(authUser.id());
        verify(profileMapper).updateFromTo(any(Profile.class), eq(updatedProfileTo));
        verify(profileRepository).save(newProfile);
    }

    @Test
    void testUpdate_InconsistentId() {
        ProfileTo invalidProfileTo = new ProfileTo(20L, Set.of("DAILY_DIGEST"), Set.of());

        assertThrows(IllegalRequestDataException.class, () -> profileRestController.update(invalidProfileTo, authUser.id()));

        verify(profileRepository, never()).getOrCreate(authUser.id());
        verify(profileMapper, never()).updateFromTo(any(), any());
    }
}
