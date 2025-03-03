package dev.skrock.camunda.toolkit.api;

import dev.skrock.camunda.toolkit.util.ResponseException;
import dev.skrock.camunda.toolkit.util.ResponseUtil;
import lombok.AllArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.camunda.community.rest.client.api.DeploymentApi;
import org.camunda.community.rest.client.model.DeploymentWithDefinitionsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@Service
public class DeploymentService {

    private final DeploymentApi deploymentApi;

    @Autowired
    public DeploymentService(DeploymentApi deploymentApi) {
        this.deploymentApi = deploymentApi;
    }

    public void deployDiagram(String fileName, byte[] content) throws ResponseException {
        ResponseEntity<DeploymentWithDefinitionsDto> deploymentResponse = deploymentApi.createDeployment(
                null,
                "camunda-toolkit",
                true,
                true,
                null,
                null,
                new MultipartFile[] {
                        new ByteArrayMultipartFile(StringUtils.appendIfMissing(fileName, ".bpmn"), MediaType.APPLICATION_XML, content)
                }
        );

        ResponseUtil.checkResponse(deploymentResponse);
    }

    @AllArgsConstructor
    private static class ByteArrayMultipartFile implements MultipartFile {

        private final String name;
        private final MediaType mediaType;
        private final byte[] content;

        @Override
        public String getName() {
            return "file";
        }

        @Override
        public String getOriginalFilename() {
            return name;
        }

        @Override
        public String getContentType() {
            return mediaType.toString();
        }

        @Override
        public boolean isEmpty() {
            return getSize() <= 0;
        }

        @Override
        public long getSize() {
            return getBytes().length;
        }

        @Override
        public byte[] getBytes() {
            return content;
        }

        @Override
        public InputStream getInputStream() {
            return new ByteArrayInputStream(getBytes());
        }

        @Override
        public void transferTo(File dest) throws IOException {
            try (InputStream in = getInputStream()) {
                FileUtils.copyToFile(in, dest);
            }
        }
    }
}
