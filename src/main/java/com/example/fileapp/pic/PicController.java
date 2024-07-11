package com.example.fileapp.pic;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.UUID;

@RequiredArgsConstructor
@Controller
public class PicController {

    private final PicRepository picRepository;

    @PostMapping("/upload")
    public String upload(PicRequest.UploadDTO requestDTO){
        // 1. 데이터 전달 받고
        String title = requestDTO.getTitle();
        MultipartFile imgFile = requestDTO.getImgFilename();

        // 2. 파일저장 위치 설정해서 파일을 저장 (UUID 붙여서 롤링)
        String imgFilename = UUID.randomUUID()+"_"+imgFile.getOriginalFilename();
        System.out.println("imgFilename = " + imgFilename);
        Path imgPath = Paths.get("./upload/"+imgFilename);
        try {
            Files.write(imgPath, imgFile.getBytes());

            // 3. DB에 저장 (title, realFileName)
            picRepository.insert(title, imgFilename);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return "redirect:/";
    }

    @GetMapping("/test/pic")
    @ResponseBody
    public ResponseEntity<?> testPic() {

        String imgFilename = "e0a1342d-0215-4d33-ba1e-e110c8c41d28_insideout.jpg";
//        System.out.println("filename = " + imgFilename);
//        System.out.println("mimeType = " + mimeType);
        
        Path imgPath = Paths.get("./upload/"+imgFilename);
//        System.out.println("imgPath = " + imgPath);

        try {
            byte[] fileBytes = Files.readAllBytes(imgPath);
//            String base64Encoded = Base64.getEncoder().encodeToString(fileBytes);
////            System.out.println("base64Encoded = " + base64Encoded);
//
////            base64Encoded = "data:$mimeType;base64,$base64Encoded"
////                    .replace("$mimeType", "jpg").replace("$base64Encoded", base64Encoded);
//
//            base64Encoded = "data:image/jpg;base64," + base64Encoded;
//
////            return base64Encoded;
//
//            //        디코딩
//            // 2. img parsing
//            Integer prefixEndIndex = base64Encoded.indexOf(",");
//            String img = base64Encoded.substring(prefixEndIndex+1);
////            System.out.println("img = " + img);
//
//            // 3. base64 decode to byte[]
//            byte[] imgBytes = Base64.getDecoder().decode(img);
//            System.out.println("imgBytes = " + imgBytes);
//            return imgBytes;

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(org.springframework.http.MediaType.IMAGE_JPEG);

            return ResponseEntity.ok().headers(headers).body(fileBytes);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

//    @GetMapping("/test/pic/decode")
//    public String testPicDecode(String base64Encoded) {
//        System.out.println("base64Encoded = " + base64Encoded);
//
//
//        return null;
//    }



    @GetMapping("/")
    public String index(){
        return "index";
    }

    @GetMapping("/uploadForm")
    public String uploadForm(){
        return "uploadForm";
    }

    @GetMapping("/uploadCheck")
    public String uploadCheck(HttpServletRequest request){
        Pic pic = picRepository.findById(1);
        request.setAttribute("pic", pic);
        return "uploadCheck";
    }
}








