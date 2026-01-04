package pt.psoft.g1.psoftg1.shared.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pt.psoft.g1.psoftg1.shared.api.UploadFileResponse;
import pt.psoft.g1.psoftg1.shared.services.FileStorageService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FileUtilsTest {

    @Mock
    FileStorageService fileStorageService;

    @Mock
    MultipartFile multipartFile;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    // ----------------- Happy Path -----------------
    @Test
    void doUploadFile_success() throws Exception {
        FileStorageService fileStorageService = mock(FileStorageService.class);
        MultipartFile file = mock(MultipartFile.class);

        when(file.getContentType()).thenReturn("image/jpeg");
        when(file.getSize()).thenReturn(1024L);
        when(fileStorageService.storeFile(anyString(), eq(file))).thenReturn("test.jpg");

        // Mock estático do ServletUriComponentsBuilder
        try (MockedStatic<ServletUriComponentsBuilder> mockedBuilder = mockStatic(ServletUriComponentsBuilder.class)) {
            ServletUriComponentsBuilder builder = mock(ServletUriComponentsBuilder.class);
            when(builder.pathSegment("test.jpg")).thenReturn(builder);
            when(builder.toUriString()).thenReturn("http://localhost/photo/test.jpg");
            mockedBuilder.when(ServletUriComponentsBuilder::fromCurrentRequestUri).thenReturn(builder);

            UploadFileResponse response = FileUtils.doUploadFile(fileStorageService, "123", file);

            assertEquals("http://localhost/photo/test.jpg", response.getFileDownloadUri());
        }
    }

    // ----------------- Null Checks -----------------
    @Test
    void doUploadFile_nullFileStorageService_throwsException() {
        Exception exception = assertThrows(Exception.class,
                () -> FileUtils.doUploadFile(null, "123", multipartFile));
        assertEquals("Could not get reference of fileStorageService, id or file", exception.getMessage());
    }

    @Test
    void doUploadFile_nullId_throwsException() {
        Exception exception = assertThrows(Exception.class,
                () -> FileUtils.doUploadFile(fileStorageService, null, multipartFile));
        assertEquals("Could not get reference of fileStorageService, id or file", exception.getMessage());
    }

    @Test
    void doUploadFile_nullFile_throwsException() {
        Exception exception = assertThrows(Exception.class,
                () -> FileUtils.doUploadFile(fileStorageService, "123", null));
        assertEquals("Could not get reference of fileStorageService, id or file", exception.getMessage());
    }

    // ----------------- URI Manipulation Check -----------------
    @Test
    void doUploadFile_mockServletUri() throws Exception {
        FileStorageService fileStorageService = mock(FileStorageService.class);
        MultipartFile file = mock(MultipartFile.class);
        when(file.getContentType()).thenReturn("image/jpeg");
        when(file.getSize()).thenReturn(1024L);
        when(fileStorageService.storeFile(anyString(), eq(file))).thenReturn("photo.jpg");

        try (MockedStatic<ServletUriComponentsBuilder> mockedStatic = mockStatic(ServletUriComponentsBuilder.class)) {
            ServletUriComponentsBuilder builder = mock(ServletUriComponentsBuilder.class);
            when(builder.pathSegment("photo.jpg")).thenReturn(builder);
            when(builder.toUriString()).thenReturn("http://localhost/photo/photo.jpg");
            mockedStatic.when(ServletUriComponentsBuilder::fromCurrentRequestUri).thenReturn(builder);

            UploadFileResponse response = FileUtils.doUploadFile(fileStorageService, "123", file);

            assertEquals("http://localhost/photo/photo.jpg", response.getFileDownloadUri());
            assertEquals("photo.jpg", response.getFileName());
        }
    }
}
