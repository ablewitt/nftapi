package app.nftguy.nftapi;

import app.nftguy.nftapi.model.TransactionClientModel;
import com.bloxbean.cardano.client.api.exception.ApiException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@CrossOrigin(origins = "*")
@RestController
public class ApiController {

  CreateService creatorService;
  TransactionService transactionService;

  ApiController(CreateService creatorService, TransactionService transactionService) {
    this.creatorService = creatorService;
    this.transactionService = transactionService;
  }

  @PostMapping("/create")
  public TransactionClientModel create(
      @RequestParam(value = "file") MultipartFile file,
      @RequestParam(value = "user_input") JSONObject userInput)
      throws Exception {
    validateUserInput(userInput);
    return creatorService.primeTransaction(file, userInput);
  }

  @GetMapping("/transaction")
  public TransactionClientModel transactionStatus(@RequestParam(value = "id") String id)
      throws ApiException {
    return transactionService.checkStatus(id);
  }

  private void validateUserInput(JSONObject userInput) {
    if (!userInput.has("attributes")) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, String.format("Request must contain field '%s' ", "attributes"));
    } else if (!userInput.getJSONObject("attributes").has("name")) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, String.format("Request must contain field '%s' ", "name"));
    } else if (!userInput.has("receive_address")) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          String.format("Request must contain field '%s'", "receive_address"));
    }
  }
}
