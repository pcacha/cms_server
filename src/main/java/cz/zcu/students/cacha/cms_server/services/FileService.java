package cz.zcu.students.cacha.cms_server.services;

import org.apache.tika.Tika;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.Date;

import static cz.zcu.students.cacha.cms_server.assets_store_config.WebConfiguration.ARTICLES_FOLDER;

@Service
public class FileService {

    Tika tika = new Tika();

    public String detectType(byte[] fileArr) {
        return tika.detect(fileArr);
    }

    public String save(String documentName, String encodedDocument) throws IOException {
        String imageName = new Date().getTime() + "_" + documentName;
        byte[] decodedBytes = Base64.getMimeDecoder().decode(encodedDocument.replace("\n", "").trim());
        File target = new File(ARTICLES_FOLDER + "/" + imageName);
        FileUtils.writeByteArrayToFile(target, decodedBytes);
        return imageName;
    }

    public void delete(String name) {
        File target = new File(ARTICLES_FOLDER + "/" + name);
        if(target.exists()) {
            target.delete();
        }
    }
}
