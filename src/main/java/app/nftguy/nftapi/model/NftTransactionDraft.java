package app.nftguy.nftapi.model;

import lombok.Data;

import java.math.BigInteger;

@Data
public class NftTransactionDraft {

    private String id;

    private String transactionId;

    private String nftPayAddress;

    private String nftRxAddress;

    private BigInteger networkFee;

    private BigInteger createFee;

    private PaymentState paymentState;


    private BigInteger nftPayAddressBalance;

    public NftTransactionDraft(String id,
                               String transactionId,
                               String nftPayAddress,
                               String nftRxAddress,
                               BigInteger networkFee,
                               BigInteger createFee,
                               PaymentState paymentState) {
        this.id = id;
        this.transactionId = transactionId;
        this.nftPayAddress = nftPayAddress;
        this.nftRxAddress = nftRxAddress;
        this.networkFee = networkFee;
        this.createFee = createFee;
        this.paymentState = paymentState;
    }
}
