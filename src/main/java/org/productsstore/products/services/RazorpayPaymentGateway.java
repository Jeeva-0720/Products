package org.productsstore.products.services;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("razorpayPaymentGateway")
public class RazorpayPaymentGateway implements PaymentService {

    RazorpayClient razorpayClient;

    @Autowired
    public RazorpayPaymentGateway (RazorpayClient razorpayClient){
        this.razorpayClient = razorpayClient;
    }

    @Override
    public String generatePaymentLink(Long orderId, Long amount) throws RazorpayException {

        JSONObject paymentLinkRequest = new JSONObject();

        paymentLinkRequest.put("amount",amount);
        paymentLinkRequest.put("currency","INR");
        paymentLinkRequest.put("accept_partial",true);
        paymentLinkRequest.put("first_min_partial_amount",1000);
        paymentLinkRequest.put("expire_by", System.currentTimeMillis() + 10*60*1000 );
        paymentLinkRequest.put("reference_id","TS1989");
        paymentLinkRequest.put("description","Payment for a Notebook");
        JSONObject customer = new JSONObject();
        customer.put("name","Jeeva");
        customer.put("contact","+91 4145223980");
        customer.put("email","jnand1227@gmail.com");
        paymentLinkRequest.put("customer",customer);
        JSONObject notify = new JSONObject();
        notify.put("sms",true);
        notify.put("email",true);
        paymentLinkRequest.put("reminder_enable",true);
        paymentLinkRequest.put("callback_url","https://www.google.com/");
        paymentLinkRequest.put("callback_method","get");

        PaymentLink payment = razorpayClient.paymentLink.create(paymentLinkRequest);

        return payment.toString();
    }

}
