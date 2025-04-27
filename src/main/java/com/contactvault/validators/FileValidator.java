package com.contactvault.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;


public class FileValidator implements ConstraintValidator<ValidFile, MultipartFile> {


    private static final long MAX_FILE_SIZE = 1024 * 1024 * 2;
//can check for
    //type
    //height
    //width

    @Override
    public boolean isValid(MultipartFile multipartFile, ConstraintValidatorContext constraintValidatorContext) {

        if(multipartFile == null || multipartFile.isEmpty()){
//            constraintValidatorContext.disableDefaultConstraintViolation();
//            constraintValidatorContext.buildConstraintViolationWithTemplate("File cannot be Empty")
//                    .addConstraintViolation();
            return true;
        }
        //file size
        if(multipartFile.getSize()>MAX_FILE_SIZE){
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate("File size should be less than 2MB")
                    .addConstraintViolation();
            return  false;
        }
        //resolution
//        try{
//            BufferedImage bufferedImage = ImageIO.read(multipartFile.getInputStream());
//            if(bufferedImage.getHeight())
//
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }

        return true;

    }
}
