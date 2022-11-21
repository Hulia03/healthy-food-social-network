package com.telerikacademy.healthy.food.social.network.services;

import com.telerikacademy.healthy.food.social.network.exceptions.FileStorageException;
import com.telerikacademy.healthy.food.social.network.repositories.CloudStorageImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;

import static com.telerikacademy.healthy.food.social.network.Factory.createMockMultipartFile;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;

@RunWith(MockitoJUnitRunner.class)
public class FilesServiceImplTests {
    @Mock
    CloudStorageImpl mockCloudStorage;

    @InjectMocks
    FilesServiceImpl mockService;

    @Test
    public void uploadFileShould_ReturnUrl() throws IOException {
        // Arrange
        String expected = "url";
        Mockito.when(mockCloudStorage.uploadFile("content".getBytes(), "image", 300)).thenReturn(expected);

        // Act
        String actual = mockService.uploadFile(createMockMultipartFile(), 300);

        // Assert
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void uploadFileShould_Throw_WhenFileNotExist() {
        // Arrange, Act, Assert
        Assert.assertThrows(FileStorageException.class,
                () -> mockService.uploadFile(null, anyInt()));
    }

    @Test
    public void getFileTypeShould_ReturnFileType() {

        // Arrange, Act, Assert
        Assert.assertEquals("image", mockService.getFileType(createMockMultipartFile()));
    }

}
