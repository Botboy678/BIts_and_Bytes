package com.bits.bytes.bits.bytes.Service;


import com.bits.bytes.bits.bytes.DTOs.*;
import com.bits.bytes.bits.bytes.Factories;
import com.bits.bytes.bits.bytes.MapDTOs.MapDTOs;
import com.bits.bytes.bits.bytes.Models.*;
import com.bits.bytes.bits.bytes.Models.MiscellaneousModels.LeetCodeProfile;
import com.bits.bytes.bits.bytes.Models.MiscellaneousModels.MyCurrentUser;
import com.bits.bytes.bits.bytes.Repo.ProjectRepo;
import com.bits.bytes.bits.bytes.Repo.UserRepo;
import com.bits.bytes.bits.bytes.Services.Impl.UserServicesImpl;
import com.github.javafaker.Faker;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class UserServicesImplTests {
    // The Repo I want to mock
    @Mock
    private UserRepo userRepo;

    @Mock
    private ProjectRepo projectRepo;

    @Mock
    MyCurrentUser myCurrentUser;

    @Mock
    BCryptPasswordEncoder encoder;

    @Mock
    Factories myFactory;

    @Mock
    RestTemplate restTemplate;
    // Injecting the service layer, I want to test
    @InjectMocks
    private UserServicesImpl userServicesImpl;

    private Users testUser;
    private Users testUser2;
    private UsersDTO testUserDto;
    private ProjectsDTO testProjectsDTO;
    private Projects testProject;
    private ProjectComments testProjectComment;
    private ProjectCommentsDTO testProjectCommentsDTO;
    private ProfilesDTO testprofilesDTO;
    private Profiles testProfile;
    private LeetCodeProfile leetCodeData;
    private BugReportsDTO bugReportsDTO;
    private BugReports bugReports;

    @BeforeEach
    void setup() {
        Faker faker = new Faker();

        /* Password is global b/c for testUser I need it encoded,
        but for its DTO (testUserDTO) I need it unchanged as to make sure the
        registration is working correctly */
        String password = faker.internet().password();


        List<Users> usersList = new ArrayList<>();
        List<Projects> projectsList = new ArrayList<>();
        List<Profiles> profilesList = new ArrayList<>();

        for(int i = 0; i < 2; i++) {
            Users u = new Users();
            String url = faker.internet().url();
            String name = faker.rickAndMorty().character();
            u.setUserId(i);
            u.setUsername(name);
            u.setEmail(faker.internet().emailAddress());
            u.setPassword_hash(encoder.encode(password));
            usersList.add(u);

            Profiles profile = new Profiles();
            profile.setProfile_photo_url(url);
            profile.setGithub_url(url);
            profilesList.add(profile);
        }

        testUser = usersList.get(0);
        testUser2 = usersList.get(1);

        for(int i = 0; i < 2; i++){
            String url = faker.internet().url();
            Projects p = new Projects();
            p.setUserId(testUser);
            p.setTitle(faker.rickAndMorty().quote());
            p.setDescription(faker.rickAndMorty().quote());
            p.setGithub_repo_url(url);
            projectsList.add(p);
        }

        testUserDto = UsersDTO.builder()
                .username(testUser.getUsername())
                .email(testUser.getEmail())
                .password(password)
                .build();

        testProject = projectsList.get(0);

        testProjectsDTO = ProjectsDTO.builder()
                .userId(testUser.getUserId())
                .title(projectsList.get(1).getTitle())
                .description(projectsList.get(1).getDescription())
                .github_repo_url(projectsList.get(1).getGithub_repo_url())
                .build();

        testProjectComment = new ProjectComments();
        testProjectCommentsDTO = ProjectCommentsDTO.builder()
                .projectId(testProjectsDTO)
                .content(faker.rickAndMorty().character()).build();

        testProfile = profilesList.get(0);

        testprofilesDTO = ProfilesDTO.builder()
                .github_url(faker.internet().url())
                .profile_photo_url(faker.internet().url())
                .leetcode_username(faker.rickAndMorty().character())
                .build();

        leetCodeData = LeetCodeProfile.builder()
                .totalSolved(356)
                .totalQuestions(2800)
                .easySolved(120)
                .totalEasy(600)
                .mediumSolved(190)
                .totalMedium(1500)
                .hardSolved(46)
                .totalHard(700)
                .ranking(12450)
                .contributionPoint(320)
                .reputation(1500)
                .build();

        bugReportsDTO = BugReportsDTO.builder()
                .description(faker.rickAndMorty().quote())
                .build();

        bugReports = new BugReports();
        bugReports.setDescription(faker.rickAndMorty().quote());
    }

    @Test
    public void UserServices_Register_ReturnRegisteredUser() {
        when(userRepo.save(Mockito.any(Users.class))).thenReturn(testUser);
        userServicesImpl.Register(testUserDto);
        assertEquals(testUser.getPassword_hash(), encoder.encode(testUserDto.getPassword()));
    }

    @Test
    public void UserServices_findUser_ReturnUser()  {
            when(userRepo.findByUsername(testUserDto.getUsername())).thenReturn(testUser);
            // Act + Assert inside the static mock block
            try (MockedStatic<MapDTOs> mockedStatic = Mockito.mockStatic(MapDTOs.class)) {
                mockedStatic.when(() -> MapDTOs.mapToUsersDto(testUser))
                        .thenReturn(testUserDto);

                UsersDTO user = userServicesImpl.findUser(testUserDto.getUsername());

                Assertions.assertThat(user.getUsername()).isEqualTo(testUserDto.getUsername());
            }
        }

    @Test
    public void UserServices_addUserProjects_ReturnRecentlyUserProject(){
        when(userRepo.save(Mockito.any(Users.class))).thenReturn(testUser);
        when(myCurrentUser.getPrincipalUser()).thenReturn(testUser);
        when(myFactory.ProjectFactory()).thenReturn(testProject);

        // Act
        userServicesImpl.addUserProjects(testProjectsDTO);

        // Assert
        assertEquals(1, testUser.getProjects().size());
        assertNotNull(testUser.getProjects().iterator().next());
    }

    @Test
    public void UserServices_deleteUserProject_ReturnSuccess(){
        when(myCurrentUser.getPrincipalUser()).thenReturn(testUser);
        when(projectRepo.findByTitleAndUserId(testProject.getTitle(), testUser)).thenReturn(testProject);

        userServicesImpl.deleteUserProject(testProject.getTitle());
        assertEquals(0, testUser.getProjects().size());
    }

    @Test
    public void UserServices_updateUserProjects_ReturnUpdatedProject(){
        when(userRepo.save(Mockito.any(Users.class))).thenReturn(testUser);
        when(myCurrentUser.getPrincipalUser()).thenReturn(testUser);
        when(projectRepo.findByTitleAndUserId(testProject.getTitle(), testUser)).thenReturn(testProject);

        userServicesImpl.updateUserProjects(testProjectsDTO, testProject.getTitle());

        assertEquals(testProject.getTitle(), testProjectsDTO.getTitle());
        assertEquals(testProject.getDescription(), testProjectsDTO.getDescription());
        assertEquals(testProject.getGithub_repo_url(), testProjectsDTO.getGithub_repo_url());
    }

    @Test
    public void UserServices_addCommentToProject_ReturnAddedCommentToProject(){
        when(userRepo.save(Mockito.any(Users.class))).thenReturn(testUser2);
        when(myCurrentUser.getPrincipalUser()).thenReturn(testUser2);
        when(projectRepo.findByTitleAndUserId_Username(testProject.getTitle(), testUser.getUsername())).thenReturn(Optional.ofNullable(testProject));
        when(myFactory.ProjectCommentsFactory()).thenReturn(testProjectComment);

        userServicesImpl.addCommentToProject(testProject.getTitle(), testProjectCommentsDTO, testUser.getUsername());

        //Assert
        ProjectComments savedComment = testProject.getComments().iterator().next();
        assertEquals(testProject, savedComment.getProjectId());
        assertEquals(testUser2, savedComment.getUserId());
        assertEquals(testProjectCommentsDTO.getContent(), savedComment.getContent());
    }

    @Test
    public void UserServices_updateUserProfile_ReturnUpdatedUserProfile(){
        when(userRepo.save(Mockito.any(Users.class))).thenReturn(testUser2);
        when(myCurrentUser.getPrincipalUser()).thenReturn(testUser2);
        when(myFactory.ProfilesFactory()).thenReturn(testProfile);
        when(restTemplate.getForObject(anyString(), eq(LeetCodeProfile.class)))
                .thenReturn(leetCodeData);
        userServicesImpl.updateUserProfile(testprofilesDTO);

        // Assert
        Profiles updatedProfile = testUser2.getProfile();
        assertNotNull(updatedProfile);

        // Fields from testprofilesDTO
        assertEquals(testprofilesDTO.getGithub_url(), updatedProfile.getGithub_url());
        assertEquals(testprofilesDTO.getProfile_photo_url(), updatedProfile.getProfile_photo_url());
        assertEquals(testprofilesDTO.getLeetcode_username(), updatedProfile.getLeetcode_username());

        // Fields from leetCodeData
        assertEquals(leetCodeData.getTotalSolved(), updatedProfile.getLeetcode_problems_solved());

        // Check user <-> profile linkage
        assertEquals(testUser2, updatedProfile.getUser());

    }

    @Test
    public void UserServices_deleteUser_ReturnDeletedUser(){
        when(myCurrentUser.getPrincipalUser()).thenReturn(testUser2);
        userServicesImpl.deleteUser(testUser2.getUsername());
    }

    @Test
    public void UserServices_addBugReport_ReturnAddedBugReport(){
        when(userRepo.save(Mockito.any(Users.class))).thenReturn(testUser2);
        when(myCurrentUser.getPrincipalUser()).thenReturn(testUser2);
        when(myFactory.BugReportsFactory()).thenReturn(bugReports);

        userServicesImpl.addBugReport(bugReportsDTO);

        assertEquals(1, testUser2.getReports().size());
    }


}
