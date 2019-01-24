package com.web.multifactor.controller;

import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.web.multifactor.service.ExcelBulkReadService;

import java.io.File;

@Controller
@RequestMapping("/file")
public class FileUploadController {
	
	@Autowired
	ExcelBulkReadService excelBulkReadService;
	
	@RequestMapping(value="/excel/xlsxUpload", method=RequestMethod.POST)
	@ResponseBody 
	public String fileUp(MultipartHttpServletRequest multi) {
        
        // 저장 경로 설정
        String root = multi.getSession().getServletContext().getRealPath("/");
        String path = root+"resources/upload/";         
        String newFileName = ""; // 업로드 되는 파일명
         
        java.io.File dir = new java.io.File(path);
        if(!dir.isDirectory()){
            dir.mkdir();
        }
         
        Iterator<String> files = multi.getFileNames();
        while(files.hasNext()){
            String uploadFile = files.next();
            
            MultipartFile mFile = multi.getFile(uploadFile);
            String fileName = mFile.getOriginalFilename();
            System.out.println("실제 파일 이름 : " +fileName);
            newFileName = System.currentTimeMillis()+"."
                    +fileName.substring(fileName.lastIndexOf(".")+1);
             
            try {
                mFile.transferTo(new File(path+newFileName));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
         
        System.out.println("id : " + multi.getParameter("id"));
        System.out.println("pw : " + multi.getParameter("pw"));
        
        return "ajaxUpload";
    }


	public String testOk(@RequestPart MultipartFile[] file) {
		System.out.println(file[0].getOriginalFilename());
		return "";
	}
}
