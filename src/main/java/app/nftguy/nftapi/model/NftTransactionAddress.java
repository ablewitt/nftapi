package app.nftguy.nftapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NftTransactionAddress {

    private String id;

    private String nftPayAddress;

    private PaymentState paymentState;
}
