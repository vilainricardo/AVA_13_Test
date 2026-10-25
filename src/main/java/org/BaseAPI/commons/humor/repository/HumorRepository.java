package org.BaseAPI.commons.humor.repository;

import org.BaseAPI.commons.humor.entity.Humor;
import org.BaseAPI.erros.exceptions.WriteFileException;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.format.DateTimeFormatter;

@Repository
public class HumorRepository {

    public boolean addHumor(Humor humor) throws Exception {

        Path path = Paths.get(getFilePath());
        Files.createDirectories(path);
        String lineInfo =getLineInfo(humor);
        Files.writeString(path, lineInfo, StandardOpenOption.CREATE, StandardOpenOption.APPEND);

        try(PrintWriter pw = new PrintWriter(Files.newBufferedWriter(file, StandardCharsets.UTF_8, options))){
            pw.println(lineInfo);
        }
        catch (Exception e){
            throw new WriteFileException("Unable to write file.");
        }
        return true;
    }

    private String getFileName(Humor humor){
        return "humor_%s.txt".formatted(humor.getUser().getId());
    }

    private String getFilePath(){
        return "humor";
    }

    private String getLineInfo(Humor humor){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return "%s;%s;%s\n".formatted(humor.getId(), humor.getHumor(),  humor.getHumorAt().format(formatter));
    }
}
