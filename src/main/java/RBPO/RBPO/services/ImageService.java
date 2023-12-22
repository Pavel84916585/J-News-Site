package RBPO.RBPO.services;

import RBPO.RBPO.entity.Article;
import RBPO.RBPO.entity.Image;
import RBPO.RBPO.repositories.ImageRepository;
import RBPO.RBPO.utils.ImageUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.exception.ContextedRuntimeException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.zip.DataFormatException;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;

    public String uploadImage(MultipartFile imageFile, Article article) throws IOException {
        var imageToSave = Image.builder()
                .name(imageFile.getOriginalFilename())
                .contentType(imageFile.getContentType())
                .article(article)
                .bytes(ImageUtils.compressImage(imageFile.getBytes()))
                .build();

        imageRepository.save(imageToSave);
        return "Файл успешно загружен : " + imageFile.getOriginalFilename();
    }

    public List<String> downloadImage(Article article ) {
        List<Optional<Image>> dbImages = imageRepository.findByArticle(article);

        List<String> images = new ArrayList<>();

        for(Optional<Image> dbImage : dbImages)
        {
            images.add(
                    Base64.getEncoder().encodeToString(
                            dbImage.map(image -> {
                                try {
                                    return ImageUtils.decompressImage(image.getBytes());
                                } catch (DataFormatException | IOException exception) {
                                    throw new ContextedRuntimeException("Ошибка загрузки изображения", exception)
                                            .addContextValue("ID изображения",  image.getId());
                                }
                            }).orElse(null)
                    )
            );



        }

        return images;
    }

}