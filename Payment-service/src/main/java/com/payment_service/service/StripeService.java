package com.payment_service.service;

import com.payment_service.dto.ProductRequest;
import com.payment_service.dto.StripeResponse;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class StripeService {
    
    @Value("${stripe.secretKey}")
    private String secretKey;


    //-ProductRequest : productName , amount , quantity , currency
    //-StripeResponse: return sessionId and url

    public StripeResponse checkoutProducts(ProductRequest productRequest){
        Stripe.apiKey=secretKey;

        // Create a PaymentIntent(Generating bill) with the order amount and currency
        SessionCreateParams.LineItem.PriceData.ProductData productData=
                SessionCreateParams.LineItem.PriceData.ProductData.builder()
                        .setName(productRequest.getName())
                        .build();

        // Create new line item with the above product data and associated price
        SessionCreateParams.LineItem.PriceData priceData=
                SessionCreateParams.LineItem.PriceData.builder()
                        .setCurrency(
                                productRequest.getCurrency() != null
                                        ? productRequest.getCurrency() : "USD"
                                )
                        .setUnitAmount((long) productRequest.getAmount())
                        .setProductData(productData)
                        .build();

        // Create new line item with the above price data
        SessionCreateParams.LineItem lineItem=
                SessionCreateParams
                        .LineItem.builder()
                        .setQuantity(productRequest.getQuantity())
                        .setPriceData(priceData)
                        .build();

        // Create new session with the line items
        SessionCreateParams params=
                SessionCreateParams.builder()
                        .setMode(SessionCreateParams.Mode.PAYMENT)
                        .setSuccessUrl("http://localhost:8080/product/v1/success?session_id={CHECKOUT_SESSION_ID}")
                        .setCancelUrl("http://localhost:8080/cancel")
                        .putMetadata(
                                "bookingId",
                                String.valueOf(productRequest.getBookingId())
                        )
                        .addLineItem(lineItem)
                        .build();

        // Create new session
        Session session=null;
        try{
            session=Session.create(params);

            StripeResponse response = new StripeResponse();

            response.setStatus("SUCCESS");
            response.setMessage("Payment session created");
            response.setSessionId(session.getId());
            response.setSessionUrl(session.getUrl());
            return response;
        }catch (StripeException e){
            e.printStackTrace();
            // log the error
//            StripeResponse response = new StripeResponse();
//            response.setStatus("FAILED");
//            response.setMessage("Payment session creation failed");
//
//            return response;
        }
        return null;
    }
}





















