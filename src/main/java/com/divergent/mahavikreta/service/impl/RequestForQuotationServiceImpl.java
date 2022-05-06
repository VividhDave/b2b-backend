package com.divergent.mahavikreta.service.impl;

import com.divergent.mahavikreta.constants.MsgConstants;
import com.divergent.mahavikreta.entity.PlaceOrder;
import com.divergent.mahavikreta.entity.RequestForQuotation;
import com.divergent.mahavikreta.entity.User;
import com.divergent.mahavikreta.exception.GenricException;
import com.divergent.mahavikreta.repository.RequestForQuotationRepository;
import com.divergent.mahavikreta.repository.UserRepository;
import com.divergent.mahavikreta.service.LogService;
import com.divergent.mahavikreta.service.RequestForQuotationService;
import com.itextpdf.text.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.query.criteria.internal.predicate.IsEmptyPredicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
@Slf4j
public class RequestForQuotationServiceImpl implements RequestForQuotationService {

	@Autowired
	public RequestForQuotationRepository quotationRepository;

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	LogService logService;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public RequestForQuotation saveQuotationRequest(RequestForQuotation quotation) {
		if(quotationRepository.searchByUserIdAndProductId(quotation.getProduct().getId(),quotation.getUser().getId())  == null) {
			quotation.setRfqCode(generateRfqCode());
			quotation.setStatus(true);
			return quotationRepository.save(quotation);
		}
		return null;
	}

	public String generateRfqCode() {
		String code = quotationRepository.generateRfqCode();
		if (code != null) {
			int value = Integer.parseInt(code) + 1;
			code = "RFQ/" + LocalDate.now().getYear() + "/" + value;
		} else {
			code = "RFQ/" + LocalDate.now().getYear() + "/1001";
		}
		return code;
	}

	@Override
	public InputStreamResource quotationReport(Long productId, Long userId) {
		try {
		List<RequestForQuotation> quotations = (List<RequestForQuotation>) quotationRepository
				.getQuotationByProduct(productId);
		User users = userRepository.findUserById(userId);
		ByteArrayInputStream bis = quotationPdfReport(quotations, users);
		return new InputStreamResource(bis);
		}catch(Exception ex) {
			logService.saveErrorLog("Error on Generating quotation Report", "RequestForQuotationController",
					"quotationReport", ex.getMessage());
			throw new GenricException("Error on Generating quotation Report-" + ex.getMessage());
		}
	}

