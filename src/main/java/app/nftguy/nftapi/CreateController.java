package app.nftguy.nftapi;

import app.nftguy.nftapi.model.NftTransactionDraft;
import com.bloxbean.cardano.client.api.exception.ApiException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class CreateController {

    NftCreateService creatorService;
    NftTransactionService transactionService;

    CreateController(NftCreateService creatorService, NftTransactionService transactionService){
         this.creatorService = creatorService;
         this.transactionService = transactionService;
    }

    @PostMapping("/create")
    public NftTransactionDraft create(@RequestParam(value="file") MultipartFile file,
                                      @RequestParam(value= "user_input")JSONObject userInput) throws Exception {
        validateUserInput(userInput);
        return creatorService.draftTransaction(file, userInput);
    }

    @GetMapping("/transaction")
    public NftTransactionDraft transactionStatus(@RequestParam(value="id") String id) throws ApiException {
        return transactionService.checkStatus(id);
    }

    private void validateUserInput(JSONObject userInput){
        if(!userInput.has("attributes")) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, String.format("Request must contain field '%s' ", "attributes"));
        } else if (!userInput.getJSONObject("attributes").has("name")) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, String.format("Request must contain field '%s' ", "name"));
        }else if (!userInput.has("receive_address")) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, String.format("Request must contain field '%s'", "receive_address"));
        }
    }


}
