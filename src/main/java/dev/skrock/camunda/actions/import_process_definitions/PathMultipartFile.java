/*
  ------------------------------------------------------------------------------
        (c) by data experts gmbh
              Postfach 1130
              Woldegker Str. 12
              17001 Neubrandenburg

  Dieses Dokument und die hierin enthaltenen Informationen unterliegen
  dem Urheberrecht und duerfen ohne die schriftliche Genehmigung des
  Herausgebers weder als ganzes noch in Teilen dupliziert oder reproduziert
  noch manipuliert werden.
*/

package dev.skrock.camunda.actions.import_process_definitions;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

import org.apache.commons.io.FileUtils;
import org.springframework.http.MediaType;

import lombok.AllArgsConstructor;

/**
 * <Beschreibung>
 * <br>
 *
 * @author askrock
 */
@AllArgsConstructor
public class PathMultipartFile implements org.springframework.web.multipart.MultipartFile {

    private final Path path;

    private final MediaType mediaType;

    @Override
    public String getName() {
        return "file";
    }

    @Override
    public String getOriginalFilename() {
        return path.getFileName().toString();
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
        return path.toFile().length();
    }

    @Override
    public byte[] getBytes() throws IOException {
        return FileUtils.readFileToByteArray(path.toFile());
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new BufferedInputStream(new FileInputStream(path.toFile()));
    }

    @Override
    public void transferTo(File dest) throws IOException, IllegalStateException {
        FileUtils.copyToFile(getInputStream(), dest);
    }
}
