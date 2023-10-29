package com.example.uploadfileservice;

import com.example.uploadfileservice.enumeration.Progress;
import com.example.uploadfileservice.handler.UploadFileService;
import com.example.uploadfileservice.model.entity.FileEntity;
import com.example.uploadfileservice.repository.FileRepository;
import com.example.uploadfileservice.service.FileService;
import com.example.uploadfileservice.service.impl.FileServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

import static org.mockito.Mockito.when;

@SpringBootTest
class UploadFileServiceApplicationTests {

	@InjectMocks
	FileService fileService = new FileServiceImpl();

	@Mock
	FileRepository fileRepository;

	@BeforeEach
	public void initial() throws NoSuchMethodException {
		FileEntity fakeEntity = new FileEntity();
		fakeEntity.setFileName("test_file.xlsx");
		fakeEntity.setStatus(Progress.IN_PROGRESS.name());
		fakeEntity.setFileType("xlsx");
		fakeEntity.setLastAccessBy("System");
//		fakeEntity.setLastAccessTime(LocalDateTime.now());
//		fakeEntity.setCreatedDate(LocalDateTime.now());
		fakeEntity.setId(1L);
		when(fileRepository.saveAndFlush(fakeEntity)).thenReturn(fakeEntity);
	}

	@Test
	void contextLoads() throws IOException {
		Path path = Paths.get("src/test/resources/test_file.xlsx");
		String name = "test_file.xlsx";
		String originalFileName = "test_file.xlsx";
		String contentType = "text/plain";
		byte[] content = Files.readAllBytes(path);
		MultipartFile multipartFile = new MockMultipartFile(name,
				originalFileName, contentType, content);
		fileService.uploadFile(multipartFile);
	}

}
