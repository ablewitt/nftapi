package app.nftguy.nftapi.helper;

import app.nftguy.nftapi.model.PaymentState;
import app.nftguy.nftapi.model.TransactionAddress;
import app.nftguy.nftapi.repository.TransactionRepository;
import com.bloxbean.cardano.client.api.exception.ApiException;
import com.bloxbean.cardano.client.backend.api.AddressService;
import com.bloxbean.cardano.client.backend.model.TxContentOutputAmount;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class AddressHelper {

  AddressService addressService;
  CardanoWalletHelper cardanoWalletHelper;

  TransactionRepository transactionRepository;

  public AddressHelper(
      BlockFrostHelper blockFrostHelper,
      CardanoWalletHelper cardanoWalletHelper,
      TransactionRepository transactionRepository) {
    this.addressService = blockFrostHelper.getAddressService();
    this.cardanoWalletHelper = cardanoWalletHelper;
    this.transactionRepository = transactionRepository;
  }

  public BigInteger getAddressBalance(String address) throws ApiException {
    BigInteger lovelaceBalance = new BigInteger("0");
    try {
      for (TxContentOutputAmount balance :
          addressService.getAddressInfo(address).getValue().getAmount()) {
        if (balance.getUnit().equalsIgnoreCase("lovelace")) {
          lovelaceBalance = lovelaceBalance.add(new BigInteger(balance.getQuantity()));
        }
      }
      return lovelaceBalance;
    } catch (NullPointerException e) { // a new address with no balance will return null
      return lovelaceBalance;
    }
  }

  public String getNextAvailableAddress() {
    List<TransactionAddress> pendingAddresses = new ArrayList<>();
    pendingAddresses.addAll(transactionRepository.findAddressByPaymentState(PaymentState.PENDING));
    pendingAddresses.addAll(
        transactionRepository.findAddressByPaymentState(PaymentState.UPLOADING));
    pendingAddresses.addAll(
        transactionRepository.findAddressByPaymentState(PaymentState.COMPLETED));
    List<String> unusedAddresses = cardanoWalletHelper.getUnusedAddresses();
    String nextAddress = "";

    for (String unusedAddress : unusedAddresses) {
      nextAddress = unusedAddress;
      for (TransactionAddress pendingAddress : pendingAddresses) {
        if (nextAddress.equals(pendingAddress.getNftPayAddress())) {
          nextAddress = "";
          break;
        }
      }
      if (!nextAddress.equals("")) {
        return nextAddress;
      }
    }
    return nextAddress;
  }
}
