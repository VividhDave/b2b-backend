package com.divergent.mahavikreta.service.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import com.divergent.mahavikreta.entity.*;
import com.divergent.mahavikreta.repository.PlaceOrderRepository;
import com.divergent.mahavikreta.repository.ProductRepository;
import com.divergent.mahavikreta.repository.SubCategoryRepository;
import com.itextpdf.text.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;

import com.divergent.mahavikreta.repository.PlaceOrderProductRepository;
import com.divergent.mahavikreta.service.InvoiceHistoryService;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class InvoiceHistoryServiceImpl implements InvoiceHistoryService {

	@Autowired
	private PlaceOrderProductRepository placeOrderProductRepository;

	@Autowired
	private PlaceOrderRepository placeOrderRepository;

	@Autowired
	private SubCategoryRepository subCategoryRepository;

	@Autowired
	private ProductRepository productRepository;

	@Override
	public InputStreamResource downloadInvoiceHistory(Long orderid) throws IOException {
		List<PlaceOrderProduct> placeOrderProducts = placeOrderProductRepository
				.findbyPlaceOrderProductByOrderId(orderid);
		ByteArrayInputStream bis = invoicePdfHistory(placeOrderProducts);
		return new InputStreamResource(bis);
	}

	private ByteArrayInputStream invoicePdfHistory(List<PlaceOrderProduct> products) {
		Double grandTotal = 0.0;
		Document document = new Document();
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		try {

			Font font = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, BaseColor.BLACK);
			Font font2 = FontFactory.getFont(FontFactory.TIMES_ROMAN, 26, BaseColor.BLACK);
			Font font3 = FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, BaseColor.BLACK);

			PdfWriter.getInstance(document, out);
			document.open();
			// Add Text to PDF file ->
			Font font1 = FontFactory.getFont(FontFactory.TIMES_ROMAN, 26, BaseColor.BLACK);

//--------------------------LOGO---------------------------------------------

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
//
//			Paragraph paragraph1 = new Paragraph("GA TRADE LINKS PRIVATE LIMITED,\n" +
//					"UG 09 Highway Tower Building , Navlakha Square Indore, Madhya Pradesh, 452001, India\n" +
//					"CIN : U52609MP2016PTC041247\n" +
//					"GSTIN: 23AAGCG4755M1ZB", font3);
//			paragraph1.setAlignment(Element.ALIGN_RIGHT);
//			document.add(paragraph1);

			//--------------------------HEADER---------------------------------------------

			Paragraph header = new Paragraph("Invoice", font2);
			header.setAlignment(Element.ALIGN_CENTER);
			document.add(header);

			Long orderId = null;
			Date date = new Date();
			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
			String strDate = format.format(date);
			 Long productId = null;

			for (PlaceOrderProduct pop : products) {
				orderId = pop.getPlaceOrder().getId();
				productId = pop.getProduct().getId();
			}

			Paragraph info = new Paragraph("----------------------------------------------------------------------------------------------------------------------------------\nDate ::" + strDate + "\n" + "Order Id ::" + " " + orderId +"\n----------------------------------------------------------------------------------------------------------------------------------", font);
			info.setAlignment(Element.ALIGN_LEFT);
			document.add(info);

			Optional<PlaceOrder> placeOrder = this.placeOrderRepository.findByOrderId(orderId);
            Product product = this.productRepository.findProductById(productId);
			//----------------------------------------------CALCULATION FOR GST---------------------------------------------------------
//          Double gst = 1.18;
//			Double gst2 =0.18;
//			Double amountWithoutGst = placeOrder.get().getTotalSaleAmount()/gst;
//			Double gstAmount = amountWithoutGst*gst2;
//			Double sgst = gstAmount/2;
//			Double cgst = gstAmount/2;
//			DecimalFormat df = new DecimalFormat("0.00");
//			String taxes = "SGST: 9%" + "\nCGST: 9%";
//			String igst = "IGST: 18%";
			SubCategory subCategory = subCategoryRepository.findSubCategoryById(product.getSubCategory().getId());
			DecimalFormat df = new DecimalFormat("0.00");
			Double gst1 = (subCategory.getGst()+100)/100; //1.18
			Double gst2 = (subCategory.getGst()/100);     //0.18

			Double amountWithoutGst = placeOrder.get().getTotalSaleAmount()/gst1;
			Double gstAmount = amountWithoutGst*gst2;
			Double sgst = gstAmount/2;
			Double cgst = gstAmount/2;

			String taxes = "SGST:"+df.format(sgst)+"%" + "\nCGST:"+df.format(cgst)+"%";
			String igst = "IGST:"+subCategory.getGst()+"%";

			//-------------------------USER DETAILS----------------------------------------
			User user = placeOrder.get().getUser();
			UserAddress userAddress = placeOrder.get().getDeliveryAddress();

			String firstName = user.getFirstName();
			String lastName = user.getLastName();
            Paragraph info1 = new Paragraph("\nTo: "+firstName+" "+lastName);
			info1.setAlignment(Element.ALIGN_LEFT);
			document.add(info1);

			Paragraph info3 = new Paragraph("\n");
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

			String ad=userAddress.getAddress();
			String city=userAddress.getCity();
			Paragraph info7 = new Paragraph("\nAddress:"+ad+", "+city);
			info7.setAlignment(Element.ALIGN_LEFT);
			document.add(info7);
			document.add(Chunk.NEWLINE);
			//-----------------------------TABLE--------------------------------------------
			float columnWidths[] = {200f, 150f, 100f, 100f, 100f, 150f, 100f, 100f};
			PdfPTable table = new PdfPTable(columnWidths);
			table.setWidthPercentage(100);
			//table.size();
			// Add PDF Table Header ->
			Stream.of("Product Name", "Ordered Date", "Unit Price","Quantity","Net Amount","GST","Tax Amount","Total Price").forEach(headerTitle -> {
				PdfPCell tableHeader = new PdfPCell();
				Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
				tableHeader.setBackgroundColor(BaseColor.LIGHT_GRAY);
				tableHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
				tableHeader.setBorderWidth(2);
				tableHeader.setPhrase(new Phrase(headerTitle, headFont));
				table.addCell(tableHeader);
			});

			for (PlaceOrderProduct placeOrderProduct : products) {
                //-----------------PRODUCT NAME-------------------
				PdfPCell productName = new PdfPCell(new Phrase(placeOrderProduct.getProduct().getProductName()));
				productName.setPaddingLeft(2);
				productName.setVerticalAlignment(Element.ALIGN_MIDDLE);
				productName.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(productName);
                //-----------------DATE----------------------
				Date createdDate = placeOrderProduct.getCreatedDate();
				String strOrderedDate = format.format(createdDate);
				PdfPCell orderedDate = new PdfPCell(new Phrase(String.valueOf(strOrderedDate)));
				orderedDate.setPaddingLeft(2);
				orderedDate.setVerticalAlignment(Element.ALIGN_MIDDLE);
				orderedDate.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(orderedDate);
				//--------------UNIT PRICE----------------
				Double unitPriceAmount = placeOrderProduct.getProduct().getDiscountPrice()/gst1;
				PdfPCell unitPrice = new PdfPCell(new Phrase(df.format(unitPriceAmount)));
				unitPrice.setPaddingLeft(4);
				unitPrice.setVerticalAlignment(Element.ALIGN_MIDDLE);
				unitPrice.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(unitPrice);
                //----------QUANTITY------------------
				PdfPCell quantity = new PdfPCell(new Phrase(String.valueOf(placeOrderProduct.getQty())));
				quantity.setPaddingLeft(2);
				quantity.setVerticalAlignment(Element.ALIGN_MIDDLE);
				quantity.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(quantity);

				//------------NET AMOUNT------------
				PdfPCell netAmount = new PdfPCell(new Phrase(df.format(amountWithoutGst)));
				netAmount.setPaddingLeft(4);
				netAmount.setVerticalAlignment(Element.ALIGN_MIDDLE);
				netAmount.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(netAmount);
				//-------------------GST------------------------
				String checkGst = user.getGstin();
				if (checkGst.startsWith("23")){

					PdfPCell GST = new PdfPCell(new Phrase((taxes)));
					GST.setPaddingLeft(0);
					GST.setPaddingRight(0);
					GST.setVerticalAlignment(Element.ALIGN_MIDDLE);
					GST.setHorizontalAlignment(Element.ALIGN_CENTER);
					table.addCell(GST);
					//------------------TAX AMOUNT-----------------
					PdfPCell taxAmount = new PdfPCell(new Phrase(df.format((gstAmount))));
					taxAmount.setPaddingLeft(4);
					taxAmount.setVerticalAlignment(Element.ALIGN_MIDDLE);
					taxAmount.setHorizontalAlignment(Element.ALIGN_CENTER);
					table.addCell(taxAmount);

				}else{

					PdfPCell GST = new PdfPCell(new Phrase(String.valueOf(igst)));
					GST.setPaddingLeft(0);
					GST.setPaddingRight(0);
					GST.setVerticalAlignment(Element.ALIGN_MIDDLE);
					GST.setHorizontalAlignment(Element.ALIGN_CENTER);
					table.addCell(GST);
					//------------------TAX AMOUNT-----------------
					PdfPCell taxAmount = new PdfPCell(new Phrase(df.format(gstAmount)));
					taxAmount.setPaddingLeft(4);
					taxAmount.setVerticalAlignment(Element.ALIGN_MIDDLE);
					taxAmount.setHorizontalAlignment(Element.ALIGN_CENTER);
					table.addCell(taxAmount);
				}

                //----------------TOTAL AMOUNT------------------
				PdfPCell totalPrice = new PdfPCell(
						new Phrase(String.valueOf(placeOrder.get().getTotalSaleAmount())));
				totalPrice.setPaddingLeft(4);
				totalPrice.setVerticalAlignment(Element.ALIGN_MIDDLE);
				totalPrice.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(totalPrice);

				Double total = placeOrderProduct.getPrice() * placeOrderProduct.getQty();
				grandTotal += total;

			}

			Stream.of("", "", "", "Grand Total", String.valueOf(grandTotal)).forEach(footerTitle -> {
				PdfPCell tableFooter = new PdfPCell();
				Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
				tableFooter.setHorizontalAlignment(Element.ALIGN_CENTER);
				tableFooter.setBorderWidth(2);
				tableFooter.setPhrase(new Phrase(footerTitle, headFont));
				table.addCell(tableFooter);
			});

			document.add(table);
			document.add(Chunk.NEWLINE);

			Paragraph paragraph = new Paragraph("Thanks and Regards,\nMahavikreta Team\nEmail : notification@mahavikreta.com\nContact: +918629933012", font);
			paragraph.setAlignment(Element.ALIGN_LEFT);
			document.add(paragraph);

			document.close();
		} catch (DocumentException e) {
			log.error(e.toString());
		}

		return new ByteArrayInputStream(out.toByteArray());
	}


}
