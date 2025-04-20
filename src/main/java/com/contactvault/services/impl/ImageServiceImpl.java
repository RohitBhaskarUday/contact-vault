package com.contactvault.services.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import com.contactvault.helpers.Constants;
import com.contactvault.services.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class ImageServiceImpl implements ImageService {

    private final Cloudinary cloudinary;

    @Autowired
    ImageServiceImpl(Cloudinary cloudinary){
        this.cloudinary = cloudinary;
    }

    @Override
    public String uploadImage(MultipartFile contactImage, String fileName) {
        try{
            //upload the image to the cloud.
            byte[] data = new byte[contactImage.getInputStream().available()];
            contactImage.getInputStream().read(data);
            cloudinary.uploader().upload(data, ObjectUtils.asMap(
                    "public_id", fileName ));
            // return the url from the cloud.
            return this.getURLFromPublicId(fileName);
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getURLFromPublicId(String publicId) {
        return cloudinary
                .url()
                .transformation(
                        new Transformation()
                                .width(Constants.CONTACT_IMAGE_WIDTH.getIntValue())
                                .height(Constants.CONTACT_IMAGE_HEIGHT.getIntValue())
                                .crop(Constants.CONTACT_IMAGE_CROP.getStringValue())
                )
                .generate(publicId);
    }


}