	private ByteArrayInputStream quotationPdfReport(List<RequestForQuotation> quotations, User user) {
		Document document = new Document();
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		try {

			PdfWriter.getInstance(document, out);
			document.open();

			// Add Text to PDF file ->
			Font font1 = FontFactory.getFont(FontFactory.TIMES_ROMAN, 26, BaseColor.BLACK);
			Font font = FontFactory.getFont(FontFactory.TIMES_ROMAN, 18, BaseColor.BLACK);
			Font font3 = FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, BaseColor.BLACK);

			Paragraph header1 = new Paragraph("img");
			header1.setAlignment(Element.ALIGN_LEFT);
			Image img = null;
			try {
				img = Image.getInstance("/home/ubuntu/mahavikreta/logo/mahavikreta_logo.png");
			} catch (IOException e) {
				e.printStackTrace();
			}
			img.scaleToFit(500f, 500f);
			document.add(img);

//			Paragraph headerHeading = new Paragraph("Mahavikreta\n", font1);
//			headerHeading.setAlignment(Element.ALIGN_RIGHT);
//			document.add(headerHeading);

//			Paragraph paragraph1 = new Paragraph("GA TRADE LINKS PRIVATE LIMITED,\n" +
//					"UG 09 Highway Tower Building , Navlakha Square Indore, Madhya Pradesh, 452001, India\n" +
//					"CIN : U52609MP2016PTC041247\n" +
//					"GSTIN: 23AAGCG4755M1ZB", font3);
//			paragraph1.setAlignment(Element.ALIGN_RIGHT);
//			document.add(paragraph1);

			Paragraph header = new Paragraph("Quotation", font1);
			header.setAlignment(Element.ALIGN_CENTER);
			document.add(header);

			Date date = new Date();
			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
			String strDate = format.format(date);

			//----------------------user details------------------------------------------

			Paragraph info = new Paragraph("-----------------------------------------------------------------------------------\nDate: "+strDate + "\nDear User: " + user.getFirstName() + " " + user.getLastName(), font);
			info.setAlignment(Element.ALIGN_LEFT);
			document.add(info);

			Paragraph info3 = new Paragraph("\nShop Name:");
			info3.setAlignment(Element.ALIGN_LEFT);
			info3.add(user.getShopName());
			document.add(info3);

			Paragraph info4 = new Paragraph("\nGST Number:");
			info4.setAlignment(Element.ALIGN_LEFT);
			info4.add(user.getGstin());
			document.add(info4);

			Paragraph info5 = new Paragraph("\nEmail:");
			info5.setAlignment(Element.ALIGN_LEFT);
			info5.add(user.getEmail());
			document.add(info5);

			Paragraph info6 = new Paragraph("\nMobile Number:");
			info6.setAlignment(Element.ALIGN_LEFT);
			info6.add(user.getWhatsappMobileNumber());
			document.add(info6);

			Paragraph para = new Paragraph(
					"\nEnclosed is a request for quotation services. Therefore,"
							+ "we would appreciate a quote from you on the items listed below:",
					font);
			para.setAlignment(Element.ALIGN_LEFT);
			document.add(para);
			document.add(Chunk.NEWLINE);

			float columnWidths[] = {120f, 170f, 100f, 100f, 100f, 150f};
			PdfPTable table = new PdfPTable(columnWidths);
			table.setWidthPercentage(100);
			// Add PDF Table Header ->
			Stream.of("Quotation Code", "Product Name", "Preferred Quantity", "Unit Price", "Total", "Details")
					.forEach(headerTitle -> {
						PdfPCell tableHeader = new PdfPCell();
						Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
						tableHeader.setBackgroundColor(BaseColor.LIGHT_GRAY);
						tableHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
						tableHeader.setBorderWidth(2);
						tableHeader.setPhrase(new Phrase(headerTitle, headFont));
						table.addCell(tableHeader);
					});

			for (RequestForQuotation quotation : quotations) {

				PdfPCell rfqCode = new PdfPCell(new Phrase(quotation.getRfqCode()));
				rfqCode.setPaddingLeft(4);
				rfqCode.setVerticalAlignment(Element.ALIGN_MIDDLE);
				rfqCode.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(rfqCode);

				PdfPCell productName = new PdfPCell(new Phrase(quotation.getProduct().getProductName()));
				productName.setPaddingLeft(4);
				productName.setVerticalAlignment(Element.ALIGN_MIDDLE);
				productName.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(productName);

//				PdfPCell minQuantity = new PdfPCell(new Phrase(String.valueOf(quotation.getMinQuantity())));
//				minQuantity.setPaddingLeft(4);
//				minQuantity.setVerticalAlignment(Element.ALIGN_MIDDLE);
//				minQuantity.setHorizontalAlignment(Element.ALIGN_RIGHT);
//				table.addCell(minQuantity);

				PdfPCell maxQuantity = new PdfPCell(new Phrase(String.valueOf(quotation.getMaxQuantity())));
				maxQuantity.setPaddingLeft(2);
				maxQuantity.setVerticalAlignment(Element.ALIGN_MIDDLE);
				maxQuantity.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(maxQuantity);

				PdfPCell preferredUnitPrice = new PdfPCell(new Phrase(String.valueOf(quotation.getProduct().getDiscountPrice())));
				preferredUnitPrice.setPaddingLeft(2);
				preferredUnitPrice.setVerticalAlignment(Element.ALIGN_MIDDLE);
				preferredUnitPrice.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(preferredUnitPrice);

				Double TotalAmount = quotation.getMaxQuantity() * quotation.getProduct().getDiscountPrice();
				PdfPCell total = new PdfPCell(new Phrase(String.valueOf(TotalAmount)));
				total.setPaddingLeft(2);
				total.setVerticalAlignment(Element.ALIGN_MIDDLE);
				total.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(total);

				PdfPCell details = new PdfPCell(new Phrase(quotation.getDetails()));
				details.setPaddingLeft(2);
				details.setVerticalAlignment(Element.ALIGN_MIDDLE);
				details.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(details);
			}
			document.add(table);
			document.add(Chunk.NEWLINE);

			Paragraph paragraph2 = new Paragraph("Thanks and Regards,\nMahavikreta Team\nEmail : notification@mahavikreta.com\nContact: +918629933012", font);
			paragraph2.setAlignment(Element.ALIGN_LEFT);
			document.add(paragraph2);

			document.close();
		} catch (DocumentException e) {
			log.error(e.toString());
			log.error(e.getMessage());
			throw new GenricException("Error on Generating quotation Report-" + e.getMessage());
		}

		return new ByteArrayInputStream(out.toByteArray());
	}
}
