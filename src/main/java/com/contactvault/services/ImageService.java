package com.contactvault.services;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

    String uploadImage(MultipartFile contactImage, String fileName);
    String getURLFromPublicId(String publicId);

}
