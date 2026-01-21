package portfolio.backend.service;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import portfolio.backend.dto.ContactRequest;

import java.io.File;
import java.io.FileOutputStream;

@Service
public class ContactService {

    private final JavaMailSender mailSender;

    public ContactService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void handleContact(ContactRequest request) {
        try {
            //saveToExcel(request);
            //sendEmail(request);
            oupt();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Contact processing failed");
        }
    }

    private String oupt() {
        return "I am here";
    }

    private synchronized void saveToExcel(ContactRequest request) {

        File file = new File(System.getProperty("user.home"), "contacts.xlsx");
        File tempFile = new File(System.getProperty("user.home"), "contacts_tmp.xlsx");

        try {
            Workbook workbook;

            if (file.exists() && file.length() > 0) {
                workbook = WorkbookFactory.create(file);
            } else {
                workbook = new XSSFWorkbook();
            }

            Sheet sheet = workbook.getSheet("Contacts");
            if (sheet == null) {
                sheet = workbook.createSheet("Contacts");
                Row header = sheet.createRow(0);
                header.createCell(0).setCellValue("Name");
                header.createCell(1).setCellValue("Email");
                header.createCell(2).setCellValue("Message");
            }

            int rowIndex = sheet.getLastRowNum() + 1;
            Row row = sheet.createRow(rowIndex);
            row.createCell(0).setCellValue(request.getName());
            row.createCell(1).setCellValue(request.getEmail());
            row.createCell(2).setCellValue(request.getMessage());

            // 🔑 WRITE TO TEMP FILE FIRST
            try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                workbook.write(fos);
                fos.flush();
            }

            workbook.close();

            // 🔑 ATOMIC REPLACE
            if (file.exists()) {
                file.delete();
            }
            tempFile.renameTo(file);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to save contact to Excel", e);
        }
    }
    private void sendEmail(ContactRequest request) {
        SimpleMailMessage mail = new SimpleMailMessage();

        mail.setTo("panishita21@gmail.com");
        mail.setFrom("panishita21@gmail.com"); // REQUIRED
        //mail.setReplyTo(request.getEmail());   // OPTIONAL but good

        mail.setSubject("New Contact Message From Your Site");

        mail.setText(
                "Name: " + request.getName() + "\n" +
                        "Email: " + request.getEmail() + "\n\n" +
                        request.getMessage()
        );

        mailSender.send(mail);
    }

}
